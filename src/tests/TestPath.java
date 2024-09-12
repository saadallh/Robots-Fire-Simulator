package tests;
import java.awt.Color;
import java.io.FileNotFoundException;
import java.util.zip.DataFormatException;
import java.util.Iterator;

import gui.GUISimulator;

import simulation.*;
import io.*;
import donnees.*;
import robots.*;
import path.*;

public class TestPath {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {

			String fichierName = "cartes/spiralOfMadness-50x50.map";
			DonneesSimulation donneesInit = NewLecteurDonnees.lire(fichierName);
			Carte carteToDraw = donneesInit.getCarte();
			
			GUISimulator gui = new GUISimulator(500, 500, Color.WHITE);
			Simulateur simulateur = new Simulateur(gui, donneesInit, fichierName);
			
			Robot[] robots = donneesInit.getrobot();
			Robot robotsTodeplace = robots[2];
			Case source = robotsTodeplace.getPosition();
			Case destination = carteToDraw.getCase(0, 49);
			Path path = new Path(robotsTodeplace, carteToDraw, source, destination);
			Iterator<Direction> it2 = path.getPath().iterator();
			while(it2.hasNext()) {
				robotsTodeplace.deplacerEffectivement(it2.next(), carteToDraw, simulateur);
			}

		}catch (FileNotFoundException e) {
            System.out.println("fichier " + args[0] + " inconnu ou illisible");
        } catch (DataFormatException e) {
            System.out.println("\n\t**format du fichier " + args[0] + " invalide: " + e.getMessage());
        
        }
	}
}
