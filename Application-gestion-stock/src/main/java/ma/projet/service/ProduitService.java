package ma.projet.service;

import ma.projet.classes.Commande;
import ma.projet.classes.LigneCommandeProduit;
import ma.projet.classes.Produit;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProduitService implements IDao<Produit> {

    // ---------- CRUD ----------

    @Override
    public boolean create(Produit o) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(o);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean update(Produit o) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(o);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean delete(Produit o) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(o);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public Produit findById(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.get(Produit.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<Produit> findAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.createQuery("FROM Produit", Produit.class).list();
        } finally {
            session.close();
        }
    }

    // ---------- Méthodes demandées par le TP ----------

    // 1) Produits par catégorie
    public void afficherProduitsParCategorie(int categorieId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            List<Produit> produits = session.createQuery(
                            "FROM Produit p WHERE p.categorie.id = :id", Produit.class)
                    .setParameter("id", categorieId)
                    .list();

            System.out.println("Produits de la catégorie " + categorieId + " :");
            System.out.println("Référence\tPrix");
            for (Produit p : produits) {
                System.out.println(p.getReference() + "\t\t" + p.getPrix() + " DH");
            }
        } finally {
            session.close();
        }
    }

    // 2) Produits commandés entre deux dates
    public void afficherProduitsEntreDates(Date debut, Date fin) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            List<LigneCommandeProduit> lignes = session.createQuery(
                            "SELECT l FROM LigneCommandeProduit l " +
                                    "JOIN FETCH l.produit JOIN FETCH l.commande c " +
                                    "WHERE c.date BETWEEN :debut AND :fin", LigneCommandeProduit.class)
                    .setParameter("debut", debut)
                    .setParameter("fin", fin)
                    .list();

            System.out.println("Produits commandés entre deux dates :");
            System.out.println("Référence\tPrix\t\tQuantité");
            for (LigneCommandeProduit l : lignes) {
                System.out.println(l.getProduit().getReference() + "\t\t"
                        + l.getProduit().getPrix() + " DH\t" + l.getQuantite());
            }
        } finally {
            session.close();
        }
    }

    // 3) Produits d'une commande donnée (format de l'énoncé)
    public void afficherProduitsParCommande(int commandeId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Commande c = session.get(Commande.class, commandeId);
            if (c == null) {
                System.out.println("Commande introuvable : " + commandeId);
                return;
            }

            List<LigneCommandeProduit> lignes = session.createQuery(
                            "SELECT l FROM LigneCommandeProduit l " +
                                    "JOIN FETCH l.produit WHERE l.commande.id = :id", LigneCommandeProduit.class)
                    .setParameter("id", commandeId)
                    .list();

            SimpleDateFormat sdf = new SimpleDateFormat("d MMMM yyyy", Locale.FRENCH);
            System.out.println("Commande : " + c.getId() + "     Date : " + sdf.format(c.getDate()));
            System.out.println("Liste des produits :");
            System.out.println("Référence\tPrix\t\tQuantité");
            for (LigneCommandeProduit l : lignes) {
                System.out.println(l.getProduit().getReference() + "\t\t"
                        + l.getProduit().getPrix() + " DH\t" + l.getQuantite());
            }
        } finally {
            session.close();
        }
    }

    // 4) Produits dont le prix > 100 DH (requête nommée)
    public void afficherProduitsPrixSuperieur(float prix) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            List<Produit> produits = session.createNamedQuery("Produit.prixSuperieur", Produit.class)
                    .setParameter("prix", prix)
                    .list();

            System.out.println("Produits dont le prix > " + prix + " DH :");
            System.out.println("Référence\tPrix");
            for (Produit p : produits) {
                System.out.println(p.getReference() + "\t\t" + p.getPrix() + " DH");
            }
        } finally {
            session.close();
        }
    }
}