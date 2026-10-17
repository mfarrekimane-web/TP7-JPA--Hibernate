package ma.projet;

import ma.projet.classes.Employe;
import ma.projet.classes.EmployeTache;
import ma.projet.classes.Projet;
import ma.projet.classes.Tache;
import ma.projet.services.EmployeService;
import ma.projet.services.EmployeTacheService;
import ma.projet.services.ProjetService;
import ma.projet.services.TacheService;
import ma.projet.util.HibernateUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class App {

    private static final SimpleDateFormat F = new SimpleDateFormat("dd/MM/yyyy");

    private static Date d(String s) throws Exception {
        return F.parse(s);
    }

    public static void main(String[] args) throws Exception {
        EmployeService employeService = new EmployeService();
        ProjetService projetService = new ProjetService();
        TacheService tacheService = new TacheService();
        EmployeTacheService etService = new EmployeTacheService();

        // ---------- 1. Jeu de données ----------
        Employe e1 = new Employe("Alami", "Karim", "0600000001");
        Employe e2 = new Employe("Bennani", "Sara", "0600000002");
        employeService.create(e1);
        employeService.create(e2);

        Projet p = new Projet("Gestion de stock", d("14/01/2013"), d("30/06/2013"), e1);
        projetService.create(p);

        Tache t1 = new Tache("Analyse",       d("10/02/2013"), d("20/02/2013"), 800,  p);
        Tache t2 = new Tache("Conception",    d("10/03/2013"), d("15/03/2013"), 1500, p);
        Tache t3 = new Tache("Développement", d("10/04/2013"), d("25/04/2013"), 3000, p);
        tacheService.create(t1);
        tacheService.create(t2);
        tacheService.create(t3);

        etService.create(new EmployeTache(e1, t1, d("10/02/2013"), d("20/02/2013")));
        etService.create(new EmployeTache(e2, t2, d("10/03/2013"), d("15/03/2013")));
        etService.create(new EmployeTache(e2, t3, d("10/04/2013"), d("25/04/2013")));

        // ---------- 2. EmployeService ----------
        System.out.println("\n===== EmployeService =====");
        employeService.afficherTachesRealisees(e2.getId());
        employeService.afficherProjetsGeres(e1.getId());

        // ---------- 3. ProjetService ----------
        System.out.println("\n===== ProjetService =====");
        projetService.afficherTachesPlanifiees(p.getId());
        System.out.println();
        projetService.afficherTachesRealisees(p.getId());

        // ---------- 4. TacheService ----------
        System.out.println("\n===== TacheService =====");
        System.out.println("Tâches avec prix > 1000 DH :");
        tacheService.getTachesPrixSuperieurA1000()
                .forEach(t -> System.out.println("  " + t));

        System.out.println("Tâches réalisées entre 01/03/2013 et 30/04/2013 :");
        tacheService.getTachesRealiseesEntre(d("01/03/2013"), d("30/04/2013"))
                .forEach(t -> System.out.println("  " + t));

        HibernateUtil.shutdown();
    }
}