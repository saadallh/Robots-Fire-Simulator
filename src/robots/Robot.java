package robots;
import java.util.*;
import donnees.*;
import simulation.*;
import evenements.*;
import path.*;

/**
 * Classe Robot, une classe abstraite des Robots, qui vont éteindre les incendies
 */
public abstract class Robot {
	protected double vitesse;
	protected Case position;
	protected Carte carte;
	
	protected long dateArrive;// = 0 si le robot ne bouge pas sinon = le nombre d'etapes pour qu'il arrive
	
	protected Case positionCourante;
	
		
	/**
	 * Constructeur public,
	 * @param position case dans laquelle le robot se trouve
	 */
	public Robot(Case position, Carte carte) {
		this.position = position;
		this.carte = carte;
		this.positionCourante = position;
		this.dateArrive = (long) 0;
		}
	
	public long getDateArrive() {
		return this.dateArrive;
	}
	
	public Carte getCarte() {
		return this.carte;
	}
	
	
	/**
	 * Constructeur public
	 * @param vitesse vitesse du robot
	 * @param position cose dans laquelle le robot se trouve
	 */
	public Robot(double vitesse, Case position, Carte carte) {
		this.vitesse = vitesse;
		this.position = position;
		this.carte = carte;
		this.dateArrive = (long) 0;
		this.positionCourante = position;
	}
	public String toString() {
		return " le robot a se déplace avec une vitesse de " + vitesse + " km/h et est dans la case " + this.position.toString();
	}
	public Case getPosition() {
		return position;
	}
	public Case getPositionCourante() {
		return positionCourante;
	}
	public  void setPosition(Case new_case) {
		this.position = new_case;
	}
	
	
	
	/**********Les methodes pour le deplacement************/
	/**
	 * Méthode qui nous permet de connaitre le temps de deplacement 
	 * @param caseArrivee caseArrivee
	 * @param carte map 
	 * @return le temps de deplacement : double
	 */
	public double tempsDeplacement(Case caseArrivee, Carte carte) {
		// supposant caseArrivee est une case voisine 
		// si on ajoute l algo de Path on peut effectuer ce calcul sur n'importe quelle case
		// on verifie deja si la case est accessible
		double vitesse1 = this.getVitesse(caseArrivee.getNature());
		double vitesse2 = this.getVitesse(positionCourante.getNature());
		double tailleCase = carte.getTailleCases();
		return (tailleCase)/(((vitesse1+vitesse2)*1000)/(2*3600));
	}
	
	public long max(long a, long b) {
		if (a < b) {
			return b;
		}
		return a;
	}
	

	/**
	 * Méthode qui nous permet de ajouter l evenement effectif de déplacement dans la liste des
	 * evenements qui sera executé directement dans une date calculée en fonction des données du robot
	 * @param dir Direction (NORD, SUD, OUEST, EST)
	 * @param carte map 
	 * @param simulateur simulateur du jeu
	 */
	public void deplacerEffectivement(Direction dir, Carte carte, Simulateur simulateur) {
		Case caseArrivee = carte.getVoisin(positionCourante, dir);
		if (this.hasAccessto(caseArrivee.getNature())) {
			double temps = tempsDeplacement(caseArrivee, carte);
			System.out.println(temps);
			long dateToAdd = max((long) 1,(long) (temps) / 6) ; //temps d'attente pour le deplacement
			this.dateArrive = this.dateArrive + dateToAdd;
			simulateur.ajouteEvenement(new EventRobotDeplace(this.dateArrive, dir, this, caseArrivee.getNature()));	
			this.positionCourante = caseArrivee;
		}
	}

	
	/**********Les methodes pour les incendies************/
	
	/**
	 * Fonction pour eteindre les incendies dans une date précise calculée en fonction des parametres du robot
	 * On suppose que le robot est deja sur une case avec incendie
	 * @param incendieTableau
	 * @param dateCourante
	 * @param simulateur
	 */
	public void eteindreIncendie(Simulateur simulateur, Incendie incendie) {
		double reservoirCourant = this.getReservoirCourant();
		double litresAverser = incendie.getIntensiteCourante()- reservoirCourant;
		long dateToAdd = 1;
		if ( litresAverser > 0) {
			dateToAdd = max((long)1,this.tempsEteinte(litresAverser)/30);
		}
		else {
			dateToAdd = max((long)1,this.tempsEteinte(incendie.getIntensiteCourante())/30);
		}
		System.out.println("Robot is shuting down the fire, time_needed ---->" + dateToAdd + " steps");
		this.dateArrive =this.dateArrive + dateToAdd;
		simulateur.ajouteEvenement(new EventRobotFire(this.dateArrive, this, simulateur.getDonnees().getIncendie()));
		try {
		if (litresAverser >= 0) {
			incendie.setIntensiteCourante(incendie.getIntensiteCourante() - reservoirCourant);
			System.out.println("Il reste " + incendie.getIntensiteCourante() + " pour l'éteindre");
			programmeEvents(closestWaterDestination(), simulateur);
			remplirReservoir(simulateur);
			this.remplirEauCourant();
			
		}
		else {
			this.deverserEauCourant((int) litresAverser);
			System.out.println("c bon");
			incendie.setIntensiteCourante(0);
		}
		}catch(NullPointerException e) {
			System.out.println("La case n'a pas d'incendie");
		}		
	}
	
	
	abstract public long tempsEteinte(double litresAverser);
	
	
	/*********Les methodes pour le remplissage d'eau*********/
	/**
	 * Fonction pour remplir le reservoir du robot dans une date précise calculée en fonction des parametres du robot
	 * @param dateCourante
	 * @param simulateur
	 */
	public void remplirReservoir(Simulateur simulateur) {
		long dateToAdd = max((long)1, this.tempsCharge()/50); //temps d'attente pour le remplissage du reservoir on divise par 20 pour la rapidité
		System.out.println("Le robot est en train de remplir son reservoir, temps necessaire ----->" + dateToAdd + "steps");
		this.dateArrive = this.dateArrive+ dateToAdd;
		simulateur.ajouteEvenement(new EventRobotCharge(this.dateArrive, this));
	}
	
	
	abstract public long tempsCharge();
	
	
	
	/*********Les methodes pour implementer les strategies*********/
	
	/**
	 * Fonction pour que le robot calcule son Path
	 * On n'appelle la fonction que si le robot peut s'y rendre a destination
	 * @param destination
	 * @return Path (LinkedList)
	 */
	public Path calculePath(Case destination){
		Path PathRobot = new Path(this, carte, this.positionCourante, destination);
		return PathRobot;
	}
	

	
	
	/**
	 * Programmen le deplacement du robot vers la destination ou il existe du feu
	 * @param destination  (La destination est la fin du Path, on suppose que ca contient un incendie)
	 * @param simulateur
	 */
	public void programmeEvents(Case destination, Simulateur simulateur) {
		if (!(destination.equals(this.positionCourante))) {
			Path Path = calculePath(destination);
			if (Path.getPath() != null) {
				Iterator<Direction> it = Path.getPath().iterator();
				while(it.hasNext()) {
					this.deplacerEffectivement(it.next(), carte, simulateur);
				}
				//On suppose ici qu'on arrive a une incendie
				}
		}
	}
	
	/**
	 * Méthode heuristicEau, nous renvoie la distance entre le robot et la case caseCourante
	 * @param caseCourante 
	 * @return heuristic : double
	 */
	public double heuristicEau(Case caseCourante) {
		double heuristic = Math.abs(caseCourante.getColonne() - positionCourante.getColonne()) + 
				Math.abs(caseCourante.getLigne() - positionCourante.getLigne());
		return heuristic;
	}
	
	/**
	 * Méthode closestWaterSource, qui renvoie la case de nature EAU la plus proche au robot
	 * @return caseProche
	 */
	public Case closestWaterSource() {
		ArrayList<Case> sourcesEau = carte.getSourcesEau();
		Iterator<Case> it = sourcesEau.iterator();
		Case caseProche = null;
		Case caseTemp = null;
		Double min = Double.POSITIVE_INFINITY;
		while(it.hasNext()) {
			caseTemp = it.next();
			if (heuristicEau(caseTemp) < min){
				caseProche = caseTemp;
				min = heuristicEau(caseTemp);
			}
		}
		return caseProche;
	}
	
	/**
	 * Méthode closestWaterDestination, qui renvoie la case voisine de la source d'eau la plus proche au robot.
	 * @return nouvelleDestination
	 */
	public Case closestWaterDestination() {
		Case destinationTemp = closestWaterSource();
		double min = Double.POSITIVE_INFINITY;
		Case nouvelleDestination = null;
		for(Direction d : Direction.values()) {
			if (carte.voisinExiste(destinationTemp, d) && hasAccessto(carte.getVoisin(destinationTemp, d).getNature())) {
				if(heuristicEau(carte.getVoisin(destinationTemp, d)) < min) {
					nouvelleDestination = carte.getVoisin(destinationTemp,  d);
					min = heuristicEau(nouvelleDestination);
				}
			}
		}
		return nouvelleDestination;

	}
	/**
	 * Méthode, qui retourne le type du robot
	 * @return type
	 */
	abstract public String returnType();
	/**
	 * Méthode waterBar, qui nous donne une idée sur le pourcentage de l'eau restante dans le reservoir.
	 * @return waterBar
	 */
	abstract public double waterBar();

	
	/**
	 * Méthode absrtaite, qui nous permet de connaitre la vitesse du robot sachant la nature du terrain
	 * sur lequel il se trouve
	 * @param nature nature du terrain
	 * @return vitesse : double
	 */
	abstract public double getVitesse(NatureTerrain nature);
	
	
	
	/**
	 * Méthode qui permet de deverser l'eau 
	 * @param vol volume d'eau à derveser
	 */
	abstract public void deverserEau(int vol);
	abstract public void deverserEauCourant(int vol);

	
	
	/**
	 * Méthode qui permet de remplir le reservoir
	 */
	abstract public void remplirEau();
	abstract public void remplirEauCourant();
	
	
	/**
	 * Méthode qui permet de retourner le reservoir
	 */
	abstract public double getReservoir();
	abstract public double getReservoirCourant();
	
	/**
	 * Méthode qui nous permet de connaitre si le robot peut acceder à une case ou non
	 * @param nature nature du terrain de la case 
	 * @return boolean : true si le robot peur acceder a ce type de terrain, false sinon.
	 */
	abstract public boolean hasAccessto(NatureTerrain nature);
}
