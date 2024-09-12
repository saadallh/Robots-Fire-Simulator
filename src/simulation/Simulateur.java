package simulation;

import java.util.*;
import java.util.zip.DataFormatException;

import java.io.FileNotFoundException;
import donnees.*;
import evenements.*;
import chefpompier.*;
import robots.*;
import io.*;

import gui.ImageElement;
import gui.Simulable;
import gui.GUISimulator;

/**Classe qui implement simulable
 * Le but c'est de visualiser les donnees recu par NewLecteurDonnees
 * Input: donnees = (carte, incendies, robots)
 * Output: visualisation de tout les elements de la simulation
 * 
 */
public class Simulateur implements Simulable {
	
	/** Interface graphique */
	private GUISimulator gui;
	
	/** Donnees a visualiser */
	private DonneesSimulation donnees;
	
	/** entier qui permet de suivre l'execution des evenements */
	private long dateSimulation;

	/** Liste evenement*/
	SortedMap<Long, LinkedList<Evenement>> evenements; // Evenements  {date: event1->event2,...}
	
	
	/**ChefPompier**/
	ChefPompier chef;
	
	
	/**Fichier de la carte**/
	String cheminMap;
	
	
	/**
	 * Constructeur pour les tests sans chef pompier
	 * @param gui
	 * @param donneesInit
	 */
	public Simulateur(GUISimulator gui, DonneesSimulation donneesInit, String cheminMap) {
		this.gui = gui;
		this.donnees = donneesInit;
		this.dateSimulation = 1;
		
		//La structure ou on sauvegarde les evenements
		this.evenements = new TreeMap<Long, LinkedList<Evenement>> ();
		this.evenements.put((long)1, new LinkedList<Evenement>());
		//
		this.cheminMap = cheminMap;
		
		gui.setSimulable(this);
		draw();
	}
	
	/**
	 * Constructeur pour la simulation génerale
	 * @param gui
	 * @param donneesInit
	 * @param chef
	 * @param cheminMap
	 */
	public Simulateur(GUISimulator gui, DonneesSimulation donneesInit, ChefPompier chef, String cheminMap) {
		this.gui = gui;
		this.donnees = donneesInit;
		this.dateSimulation = 1; 
		this.cheminMap = cheminMap;
		
		//La structure ou on sauvegarde les evenements
		this.evenements = new TreeMap<Long, LinkedList<Evenement>> ();
		this.evenements.put((long)1, new LinkedList<Evenement>());
		//
		this.chef = chef;
		
		gui.setSimulable(this);
		draw();
		
	}

	
	public long getDateSimulation() {
		return dateSimulation;
	}

	public void  incrementeDate() {
		this.dateSimulation += 1;
	}
	
	/**
	 * Partie planification, ajouter les evenements dans leurs dates correspondantes
	 * @param e
	 */
	public void ajouteEvenement(Evenement e) {
		long dateEvent = e.getDate();
		long dateCourante = this.dateSimulation;
		if (evenements.containsKey(dateEvent)) {
			evenements.get(dateEvent).add(e);
		}
		else {
			/**Si l'evenement doit se passer apres longtemps, on ajoute des dates intermediaires*/
			for (long i = dateCourante +1; i < dateEvent+1; i++) {
				//Create it if it doesn't exist
				if (!(evenements.containsKey(i))) {
					evenements.put(i, new LinkedList<Evenement>());
				}
			}
			/**Ajouter l'evenement dans sa date correspondante*/
			evenements.get(dateEvent).add(e);
		}
	}
	
	public boolean simulationTerminee() {
		/**La simulation termine si la SortedMap des evenements est vide ou si la datecourante est la derniere*/
		return (this.dateSimulation == evenements.lastKey() + 1 || evenements.isEmpty());
	}
	

	/**
	 * Dessiner les donnees
	 * */
	private void draw() {
		
		/**
		 * Variables utiles
		 * */
		Carte carteToDraw = donnees.getCarte();
		Incendie[] incendieTableau = donnees.getIncendie();
		Robot[]	robotTableau = donnees.getrobot();
		
		int nbLig = carteToDraw.getNbLignes();
		int nbCol = carteToDraw.getNBColonnes();
		
		
		//Anchor = centre pour rectangles et text, topleft pour image
		
		
        int xMax = gui.getWidth();
        xMax -= (int)xMax*0.02;  // pour eliminer les cases du toplevel du gui
        int yMax = gui.getHeight();
        yMax -= (int)yMax*0.15; // pour eliminer les cases du toplevel du gui
        
		int tailleCases_length = (yMax)/nbLig;
		int tailleCases_width = (xMax)/nbCol;
			
		
		/** Partie du dessin */
		
		/**
		 * Boucler sur les cases, et les dessiner selon la nature du terrain
		 * */
		for (int y = 0; y < nbLig; y++) {
			for (int x = 0; x < nbCol; x++) {
				Case caseCourante = carteToDraw.getCase(y, x);
		        gui.addGraphicalElement(new ImageElement(x*tailleCases_width, y*tailleCases_length, "./images/" + caseCourante.getNature() +".png", tailleCases_width, tailleCases_length, null));
			}
		}
		
		/**
		 * Faire l'incendie
		 */
		for (int i = 0; i < incendieTableau.length; i++) {
			Case positionCase = incendieTableau[i].getPosition();
			int x = positionCase.getColonne();
			int y = positionCase.getLigne();
			if (incendieTableau[i].getIntensite() != 0) {
				gui.addGraphicalElement(new ImageElement(x*tailleCases_width, y*tailleCases_length, "./images/FIRE.gif", tailleCases_width, tailleCases_length, null));
				//incendieTableau[i].setAffecte(false);
			}
		}
		
		/**
		 * Deployer les robots
		 */
		for (int i = 0; i < robotTableau.length; i++) {
			Case positionCase = robotTableau[i].getPosition();
			int x = positionCase.getColonne();
			int y = positionCase.getLigne();
			gui.addGraphicalElement(new ImageElement(x*tailleCases_width, y*tailleCases_length,  "./images/"+ robotTableau[i].returnType() + (int)robotTableau[i].waterBar() +".png", tailleCases_width, tailleCases_length, null));
		}
	}
	
	
	
	/**
	 * Dessiner la fin de la simulation
	 */
	private void drawEnd() {

        int xMax = gui.getWidth();
        xMax -= xMax % 10 + 50;  //50 est la taille de la partie non utile de la fenetre
        int yMax = gui.getHeight();
        yMax -= yMax % 10 + 80; // 80 est la taille de la partie non utile de la fenetre
        
        int imageX = (int)(xMax/2);
        int imageY = (int)(yMax/2 - 0.5*yMax/2);
        int imageWidth = (int)(xMax*0.5);
        int imageHeight = (int)(yMax*0.5);
        
        int imageX2 = (int)(xMax/2- 0.5*xMax);
        int imageY2 = (int)(yMax/2 - 0.5*yMax/2);
        int imageWidth2 = (int)(xMax*0.5);
        int imageHeight2 = (int)(yMax*0.5);
		
		gui.addGraphicalElement(new ImageElement(imageX, imageY, "./images/DANCEROBOT.gif", imageWidth, imageHeight, null));
		gui.addGraphicalElement(new ImageElement(imageX2, imageY2, "./images/GG.gif", imageWidth2, imageHeight2, null));
	}

	
	@Override
	public void next() {
		Incendie[] incendieTab = donnees.getIncendie();
		Robot[] robotTab = donnees.getrobot();
		if (!(simulationTerminee())) {		
			/**Si la simulation utilise un chef pompier on met a jour la strategie*/
			if (this.chef != null) {
			chef.strategie(this, robotTab, incendieTab);
			}
			System.out.println("Date courante :" + this.dateSimulation);
			
			/**La liste des evenements a executer dans la date courante*/
			LinkedList<Evenement> currListEvents = evenements.get(this.dateSimulation);	
			if (currListEvents != null && !(currListEvents.isEmpty())) {
				for (Evenement e : currListEvents) {
					e.execute();
				}
				gui.reset();
				draw();
				incrementeDate();
			}
			else {
				incrementeDate();
				
				System.out.println("*Il n y'a pas d'evenements a faire dans cette date, on incremente la date*");
			}
		}
		else {
			drawEnd();
			System.out.println("***La simulation est terminée***");
		}
	
	}


	public GUISimulator getGui() {
		return gui;
	}

	public void setGui(GUISimulator gui) {
		this.gui = gui;
	}

	public DonneesSimulation getDonnees() {
		return donnees;
	}

	public void setDonnees(DonneesSimulation donnees) {
		this.donnees = donnees;
	}

	public SortedMap<Long, LinkedList<Evenement>> getEvenements() {
		return evenements;
	}

	public void setEvenements(SortedMap<Long, LinkedList<Evenement>> evenements) {
		this.evenements = evenements;
	}


	public ChefPompier getChef() {
		return chef;
	}

	public void setChef(ChefPompierEvolue chef) {
		this.chef = chef;
	}

	public void setDateSimulation(long dateSimulation) {
		this.dateSimulation = dateSimulation;
	}

	@Override
	public void restart() {
		resetData();
		gui.reset();
		draw();
		
	}
	/**
	 * Méthode resetData, relit les données à nouveau pour les afficher après avoir cliqué sur "Début"
	 */
	public void resetData() {
		if(this.dateSimulation > 1)
		{
		try {
		this.donnees = NewLecteurDonnees.lire(this.cheminMap);
		}
		catch (FileNotFoundException e) {
            System.out.println("fichier inconnu ou illisible");
        } catch (DataFormatException e) {
            System.out.println("\n\t**format du fichier invalide: " + e.getMessage());
        
        }
		
		this.dateSimulation = 1; 
		
		/**Associe le chef si la simulation l'utilise
		 */
		if(this.chef != null) {
			this.evenements = new TreeMap<Long, LinkedList<Evenement>> ();
			this.evenements.put((long)1, new LinkedList<Evenement>());
			this.chef.setDonnees(this.donnees);
		}
		gui.setSimulable(this);
		}
	}

	
}

