package ma.projet.services;

import ma.projet.classes.Homme;
import ma.projet.classes.Mariage;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HommeService implements IDao<Homme> {

    @Override
    public boolean create(Homme o) {
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
    public boolean update(Homme o) {
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
    public boolean delete(Homme o) {
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
    public Homme findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Homme.class, id);
        }
    }

    @Override
    public List<Homme> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Homme", Homme.class).list();
        }
    }

    // ---------- Méthodes spécifiques ----------

    /** Affiche les épouses d'un homme dont le mariage touche la période [d1, d2]. */
    public void afficherEpousesEntreDates(int hommeId, Date d1, Date d2) {
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Homme h = session.get(Homme.class, hommeId);
            if (h == null) {
                System.out.println("Homme " + hommeId + " introuvable");
                return;
            }

            List<Mariage> mariages = session.createQuery(
                            "select m from Mariage m join fetch m.femme "
                                    + "where m.homme.id = :id "
                                    + "and m.dateDebut <= :d2 "
                                    + "and (m.dateFin is null or m.dateFin >= :d1) "
                                    + "order by m.dateDebut", Mariage.class)
                    .setParameter("id", hommeId)
                    .setParameter("d1", d1)
                    .setParameter("d2", d2)
                    .list();

            System.out.println("Épouses de " + h.getNom() + " " + h.getPrenom()
                    + " entre " + f.format(d1) + " et " + f.format(d2) + " :");
            for (Mariage m : mariages) {
                System.out.println("  - " + m.getFemme().getNom() + " " + m.getFemme().getPrenom());
            }
        }
    }

    /** API Criteria : nombre d'hommes mariés à exactement 4 femmes sur la période [d1, d2]. */
    public int compterHommesMariesAQuatreFemmes(Date d1, Date d2) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);
            Root<Mariage> m = cq.from(Mariage.class);

            // le mariage chevauche la période demandée
            Predicate chevauche = cb.and(
                    cb.lessThanOrEqualTo(m.<Date>get("dateDebut"), d2),
                    cb.or(cb.isNull(m.<Date>get("dateFin")),
                            cb.greaterThanOrEqualTo(m.<Date>get("dateFin"), d1)));

            // select homme_id from mariage where ... group by homme_id having count(*) = 4
            cq.select(m.get("homme").<Integer>get("id"))
                    .where(chevauche)
                    .groupBy(m.get("homme").get("id"))
                    .having(cb.equal(cb.count(m), 4L));

            List<Integer> hommes = session.createQuery(cq).getResultList();
            return hommes.size();
        }
    }

    /** Affiche tous les mariages d'un homme (en cours puis échoués). */
    public void afficherMariages(int hommeId) {
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Homme h = session.get(Homme.class, hommeId);
            if (h == null) {
                System.out.println("Homme " + hommeId + " introuvable");
                return;
            }

            List<Mariage> mariages = session.createQuery(
                            "select m from Mariage m join fetch m.femme "
                                    + "where m.homme.id = :id order by m.dateDebut", Mariage.class)
                    .setParameter("id", hommeId)
                    .list();

            System.out.println("Nom : " + (h.getNom() + " " + h.getPrenom()).toUpperCase());

            System.out.println("Mariages En Cours :");
            int i = 1;
            for (Mariage m : mariages) {
                if (m.getDateFin() == null) {
                    System.out.printf("%d. Femme : %-18s Date Début : %s    Nbr Enfants : %d%n",
                            i++,
                            (m.getFemme().getNom() + " " + m.getFemme().getPrenom()).toUpperCase(),
                            f.format(m.getDateDebut()), m.getNbrEnfant());
                }
            }

            System.out.println();
            System.out.println("Mariages échoués :");
            i = 1;
            for (Mariage m : mariages) {
                if (m.getDateFin() != null) {
                    System.out.printf("%d. Femme : %-18s Date Début : %s%n   Date Fin : %s    Nbr Enfants : %d%n",
                            i++,
                            (m.getFemme().getNom() + " " + m.getFemme().getPrenom()).toUpperCase(),
                            f.format(m.getDateDebut()), f.format(m.getDateFin()), m.getNbrEnfant());
                }
            }
        }
    }
}