package tests;

import simulation.*;
import io.*;
import donnees.*;


import java.awt.Color;
import java.io.FileNotFoundException;
import java.util.zip.DataFormatException;

import gui.GUISimulator;

public class TestCarte {

	public static void main(String[] args) {
		try {
			
			String fichierName = "cartes/mushroomOfHell-20x20.map";
			DonneesSimulation donneesInit = NewLecteurDonnees.lire(fichierName);

			GUISimulator gui = new GUISimulator(500, 500, Color.WHITE);

			Simulateur simulateur = new Simulateur(gui, donneesInit, fichierName);

		}catch (FileNotFoundException e) {
            System.out.println("fichier " + args[0] + " inconnu ou illisible");
        } catch (DataFormatException e) {
            System.out.println("\n\t**format du fichier " + args[0] + " invalide: " + e.getMessage());
        }
	}

}
