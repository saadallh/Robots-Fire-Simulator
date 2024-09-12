package io;
import donnees.*;
import robots.*;

import java.io.*;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.zip.DataFormatException;
import java.util.ArrayList;

/**
 * CLasse newLecteurDonnees, qui lit toutes les données ( carte incendies, robots..) et crée les objets associés utiles
 * pour la simulation
 */
public class NewLecteurDonnees {
	public static DonneesSimulation lire(String fichierDonnees)
	        throws FileNotFoundException, DataFormatException {
	        System.out.println("\n == Lecture du fichier" + fichierDonnees);
	        NewLecteurDonnees lecteur = new NewLecteurDonnees(fichierDonnees);
	        Carte carte = lecteur.lireCarte();
	        Incendie[] incendies = lecteur.lireIncendies(carte);
	        Robot[] robots = lecteur.lireRobots(carte);
	        scanner.close();
	        System.out.println("\n == Lecture terminee");
	        DonneesSimulation donnees = new DonneesSimulation(carte, incendies, robots);
	        return donnees;
	    }
	




    // Tout le reste de la classe est prive!

    private static Scanner scanner;

    /**
     * Constructeur prive; impossible d'instancier la classe depuis l'exterieur
     * @param fichierDonnees nom du fichier a lire
     */
    private NewLecteurDonnees(String fichierDonnees) 
    		throws FileNotFoundException {
        scanner = new Scanner(new File(fichierDonnees));
        scanner.useLocale(Locale.US);
    }

    /**
     * Lit et affiche les donnees de la carte.
     * @throws ExceptionFormatDonnees
     */
    private Carte lireCarte() throws DataFormatException {
        ignorerCommentaires();
        try {
            int nbLignes = scanner.nextInt();
            int nbColonnes = scanner.nextInt();
            int tailleCases = scanner.nextInt();	// en m
            Carte carte = new Carte(tailleCases, nbLignes, nbColonnes);
            System.out.println("Carte " + nbLignes + "x" + nbColonnes
                    + "; taille des cases = " + tailleCases);
            ArrayList<Case> sourcesEau = carte.getSourcesEau();
            for (int lig = 0; lig < nbLignes; lig++) {
                for (int col = 0; col < nbColonnes; col++) {
                	Case caseCourante = lireCase(lig, col);
                    carte.setCase(lig, col, caseCourante);
                    if (caseCourante.getNature().equals(NatureTerrain.EAU)) {
                    	sourcesEau.add(caseCourante);
                    }
                }
            }

            return carte;

        } catch (NoSuchElementException e) {
            throw new DataFormatException("Format invalide. "
                    + "Attendu: nbLignes nbColonnes tailleCases");
        }
        // une ExceptionFormat levee depuis lireCase est remontee telle quelle
    }




    /**
     * Lit et affiche les donnees d'une case.
     */
    private Case lireCase(int lig, int col) throws DataFormatException {

    	ignorerCommentaires();
        Case caselue = new Case(lig, col);
        System.out.print("Case (" + lig + "," + col + "): ");
        String chaineNature = new String();
        //		NatureTerrain nature;

        try {
            chaineNature = scanner.next();
            caselue.setNature(NatureTerrain.valueOf(chaineNature));

            verifieLigneTerminee();

            System.out.print("nature = " + chaineNature);
            

        } catch (NoSuchElementException e) {
            throw new DataFormatException("format de case invalide. "
                    + "Attendu: nature altitude [valeur_specifique]");
        }

        System.out.println();
        return caselue;
    }


    /**
     * Lit et affiche les donnees des incendies.
     * @param carte
     */
    private Incendie[] lireIncendies(Carte carte) throws DataFormatException {
        ignorerCommentaires();
        try {
            int nbIncendies = scanner.nextInt();
            Incendie[] incendies = new Incendie[nbIncendies];
            System.out.println("Nb d'incendies = " + nbIncendies);
            for (int i = 0; i < nbIncendies; i++) {
                incendies[i] = lireIncendie(i, carte);
            }
            return incendies;

        } catch (NoSuchElementException e) {
            throw new DataFormatException("Format invalide. "
                    + "Attendu: nbIncendies");
        }
    }


    /**
     * Lit et affiche les donnees du i-eme incendie.
     * @param i
     * @param carte
     */
    private Incendie lireIncendie(int i, Carte carte) throws DataFormatException {
        ignorerCommentaires();
        System.out.print("Incendie " + i + ": ");

        try {
            int lig = scanner.nextInt();
            int col = scanner.nextInt();
            int intensite = scanner.nextInt();
            if (intensite <= 0) {
                throw new DataFormatException("incendie " + i
                        + "nb litres pour eteindre doit etre > 0");
            }
            Incendie incendie = new Incendie(carte.getCase(lig, col),intensite);
            verifieLigneTerminee();

            System.out.println("position = (" + lig + "," + col
                    + ");\t intensite = " + intensite);
            return incendie;
        } catch (NoSuchElementException e) {
            throw new DataFormatException("format d'incendie invalide. "
                    + "Attendu: ligne colonne intensite");
        }
    }


    /**
     * Lit et affiche les donnees des robots.
     * @param carte
     */
    private Robot[] lireRobots(Carte carte) throws DataFormatException {
        ignorerCommentaires();
        try {
            int nbRobots = scanner.nextInt();
            Robot[] robots = new Robot[nbRobots];
            System.out.println("Nb de robots = " + nbRobots);
            for (int i = 0; i < nbRobots; i++) {
                robots[i] = lireRobot(i, carte);
            }
            return robots;

        } catch (NoSuchElementException e) {
            throw new DataFormatException("Format invalide. "
                    + "Attendu: nbRobots");
        }
    }


    /**
     * Lit et affiche les donnees du i-eme robot.
     * @param i
     * @param carte
     */
    private Robot lireRobot(int i, Carte carte) throws DataFormatException {
        ignorerCommentaires();
        System.out.print("Robot " + i + ": ");

        try {
            int lig = scanner.nextInt();
            int col = scanner.nextInt();
            System.out.print("position = (" + lig + "," + col + ");");
            String type = scanner.next();

            System.out.print("\t type = " + type);
            

            // lecture eventuelle d'une vitesse du robot (entier)
            System.out.print("; \t vitesse = ");
            String s = scanner.findInLine("(\\d+)");	// 1 or more digit(s) ?
            // pour lire un flottant:    ("(\\d+(\\.\\d+)?)");
            int new_vitesse = 0;
            if (s == null) {
                System.out.print("valeur par defaut");
            } else {
                int vitesse = Integer.parseInt(s);
                new_vitesse = vitesse;
                System.out.print(vitesse);
            }
            Robot robot;
            
            if (type.equals("DRONE")) {
            	if (new_vitesse != 0) {
            		robot = new Drone(new_vitesse,carte.getCase(lig, col), carte, 10000);
            	}
            	else {
            		robot = new Drone(carte.getCase(lig, col), carte, 10000);
            	}
            }
            else if (type.equals("ROUES")) {
            	if (new_vitesse != 0) {
            		robot = new Rroue(new_vitesse, carte.getCase(lig, col), carte, 5000);
            	}
            	else {
            		robot = new Rroue(carte.getCase(lig, col), carte, 5000);
            	}
            }
            else if (type.equals("PATTES")) {
            	if (new_vitesse != 0) {
            		robot = new Rpatte(new_vitesse, carte.getCase(lig, col), carte);
            	}
            	else {
            		robot = new Rpatte(carte.getCase(lig, col), carte);
            	}
            }
            else{
            	if (new_vitesse != 0) {
            		robot = new Rchenille(new_vitesse,carte.getCase(lig, col), carte, 2000);
            	}
            	else {
            		robot = new Rchenille(carte.getCase(lig, col), carte, 2000);
            	}
            }
            
            verifieLigneTerminee();

            System.out.println();
            return robot;
        } catch (NoSuchElementException e) {
            throw new DataFormatException("format de robot invalide. "
                    + "Attendu: ligne colonne type [valeur_specifique]");
        }
    }




    /** Ignore toute (fin de) ligne commencant par '#' */
    private void ignorerCommentaires() {
        while(scanner.hasNext("#.*")) {
            scanner.nextLine();
        }
    }

    /**
     * Verifie qu'il n'y a plus rien a lire sur cette ligne (int ou float).
     * @throws ExceptionFormatDonnees
     */
    private void verifieLigneTerminee() throws DataFormatException {
        if (scanner.findInLine("(\\d+)") != null) {
            throw new DataFormatException("format invalide, donnees en trop.");
        }
    }
}
