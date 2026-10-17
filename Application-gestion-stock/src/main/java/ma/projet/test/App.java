package ma.projet.test;

import ma.projet.classes.*;
import ma.projet.service.*;

import java.text.SimpleDateFormat;

public class App {
    public static void main(String[] args) throws Exception {        CategorieService cs = new CategorieService();
        ProduitService ps = new ProduitService();
        CommandeService cms = new CommandeService();
        LigneCommandeService ls = new LigneCommandeService();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        // --- Données de test ---
        Categorie c1 = new Categorie("C1", "Ordinateurs");
        Categorie c2 = new Categorie("C2", "Accessoires");
        cs.create(c1);
        cs.create(c2);

        Produit p1 = new Produit("ES12", 120, c1);
        Produit p2 = new Produit("ZR85", 100, c1);
        Produit p3 = new Produit("EE85", 200, c2);
        ps.create(p1);
        ps.create(p2);
        ps.create(p3);

        Commande cmd = new Commande(sdf.parse("14/03/2013"));
        cms.create(cmd);

        ls.create(new LigneCommandeProduit(p1, cmd, 7));
        ls.create(new LigneCommandeProduit(p2, cmd, 14));
        ls.create(new LigneCommandeProduit(p3, cmd, 5));

        // --- Tests des fonctionnalités ---
        System.out.println("\n===== 1) Produits par catégorie =====");
        ps.afficherProduitsParCategorie(c1.getId());

        System.out.println("\n===== 2) Produits commandés entre deux dates =====");
        ps.afficherProduitsEntreDates(sdf.parse("01/01/2013"), sdf.parse("31/12/2013"));

        System.out.println("\n===== 3) Produits d'une commande =====");
        ps.afficherProduitsParCommande(cmd.getId());

        System.out.println("\n===== 4) Produits avec prix > 100 DH (requête nommée) =====");
        ps.afficherProduitsPrixSuperieur(100);
    }
}