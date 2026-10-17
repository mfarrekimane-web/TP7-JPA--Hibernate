package ma.projet.services;

import ma.projet.classes.EmployeTache;
import ma.projet.classes.Projet;
import ma.projet.classes.Tache;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ProjetService implements IDao<Projet> {

    @Override
    public boolean create(Projet o) {
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
    public boolean update(Projet o) {
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
    public boolean delete(Projet o) {
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
    public Projet findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Projet.class, id);
        }
    }

    @Override
    public List<Projet> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Projet", Projet.class).list();
        }
    }

    // ---------- Méthodes spécifiques ----------

    /** Tâches planifiées pour un projet (dates prévues). */
    public List<Tache> getTachesPlanifiees(int projetId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "from Tache t where t.projet.id = :id", Tache.class)
                    .setParameter("id", projetId)
                    .list();
        }
    }

    public void afficherTachesPlanifiees(int projetId) {
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        System.out.println("Tâches planifiées du projet " + projetId + " :");
        System.out.printf("%-5s %-15s %-12s %-12s%n", "Num", "Nom", "Date Début", "Date Fin");
        for (Tache t : getTachesPlanifiees(projetId)) {
            System.out.printf("%-5d %-15s %-12s %-12s%n",
                    t.getId(), t.getNom(),
                    f.format(t.getDateDebut()), f.format(t.getDateFin()));
        }
    }

    /** Tâches réalisées avec les dates réelles (affichage de l'énoncé). */
    public void afficherTachesRealisees(int projetId) {
        SimpleDateFormat fLong = new SimpleDateFormat("d MMMM yyyy", Locale.FRENCH);
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Projet p = session.get(Projet.class, projetId);
            if (p == null) {
                System.out.println("Projet " + projetId + " introuvable");
                return;
            }

            System.out.println("Projet : " + p.getId() + "      Nom : " + p.getNom()
                    + "     Date début : " + fLong.format(p.getDateDebut()));
            System.out.println("Liste des tâches:");
            System.out.printf("%-5s %-15s %-20s %-15s%n",
                    "Num", "Nom", "Date Début Réelle", "Date Fin Réelle");

            List<EmployeTache> liste = session.createQuery(
                            "select et from EmployeTache et join fetch et.tache t "
                                    + "where t.projet.id = :id order by t.id", EmployeTache.class)
                    .setParameter("id", projetId)
                    .list();

            for (EmployeTache et : liste) {
                System.out.printf("%-5d %-15s %-20s %-15s%n",
                        et.getTache().getId(), et.getTache().getNom(),
                        f.format(et.getDateDebutReelle()), f.format(et.getDateFinReelle()));
            }
        }
    }
}