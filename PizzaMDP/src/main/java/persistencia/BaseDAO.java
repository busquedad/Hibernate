package persistencia;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import org.hibernate.query.Query;
import java.lang.reflect.ParameterizedType;

public abstract class BaseDAO<T> {

    private static final Logger logger = LoggerFactory.getLogger(BaseDAO.class);
    private final Class<T> persistentClass;

    @SuppressWarnings("unchecked")
    public BaseDAO() {
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public void guardar(T entity) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(entity);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            logger.error("Error guardando la entidad", e);
        } finally {
            session.close();
        }
    }

    public T obtenerPorId(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        T entity = null;
        try {
            entity = session.get(persistentClass, id);
        } catch (Exception e) {
            logger.error("Error obteniendo la entidad por id", e);
        } finally {
            session.close();
        }
        return entity;
    }

    public List<T> obtenerTodos() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<T> entities = null;
        try {
            Query<T> query = session.createQuery("FROM " + persistentClass.getName(), persistentClass);
            entities = query.list();
        } catch (Exception e) {
            logger.error("Error obteniendo todas las entidades", e);
        } finally {
            session.close();
        }
        return entities;
    }

    public void actualizar(T entity) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(entity);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            logger.error("Error actualizando la entidad", e);
        } finally {
            session.close();
        }
    }

    public void eliminar(T entity) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(entity);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            logger.error("Error eliminando la entidad", e);
        } finally {
            session.close();
        }
    }
}
