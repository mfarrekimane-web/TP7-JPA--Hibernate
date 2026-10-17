package ma.projet;

import ma.projet.classes.Femme;
import ma.projet.classes.Homme;
import ma.projet.classes.Mariage;
import ma.projet.services.FemmeService;
import ma.projet.services.HommeService;
import ma.projet.services.MariageService;
import ma.projet.util.HibernateUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class App {

    private static final SimpleDateFormat F = new SimpleDateFormat("dd/MM/yyyy");

    private static Date d(String s) throws Exception {
        return F.parse(s);
    }

    public static void main(String[] args) throws Exception {
        HommeService hommeService = new HommeService();
        FemmeService femmeService = new FemmeService();
        MariageService mariageService = new MariageService();

        // ---------- 1. Créer 10 femmes ----------
        Femme f1  = new Femme("Rami",       "Salima", "0611111101", "Casablanca", d("12/03/1965"));
        Femme f2  = new Femme("Ali",        "Amal",   "0611111102", "Rabat",      d("25/07/1970"));
        Femme f3  = new Femme("Alaoui",     "Wafa",   "0611111103", "Fes",        d("08/11/1975"));
        Femme f4  = new Femme("Alami",      "Karima", "0611111104", "Casablanca", d("14/02/1962"));
        Femme f5  = new Femme("Tazi",       "Nadia",  "0611111105", "Tanger",     d("30/09/1980"));
        Femme f6  = new Femme("Fassi",      "Samira", "0611111106", "Meknes",     d("17/01/1985"));
        Femme f7  = new Femme("Berrada",    "Hind",   "0611111107", "Rabat",      d("05/06/1978"));
        Femme f8  = new Femme("Idrissi",    "Laila",  "0611111108", "Agadir",     d("21/12/1990"));
        Femme f9  = new Femme("Chraibi",    "Meriem", "0611111109", "Oujda",      d("02/04/1972"));
        Femme f10 = new Femme("Benjelloun", "Zineb",  "0611111110", "Casablanca", d("19/08/1968"));
        for (Femme f : new Femme[]{f1, f2, f3, f4, f5, f6, f7, f8, f9, f10}) {
            femmeService.create(f);
        }

        // ---------- 2. Créer 5 hommes ----------
        Homme h1 = new Homme("Said",   "Safi",   "0622222201", "Casablanca", d("10/05/1960"));
        Homme h2 = new Homme("Alaoui", "Omar",   "0622222202", "Rabat",      d("03/02/1958"));
        Homme h3 = new Homme("Kadiri", "Youssef","0622222203", "Fes",        d("27/10/1970"));
        Homme h4 = new Homme("Lahlou", "Hassan", "0622222204", "Tanger",     d("15/04/1965"));
        Homme h5 = new Homme("Benani", "Rachid", "0622222205", "Agadir",     d("09/09/1975"));
        for (Homme h : new Homme[]{h1, h2, h3, h4, h5}) {
            hommeService.create(h);
        }

        // ---------- 3. Créer les mariages ----------
        // Safi Said : 1 mariage échoué + 3 en cours (comme dans l'énoncé)
        mariageService.create(new Mariage(h1, f4, d("03/09/1989"), d("03/09/1990"), 0));
        mariageService.create(new Mariage(h1, f1, d("03/09/1990"), null, 4));
        mariageService.create(new Mariage(h1, f2, d("03/09/1995"), null, 2));
        mariageService.create(new Mariage(h1, f3, d("04/11/2000"), null, 3));

        // Karima se remarie avec Omar (donc mariée 2 fois)
        mariageService.create(new Mariage(h2, f4, d("01/01/1992"), null, 1));
        mariageService.create(new Mariage(h2, f5, d("05/05/1990"), d("05/05/1998"), 2));

        mariageService.create(new Mariage(h3, f6, d("15/06/2005"), null, 1));

        // Hind : mariée avec Hassan puis avec Rachid (donc mariée 2 fois)
        mariageService.create(new Mariage(h4, f7, d("10/10/2000"), d("10/10/2008"), 2));
        mariageService.create(new Mariage(h5, f7, d("20/03/2010"), null, 1));
        mariageService.create(new Mariage(h5, f10, d("11/11/2015"), null, 0));

        // ---------- 4. Liste des femmes ----------
        System.out.println("\n===== Liste des femmes =====");
        femmeService.findAll().forEach(System.out::println);

        // ---------- 5. Femme la plus âgée ----------
        System.out.println("\n===== Femme la plus âgée =====");
        System.out.println(femmeService.getFemmePlusAgee());

        // ---------- 6. Épouses d'un homme entre deux dates ----------
        System.out.println("\n===== Épouses de Safi Said entre 01/01/1990 et 31/12/1999 =====");
        hommeService.afficherEpousesEntreDates(h1.getId(), d("01/01/1990"), d("31/12/1999"));

        // ---------- 7. Nombre d'enfants d'une femme entre deux dates ----------
        System.out.println("\n===== Nombre d'enfants de Salima Rami entre 01/01/1990 et 31/12/2000 =====");
        System.out.println(femmeService.getNbEnfantsEntreDates(
                f1.getId(), d("01/01/1990"), d("31/12/2000")));

        // ---------- 8. Femmes mariées deux fois ou plus ----------
        System.out.println("\n===== Femmes mariées au moins deux fois =====");
        femmeService.getFemmesMarieesAuMoinsDeuxFois().forEach(System.out::println);

        // ---------- 9. Hommes mariés à quatre femmes entre deux dates (Criteria) ----------
        System.out.println("\n===== Hommes mariés à 4 femmes entre 01/01/1980 et 31/12/2020 =====");
        System.out.println(hommeService.compterHommesMariesAQuatreFemmes(
                d("01/01/1980"), d("31/12/2020")));

        // ---------- 10. Mariages d'un homme avec détails ----------
        System.out.println("\n===== Mariages de Safi Said =====");
        hommeService.afficherMariages(h1.getId());

        HibernateUtil.shutdown();
    }
}