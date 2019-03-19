package CDiesel72.Implementation;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import java.util.List;

public abstract class EntityDAO<T, K> {
    private final EntityManager em;
    private String queryDAO;

    public EntityDAO(EntityManager em) {
        this.em = em;
    }

    public EntityManager getEm() {
        return em;
    }

    public String getQueryDAO() {
        return queryDAO;
    }

    public void setQueryDAO(String queryDAO) {
        this.queryDAO = queryDAO;
    }

    public void add(T t) {
        try {
            em.getTransaction().begin();
            em.persist(t);
            em.getTransaction().commit();
        } catch (Exception ex) {
            System.out.println("ERROR: Ошибка записи в базу данных Object");
            em.getTransaction().rollback();
        }
    }

    public void addList(List<T> lt) {
        try {
            em.getTransaction().begin();
            lt.forEach(t -> em.persist(t));
            em.getTransaction().commit();
        } catch (Exception ex) {
            System.out.println("ERROR: Ошибка записи в базу данных LIST");
            em.getTransaction().rollback();
        }
    }

    public void del(T t) {
        try {
            em.getTransaction().begin();
            em.remove(t);
            em.getTransaction().commit();
        } catch (Exception ex) {
            System.out.println("ERROR: Ошибка удаления из базы данных Object");
            em.getTransaction().rollback();
        }
    }

    public void delList(List<T> lt) {
        try {
            em.getTransaction().begin();
            lt.forEach(t -> em.remove(t));
            em.getTransaction().commit();
        } catch (Exception ex) {
            System.out.println("ERROR: Ошибка удаления из базы данных List");
            em.getTransaction().rollback();
        }
    }

    public void delAll() {
        List<T> lt = selAll();
        delList(lt);
    }

    public List<T> getMultiSelect(Query query) {
        List<T> ts = (List<T>) query.getResultList();
        return ts;
    }

    public T getSingleSelect(Query query) {
        T t = null;

        try {
            t = (T) query.getSingleResult();
        } catch (NoResultException ex) {
            System.out.println("ERROR: Нет результата соответствующему запросу");
        } catch (NonUniqueResultException ex) {
            System.out.println("ERROR: Нет уникального результата");
        }

        return t;
    }

    public List<T> selAll() {
        Query query = getEm().createQuery(queryDAO);
        return getMultiSelect(query);
    }

    public T selId(K id) {
        Query query = getEm().createQuery(queryDAO + " WHERE id = :id");
        query.setParameter("id", id);
        return getSingleSelect(query);
    }

}
