# TP7 : JPA / Hibernate (gestion de projets et état civil)

## Description
Projet Maven (JPA, Hibernate 5.6, base MySQL) avec deux exercices : une application de suivi de projets et une application de gestion de l'état civil.

| Exercice | Entités | Tables créées |
|---|---|---|
| Gestion de projets | Employe, Projet, Tache, EmployeTache | 4 tables, `EmployeTache` est la classe d'association (dates réelles) |
| État civil (héritage JOINED) | Personne, Homme, Femme, Mariage | `personne` + `homme` + `femme` + `mariage` (classe d'association avec `dateDebut`, `dateFin`, `nbrEnfant`) |

## Fonctionnalités
- **Gestion de projets** : tâches réalisées par un employé, projets gérés, tâches planifiées et réalisées d'un projet, tâches de prix supérieur à 1000 DH (requête nommée), tâches réalisées entre deux dates.
- **État civil** : épouses d'un homme entre deux dates, nombre d'enfants d'une femme (requête native nommée), femmes mariées au moins deux fois (requête nommée), hommes mariés à quatre femmes (API Criteria), mariages d'un homme avec détails.

## Configuration
1. Démarrer MySQL.
2. Copier `src/main/resources/application.properties.example` vers `application.properties`.
3. Renseigner l'utilisateur et le mot de passe MySQL.
4. La base et les tables sont créées automatiquement (`hibernate.hbm2ddl.auto=update`).

## Exécution
Lancer la classe `ma.projet.test.Test` (ou `ma.projet.App`).

## Preuves d'exécution

### 1. Création des tables
![Création des tables](./captures/im1.png)

### 2. Insertion des données
![Insertions](./captures/im2.png)

### 3. Résultats des requêtes
![Requêtes](./captures/im3.png)

### 4. Fin d'exécution sans erreur
![Fin](./captures/im4.png)
