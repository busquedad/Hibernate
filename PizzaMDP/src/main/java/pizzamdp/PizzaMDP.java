package pizzamdp;

import static spark.Spark.*;
import com.google.gson.Gson;
import entidades.TamanioPizza;
import org.hibernate.Session;
import org.hibernate.Transaction;
import persistencia.HibernateUtil;
import java.util.List;
import java.io.File;

public class PizzaMDP {

    public static void bootstrapData() {
        System.out.println("--- [DEBUG] bootstrapData START ---");
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            System.out.println("--- [DEBUG] Transaction BEGIN ---");

            if (session.createQuery("from TamanioPizza").list().isEmpty()) {
                System.out.println("--- [DEBUG] TamanioPizza table is empty. Seeding data... ---");

                TamanioPizza t1 = new TamanioPizza();
                t1.setNombre("Chica");
                t1.setCant_porciones(4);
                System.out.println("--- [DEBUG] Saving t1: " + t1.getNombre() + " ---");
                session.save(t1);

                TamanioPizza t2 = new TamanioPizza();
                t2.setNombre("Grande");
                t2.setCant_porciones(8);
                System.out.println("--- [DEBUG] Saving t2: " + t2.getNombre() + " ---");
                session.save(t2);

                System.out.println("--- [DEBUG] Committing transaction ---");
                tx.commit();
                System.out.println("--- [DEBUG] Transaction COMMIT successful ---");
            } else {
                System.out.println("--- [DEBUG] TamanioPizza table already has data. Skipping seed. ---");
            }
        } catch (Exception e) {
            System.err.println("--- [DEBUG] BOOTSTRAP FAILED ---");
            if (tx != null) {
                try {
                    System.err.println("--- [DEBUG] Rolling back transaction ---");
                    tx.rollback();
                } catch (Exception rbEx) {
                    System.err.println("--- [DEBUG] Could not rollback transaction: " + rbEx.getMessage() + " ---");
                }
            }
            e.printStackTrace(System.err);
        }
        System.out.println("--- [DEBUG] bootstrapData END ---");
    }

    public static void main(String[] args) {
        new File("data").mkdirs();
        bootstrapData();
        port(8080);

        // Se recomienda obtener el origen permitido de una variable de entorno en producción
        String allowedOrigin = System.getenv("ALLOWED_ORIGIN");
        if (allowedOrigin == null) {
            allowedOrigin = "http://localhost:5173";
        }
        final String finalAllowedOrigin = allowedOrigin;

        options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }
            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }
            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", finalAllowedOrigin);
        });

        Gson gson = new Gson();
        get("/api/pizzas", (req, res) -> {
            res.type("application/json");
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                List<TamanioPizza> pizzas = session.createQuery("from TamanioPizza", TamanioPizza.class).list();
                return gson.toJson(pizzas);
            } catch (Exception e) {
                // Se recomienda utilizar un logger en lugar de e.printStackTrace()
                // y no exponer los mensajes de excepción directamente al cliente
                res.status(500);
                return gson.toJson("Error interno del servidor");
            }
        });
    }
}
