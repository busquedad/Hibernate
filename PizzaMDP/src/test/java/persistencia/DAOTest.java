package persistencia;

import entidades.TamanioPizza;
import entidades.TipoPizza;
import entidades.VariedadPizza;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class DAOTest {

    private SessionFactory sessionFactory;
    private Session session;

    @Before
    public void setUp() {
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        configuration.setProperty("hibernate.connection.url", "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        configuration.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        sessionFactory = configuration.buildSessionFactory();
        session = sessionFactory.openSession();
        HibernateUtil.setSessionFactory(sessionFactory);
    }

    @After
    public void tearDown() {
        if (session != null) {
            session.close();
        }
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @Test
    public void testTamanioPizzaDAO() {
        TamanioPizzaDAO dao = new TamanioPizzaDAO();

        // Create
        TamanioPizza tamanio = new TamanioPizza();
        tamanio.setNombre("Test Size");
        tamanio.setCant_porciones(6);
        dao.guardar(tamanio);
        assertNotNull(tamanio.getId_tamanio_pizza());

        // Read
        TamanioPizza retrievedTamanio = dao.obtenerPorId(tamanio.getId_tamanio_pizza());
        assertEquals("Test Size", retrievedTamanio.getNombre());

        // Update
        retrievedTamanio.setNombre("Updated Size");
        dao.actualizar(retrievedTamanio);
        TamanioPizza updatedTamanio = dao.obtenerPorId(retrievedTamanio.getId_tamanio_pizza());
        assertEquals("Updated Size", updatedTamanio.getNombre());

        // Read All
        List<TamanioPizza> allTamanios = dao.obtenerTodos();
        assertEquals(1, allTamanios.size());

        // Delete
        dao.eliminar(updatedTamanio);
        TamanioPizza deletedTamanio = dao.obtenerPorId(updatedTamanio.getId_tamanio_pizza());
        assertNull(deletedTamanio);
    }

    @Test
    public void testTipoPizzaDAO() {
        TipoPizzaDAO dao = new TipoPizzaDAO();

        // Create
        TipoPizza tipo = new TipoPizza();
        tipo.setNombre("Test Type");
        tipo.setDescripcionPizza("Test Description");
        dao.guardar(tipo);
        assertNotNull(tipo.getId_tipo_pizza());

        // Read
        TipoPizza retrievedTipo = dao.obtenerPorId(tipo.getId_tipo_pizza());
        assertEquals("Test Type", retrievedTipo.getNombre());

        // Update
        retrievedTipo.setNombre("Updated Type");
        dao.actualizar(retrievedTipo);
        TipoPizza updatedTipo = dao.obtenerPorId(retrievedTipo.getId_tipo_pizza());
        assertEquals("Updated Type", updatedTipo.getNombre());

        // Read All
        List<TipoPizza> allTipos = dao.obtenerTodos();
        assertEquals(1, allTipos.size());

        // Delete
        dao.eliminar(updatedTipo);
        TipoPizza deletedTipo = dao.obtenerPorId(updatedTipo.getId_tipo_pizza());
        assertNull(deletedTipo);
    }

    @Test
    public void testVariedadPizzaDAO() {
        VariedadPizzaDAO dao = new VariedadPizzaDAO();

        // Create
        VariedadPizza variedad = new VariedadPizza();
        variedad.setNombre("Test Variety");
        variedad.setIngredientes("Test Ingredients");
        dao.guardar(variedad);
        assertNotNull(variedad.getId_variedad_pizza());

        // Read
        VariedadPizza retrievedVariedad = dao.obtenerPorId(variedad.getId_variedad_pizza());
        assertEquals("Test Variety", retrievedVariedad.getNombre());

        // Update
        retrievedVariedad.setNombre("Updated Variety");
        dao.actualizar(retrievedVariedad);
        VariedadPizza updatedVariedad = dao.obtenerPorId(retrievedVariedad.getId_variedad_pizza());
        assertEquals("Updated Variety", updatedVariedad.getNombre());

        // Read All
        List<VariedadPizza> allVariedades = dao.obtenerTodos();
        assertEquals(1, allVariedades.size());

        // Delete
        dao.eliminar(updatedVariedad);
        VariedadPizza deletedVariedad = dao.obtenerPorId(updatedVariedad.getId_variedad_pizza());
        assertNull(deletedVariedad);
    }
}
