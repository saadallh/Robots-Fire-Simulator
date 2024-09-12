package tests;
import java.awt.Color;
import java.io.FileNotFoundException;
import java.util.zip.DataFormatException;


import simulation.*;
import io.*;
import donnees.*;
import robots.*;
import evenements.*;

import gui.GUISimulator;

public class TestDeplacement {

	public static void main(String[] args) {
		try {
			
			String fichierName = "cartes/carteSujet.map";
			DonneesSimulation donneesInit = NewLecteurDonnees.lire(fichierName);
			Carte carteToDraw = donneesInit.getCarte();
			
			GUISimulator gui = new GUISimulator(500, 500, Color.WHITE);
			Simulateur simulateur = new Simulateur(gui, donneesInit, fichierName);
			
			Robot[] robots = simulateur.getDonnees().getrobot();
			Robot robotTodeplace = robots[1];
			
			robotTodeplace.deplacerEffectivement(Direction.NORD, carteToDraw, simulateur);
			robotTodeplace.deplacerEffectivement(Direction.OUEST, carteToDraw, simulateur);
			robotTodeplace.deplacerEffectivement(Direction.OUEST, carteToDraw, simulateur);
			robotTodeplace.deplacerEffectivement(Direction.OUEST, carteToDraw, simulateur);
			robotTodeplace.deplacerEffectivement(Direction.EST, carteToDraw, simulateur);
			robotTodeplace.deplacerEffectivement(Direction.EST, carteToDraw, simulateur);
			robotTodeplace.deplacerEffectivement(Direction.EST, carteToDraw, simulateur);
			robotTodeplace.deplacerEffectivement(Direction.EST, carteToDraw, simulateur);




		}catch (FileNotFoundException e) {
            System.out.println("fichier " + args[0] + " inconnu ou illisible");
        } catch (DataFormatException e) {
            System.out.println("\n\t**format du fichier " + args[0] + " invalide: " + e.getMessage());
        
        }
		
	}
}
