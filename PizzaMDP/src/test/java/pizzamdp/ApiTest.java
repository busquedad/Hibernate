package pizzamdp;

import com.google.gson.Gson;
import entidades.TamanioPizza;
import entidades.TipoPizza;
import entidades.VariedadPizza;
import okhttp3.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import spark.Spark;
import persistencia.HibernateUtil;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ApiTest {

    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();
    private static final String BASE_URL = "http://localhost:4567/api";

    @BeforeClass
    public static void setUp() {
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        configuration.setProperty("hibernate.connection.url", "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        configuration.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        HibernateUtil.setSessionFactory(sessionFactory);

        PizzaMDP.main(new String[]{"test"});
        Spark.awaitInitialization();
    }

    @AfterClass
    public static void tearDown() {
        Spark.stop();
    }

    @Test
    public void testTamanioPizzaAPI() throws IOException {
        // Create
        TamanioPizza tamanio = new TamanioPizza();
        tamanio.setNombre("Test Size API");
        tamanio.setCant_porciones(6);
        RequestBody body = RequestBody.create(gson.toJson(tamanio), MediaType.get("application/json"));
        Request request = new Request.Builder().url(BASE_URL + "/tamanio-pizzas").post(body).build();
        try (Response response = client.newCall(request).execute()) {
            assertEquals(200, response.code());
            TamanioPizza createdTamanio = gson.fromJson(response.body().string(), TamanioPizza.class);
            tamanio.setId_tamanio_pizza(createdTamanio.getId_tamanio_pizza());
        }

        // Read
        request = new Request.Builder().url(BASE_URL + "/tamanio-pizzas/" + tamanio.getId_tamanio_pizza()).build();
        try (Response response = client.newCall(request).execute()) {
            assertEquals(200, response.code());
            TamanioPizza retrievedTamanio = gson.fromJson(response.body().string(), TamanioPizza.class);
            assertEquals("Test Size API", retrievedTamanio.getNombre());
        }

        // Update
        tamanio.setNombre("Updated Size API");
        body = RequestBody.create(gson.toJson(tamanio), MediaType.get("application/json"));
        request = new Request.Builder().url(BASE_URL + "/tamanio-pizzas/" + tamanio.getId_tamanio_pizza()).put(body).build();
        try (Response response = client.newCall(request).execute()) {
            assertEquals(200, response.code());
        }

        // Read All
        request = new Request.Builder().url(BASE_URL + "/tamanio-pizzas").build();
        try (Response response = client.newCall(request).execute()) {
            assertEquals(200, response.code());
        }

        // Delete
        request = new Request.Builder().url(BASE_URL + "/tamanio-pizzas/" + tamanio.getId_tamanio_pizza()).delete().build();
        try (Response response = client.newCall(request).execute()) {
            assertEquals(200, response.code());
        }
    }

    @Test
    public void testTipoPizzaAPI() throws IOException {
        // Create
        TipoPizza tipo = new TipoPizza();
        tipo.setNombre("Test Type API");
        tipo.setDescripcionPizza("Test Description API");
        RequestBody body = RequestBody.create(gson.toJson(tipo), MediaType.get("application/json"));
        Request request = new Request.Builder().url(BASE_URL + "/tipo-pizzas").post(body).build();
        try (Response response = client.newCall(request).execute()) {
            assertEquals(200, response.code());
            TipoPizza createdTipo = gson.fromJson(response.body().string(), TipoPizza.class);
            tipo.setId_tipo_pizza(createdTipo.getId_tipo_pizza());
        }

        // Read
        request = new Request.Builder().url(BASE_URL + "/tipo-pizzas/" + tipo.getId_tipo_pizza()).build();
        try (Response response = client.newCall(request).execute()) {
            assertEquals(200, response.code());
            TipoPizza retrievedTipo = gson.fromJson(response.body().string(), TipoPizza.class);
            assertEquals("Test Type API", retrievedTipo.getNombre());
        }

        // Update
        tipo.setNombre("Updated Type API");
        body = RequestBody.create(gson.toJson(tipo), MediaType.get("application/json"));
        request = new Request.Builder().url(BASE_URL + "/tipo-pizzas/" + tipo.getId_tipo_pizza()).put(body).build();
        try (Response response = client.newCall(request).execute()) {
            assertEquals(200, response.code());
        }

        // Read All
        request = new Request.Builder().url(BASE_URL + "/tipo-pizzas").build();
        try (Response response = client.newCall(request).execute()) {
            assertEquals(200, response.code());
        }

        // Delete
        request = new Request.Builder().url(BASE_URL + "/tipo-pizzas/" + tipo.getId_tipo_pizza()).delete().build();
        try (Response response = client.newCall(request).execute()) {
            assertEquals(200, response.code());
        }
    }

    @Test
    public void testVariedadPizzaAPI() throws IOException {
        // Create
        VariedadPizza variedad = new VariedadPizza();
        variedad.setNombre("Test Variety API");
        variedad.setIngredientes("Test Ingredients API");
        RequestBody body = RequestBody.create(gson.toJson(variedad), MediaType.get("application/json"));
        Request request = new Request.Builder().url(BASE_URL + "/variedad-pizzas").post(body).build();
        try (Response response = client.newCall(request).execute()) {
            assertEquals(200, response.code());
            VariedadPizza createdVariedad = gson.fromJson(response.body().string(), VariedadPizza.class);
            variedad.setId_variedad_pizza(createdVariedad.getId_variedad_pizza());
        }

        // Read
        request = new Request.Builder().url(BASE_URL + "/variedad-pizzas/" + variedad.getId_variedad_pizza()).build();
        try (Response response = client.newCall(request).execute()) {
            assertEquals(200, response.code());
            VariedadPizza retrievedVariedad = gson.fromJson(response.body().string(), VariedadPizza.class);
            assertEquals("Test Variety API", retrievedVariedad.getNombre());
        }

        // Update
        variedad.setNombre("Updated Variety API");
        body = RequestBody.create(gson.toJson(variedad), MediaType.get("application/json"));
        request = new Request.Builder().url(BASE_URL + "/variedad-pizzas/" + variedad.getId_variedad_pizza()).put(body).build();
        try (Response response = client.newCall(request).execute()) {
            assertEquals(200, response.code());
        }

        // Read All
        request = new Request.Builder().url(BASE_URL + "/variedad-pizzas").build();
        try (Response response = client.newCall(request).execute()) {
            assertEquals(200, response.code());
        }

        // Delete
        request = new Request.Builder().url(BASE_URL + "/variedad-pizzas/" + variedad.getId_variedad_pizza()).delete().build();
        try (Response response = client.newCall(request).execute()) {
            assertEquals(200, response.code());
        }
    }
}
