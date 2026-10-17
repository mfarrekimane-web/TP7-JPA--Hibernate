package ma.projet.services;

import ma.projet.classes.Employe;
import ma.projet.classes.Projet;
import ma.projet.classes.Tache;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class EmployeService implements IDao<Employe> {

    @Override
    public boolean create(Employe o) {
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
    public boolean update(Employe o) {
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
    public boolean delete(Employe o) {
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
    public Employe findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Employe.class, id);
        }
    }

    @Override
    public List<Employe> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Employe", Employe.class).list();
        }
    }

    // ---------- Méthodes spécifiques ----------

    /** Liste des tâches réalisées par un employé. */
    public List<Tache> getTachesRealisees(int employeId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "select et.tache from EmployeTache et where et.employe.id = :id", Tache.class)
                    .setParameter("id", employeId)
                    .list();
        }
    }

    /** Liste des projets gérés par un employé (chef de projet). */
    public List<Projet> getProjetsGeres(int employeId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "from Projet p where p.chefProjet.id = :id", Projet.class)
                    .setParameter("id", employeId)
                    .list();
        }
    }

    public void afficherTachesRealisees(int employeId) {
        System.out.println("Tâches réalisées par l'employé " + employeId + " :");
        getTachesRealisees(employeId).forEach(t -> System.out.println("  " + t));
    }

    public void afficherProjetsGeres(int employeId) {
        System.out.println("Projets gérés par l'employé " + employeId + " :");
        getProjetsGeres(employeId).forEach(p -> System.out.println("  " + p));
    }
}