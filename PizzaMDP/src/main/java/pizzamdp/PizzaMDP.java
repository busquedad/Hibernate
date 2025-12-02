package pizzamdp;

import static spark.Spark.*;
import com.google.gson.Gson;
import entidades.TamanioPizza;
import entidades.TipoPizza;
import entidades.VariedadPizza;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import persistencia.HibernateUtil;
import persistencia.TamanioPizzaDAO;
import persistencia.TipoPizzaDAO;
import persistencia.VariedadPizzaDAO;
import java.util.List;
import java.io.File;

/**
 * Main class for the PizzaMDP application.
 * This class is responsible for bootstrapping the application,
 * seeding initial data, and defining the API endpoints.
 */
public class PizzaMDP {

    private static final Logger logger = LoggerFactory.getLogger(PizzaMDP.class);

    /**
     * Seeds the database with initial data if it's empty.
     * This method adds two default pizza sizes ("Chica" and "Grande") to the database.
     * It handles transactions and rolls back on failure.
     */
    public static void bootstrapData() {
        logger.debug("--- [DEBUG] bootstrapData START ---");
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            logger.debug("--- [DEBUG] Transaction BEGIN ---");

            if (session.createQuery("from TamanioPizza").list().isEmpty()) {
                logger.debug("--- [DEBUG] TamanioPizza table is empty. Seeding data... ---");

                TamanioPizza t1 = new TamanioPizza();
                t1.setNombre("Chica");
                t1.setCant_porciones(4);
                logger.debug("--- [DEBUG] Saving t1: {} ---", t1.getNombre());
                session.save(t1);

                TamanioPizza t2 = new TamanioPizza();
                t2.setNombre("Grande");
                t2.setCant_porciones(8);
                logger.debug("--- [DEBUG] Saving t2: {} ---", t2.getNombre());
                session.save(t2);

                logger.debug("--- [DEBUG] Committing transaction ---");
                tx.commit();
                logger.debug("--- [DEBUG] Transaction COMMIT successful ---");
            } else {
                logger.debug("--- [DEBUG] TamanioPizza table already has data. Skipping seed. ---");
            }
        } catch (Exception e) {
            logger.error("--- [DEBUG] BOOTSTRAP FAILED ---", e);
            if (tx != null) {
                try {
                    logger.error("--- [DEBUG] Rolling back transaction ---");
                    tx.rollback();
                } catch (Exception rbEx) {
                    logger.error("--- [DEBUG] Could not rollback transaction ---", rbEx);
                }
            }
        }
        logger.debug("--- [DEBUG] bootstrapData END ---");
    }

    /**
     * The main entry point for the application.
     * It initializes the application, sets up the server port,
     * configures CORS, and defines the API endpoints.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        new File("data").mkdirs();
        bootstrapData();

        // Use port 4567 for tests and 8080 for production
        boolean isTest = false;
        for (String arg : args) {
            if (arg.equals("test")) {
                isTest = true;
                break;
            }
        }

        if (isTest) {
            port(4567);
        } else {
            port(8080);
        }

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
        TamanioPizzaDAO tamanioPizzaDAO = new TamanioPizzaDAO();
        TipoPizzaDAO tipoPizzaDAO = new TipoPizzaDAO();
        VariedadPizzaDAO variedadPizzaDAO = new VariedadPizzaDAO();

        // API endpoints for TamanioPizza
        post("/api/tamanio-pizzas", (req, res) -> {
            res.type("application/json");
            TamanioPizza tamanioPizza = gson.fromJson(req.body(), TamanioPizza.class);
            tamanioPizzaDAO.guardar(tamanioPizza);
            return gson.toJson(tamanioPizza);
        });

        get("/api/tamanio-pizzas", (req, res) -> {
            res.type("application/json");
            return gson.toJson(tamanioPizzaDAO.obtenerTodos());
        });

        get("/api/tamanio-pizzas/:id", (req, res) -> {
            res.type("application/json");
            Integer id = Integer.parseInt(req.params(":id"));
            TamanioPizza tamanioPizza = tamanioPizzaDAO.obtenerPorId(id);
            if (tamanioPizza != null) {
                return gson.toJson(tamanioPizza);
            } else {
                res.status(404);
                return gson.toJson("TamanioPizza not found");
            }
        });

        put("/api/tamanio-pizzas/:id", (req, res) -> {
            res.type("application/json");
            Integer id = Integer.parseInt(req.params(":id"));
            TamanioPizza tamanioPizza = gson.fromJson(req.body(), TamanioPizza.class);
            tamanioPizza.setId_tamanio_pizza(id);
            tamanioPizzaDAO.actualizar(tamanioPizza);
            return gson.toJson(tamanioPizza);
        });

        delete("/api/tamanio-pizzas/:id", (req, res) -> {
            res.type("application/json");
            Integer id = Integer.parseInt(req.params(":id"));
            TamanioPizza tamanioPizza = tamanioPizzaDAO.obtenerPorId(id);
            if (tamanioPizza != null) {
                tamanioPizzaDAO.eliminar(tamanioPizza);
                return gson.toJson("TamanioPizza deleted");
            } else {
                res.status(404);
                return gson.toJson("TamanioPizza not found");
            }
        });

        // API endpoints for TipoPizza
        post("/api/tipo-pizzas", (req, res) -> {
            res.type("application/json");
            TipoPizza tipoPizza = gson.fromJson(req.body(), TipoPizza.class);
            tipoPizzaDAO.guardar(tipoPizza);
            return gson.toJson(tipoPizza);
        });

        get("/api/tipo-pizzas", (req, res) -> {
            res.type("application/json");
            return gson.toJson(tipoPizzaDAO.obtenerTodos());
        });

        get("/api/tipo-pizzas/:id", (req, res) -> {
            res.type("application/json");
            Integer id = Integer.parseInt(req.params(":id"));
            TipoPizza tipoPizza = tipoPizzaDAO.obtenerPorId(id);
            if (tipoPizza != null) {
                return gson.toJson(tipoPizza);
            } else {
                res.status(404);
                return gson.toJson("TipoPizza not found");
            }
        });

        put("/api/tipo-pizzas/:id", (req, res) -> {
            res.type("application/json");
            Integer id = Integer.parseInt(req.params(":id"));
            TipoPizza tipoPizza = gson.fromJson(req.body(), TipoPizza.class);
            tipoPizza.setId_tipo_pizza(id);
            tipoPizzaDAO.actualizar(tipoPizza);
            return gson.toJson(tipoPizza);
        });

        delete("/api/tipo-pizzas/:id", (req, res) -> {
            res.type("application/json");
            Integer id = Integer.parseInt(req.params(":id"));
            TipoPizza tipoPizza = tipoPizzaDAO.obtenerPorId(id);
            if (tipoPizza != null) {
                tipoPizzaDAO.eliminar(tipoPizza);
                return gson.toJson("TipoPizza deleted");
            } else {
                res.status(404);
                return gson.toJson("TipoPizza not found");
            }
        });

        // API endpoints for VariedadPizza
        post("/api/variedad-pizzas", (req, res) -> {
            res.type("application/json");
            VariedadPizza variedadPizza = gson.fromJson(req.body(), VariedadPizza.class);
            variedadPizzaDAO.guardar(variedadPizza);
            return gson.toJson(variedadPizza);
        });

        get("/api/variedad-pizzas", (req, res) -> {
            res.type("application/json");
            return gson.toJson(variedadPizzaDAO.obtenerTodos());
        });

        get("/api/variedad-pizzas/:id", (req, res) -> {
            res.type("application/json");
            Integer id = Integer.parseInt(req.params(":id"));
            VariedadPizza variedadPizza = variedadPizzaDAO.obtenerPorId(id);
            if (variedadPizza != null) {
                return gson.toJson(variedadPizza);
            } else {
                res.status(404);
                return gson.toJson("VariedadPizza not found");
            }
        });

        put("/api/variedad-pizzas/:id", (req, res) -> {
            res.type("application/json");
            Integer id = Integer.parseInt(req.params(":id"));
            VariedadPizza variedadPizza = gson.fromJson(req.body(), VariedadPizza.class);
            variedadPizza.setId_variedad_pizza(id);
            variedadPizzaDAO.actualizar(variedadPizza);
            return gson.toJson(variedadPizza);
        });

        delete("/api/variedad-pizzas/:id", (req, res) -> {
            res.type("application/json");
            Integer id = Integer.parseInt(req.params(":id"));
            VariedadPizza variedadPizza = variedadPizzaDAO.obtenerPorId(id);
            if (variedadPizza != null) {
                variedadPizzaDAO.eliminar(variedadPizza);
                return gson.toJson("VariedadPizza deleted");
            } else {
                res.status(404);
                return gson.toJson("VariedadPizza not found");
            }
        });
    }
}
