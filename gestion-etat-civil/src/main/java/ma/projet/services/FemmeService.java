package ma.projet.services;

import ma.projet.classes.Femme;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

public class FemmeService implements IDao<Femme> {

    @Override
    public boolean create(Femme o) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.save(o);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Femme o) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.update(o);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Femme o) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.delete(o);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Femme findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Femme.class, id);
        }
    }

    @Override
    public List<Femme> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Femme", Femme.class).list();
        }
    }

    // ---------- Méthodes spécifiques ----------

    /** Requête native nommée : nombre d'enfants d'une femme sur la période [d1, d2]. */
    public int getNbEnfantsEntreDates(int femmeId, Date d1, Date d2) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Object resultat = session.createNamedQuery("Femme.nbEnfantsEntreDates")
                    .setParameter("femmeId", femmeId)
                    .setParameter("d1", d1, TemporalType.DATE)
                    .setParameter("d2", d2, TemporalType.DATE)
                    .getSingleResult();
            return ((Number) resultat).intValue();
        }
    }

    /** Requête nommée (HQL) : femmes mariées au moins deux fois. */
    public List<Femme> getFemmesMarieesAuMoinsDeuxFois() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createNamedQuery("Femme.mariesAuMoinsDeuxFois", Femme.class)
                    .getResultList();
        }
    }

    /** La femme la plus âgée = celle qui a la date de naissance la plus ancienne. */
    public Femme getFemmePlusAgee() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Femme f order by f.dateNaissance asc", Femme.class)
                    .setMaxResults(1)
                    .uniqueResult();
        }
    }
}