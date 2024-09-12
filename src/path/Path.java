package path;

import donnees.*;
import robots.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
/**
 * Classe Path, qui, étant donné une source, un destination, un robot et une carte, peut nous génerer un plus court chemin de 
 * la case Source vers la case destination en utilisant l'algorithme A*
 */
public class Path {
	private Case source;
	private Case destination;
	private Robot robot;
	private Carte carte;
	private LinkedList<Direction> path;
	/**
	 * Constructeur public, qui crée un nouveau chemin source ---> destination
	 * @param robot
	 * @param carte
	 * @param source
	 * @param Destination
	 */
	public Path(Robot robot,Carte carte, Case source, Case Destination) {
		this.source = source;
		this.robot = robot;
		this.destination = Destination;
		this.carte = carte;
		this.path = generatePath();
	}

	public LinkedList<Direction> getPath() {
		return path;
	}
	public LinkedList<Direction> generatePath(){
		if( robot.returnType().equals("DRONE")) {
			return cheminDrone();
		}
		return aStar();
	}
	
	/**
	 * méthode heuristic, c'est la méthode qui va nous donner une idée sur notre distance vis à vis de la source
	 * en tenant en compte bien sur la vitesse du robot.
	 * @param caseCourante
	 * @return heuristic value 
	 */
	public double heuristic(Case caseCourante) {
		/**
		 * calcul de la distance entre la caseCourante et la destination
		 */
		double heuristic = Math.abs(caseCourante.getColonne() - destination.getColonne()) + 
				Math.abs(caseCourante.getLigne() - destination.getLigne());
		/**
		 * le code ci-dessous nous permet de savoir si les vecteurs source --> destination et caseCourante -->destination
		 * son alignés ou non à l'aide de leur produit vectoriel.
		 * Si ces deux vecteurs ne sont pas alignés, le produit vectoriel sera plus grand, du coup on donnera la priorité
		 * aux cases dont le vecteur case --> destination est (quasiment) aligné avec source-->destination.
		 */
		double x1 = (double)(caseCourante.getLigne() - destination.getLigne());
		double x2 = (double)(source.getLigne() - destination.getLigne());
		double y1 = (double)(caseCourante.getColonne() - destination.getColonne());
		double y2 = (double)(source.getColonne() - destination.getColonne());
		double cross = Math.abs(x1*y2 - x2*y1);
		heuristic += cross * 0.001;
		return heuristic * carte.getTailleCases() / robot.getVitesse(caseCourante.getNature()) ;
	}
	/**
	 * méthode nbPas, qui nous donne la matrice des nombre de pas fait en explorant la carte afin de trouver le plus court chemin.
	 * @return nbPas matrice qui va être remplie dans la méthode aStar()
	 */
	public double[][] nbPas(){
		double[][] nbPas = new double[carte.getNbLignes()][carte.getNBColonnes()];
		for(Case[] ca : carte.getCarte()) {
			for(Case c : ca) {
				if (c.equals(source)) {
					nbPas[c.getLigne()][c.getColonne()] = 0.0;
				}
				else {
					nbPas[c.getLigne()][c.getColonne()] = Double.POSITIVE_INFINITY;
				}
			}
		}
		return nbPas;
	}
	/**
	 * Méthode cout, qui nous donne la matrice des couts (qu'on essaye de minimiser pour trouver le plus court chemin)
	 * @return cout matrice qui sera remplie dans la méthode aStar()
	 */
	public double[][] cout(){
		double[][] cout = new double[carte.getNbLignes()][carte.getNBColonnes()];
		for(Case[] ca : carte.getCarte()) {
			for(Case c : ca) {
				if(c.equals(source)) {
					cout[c.getLigne()][c.getColonne()] = heuristic(c);
				}
				else {
					cout[c.getLigne()][c.getColonne()] = Double.POSITIVE_INFINITY;
				}
			}
		}
		return cout;
	}
	/**
	 * Méthode get, nous aide à accéder à la valeur d'une case dans une matrice donnée
	 * @param array
	 * @param c
	 * @return vakeur de la case dans la matrice array
	 */
	public double get(double[][] array, Case c) {
		return array[c.getLigne()][c.getColonne()];
	}
	
	/**
	 * méthode cheminDrone, nous permet de trouver un chemin pour un robot de type Drone
	 * @return chemin
	 */
	public LinkedList<Direction> cheminDrone(){
		int distanceLignes = destination.getLigne() - source.getLigne();
		int distanceColonnes = destination.getColonne() - source.getColonne();
		LinkedList<Direction> cheminnaif = new LinkedList<Direction>();
		Direction directionLigne;
		Direction directionColonne;
		int i = 0;
		int j = 0;
		if (distanceColonnes > 0) {
			directionColonne = Direction.EST;
		}
		else if (distanceColonnes < 0) {
			directionColonne = Direction.OUEST;
		}
		else {
			directionColonne = null;
		}
		if (distanceLignes > 0) {
			directionLigne = Direction.SUD;
		}
		else if (distanceLignes < 0) {
			directionLigne = Direction.NORD;
		}
		else {
			directionLigne = null;
		}
		while (i < Math.abs(distanceLignes) && j < Math.abs(distanceColonnes)) {
			cheminnaif.add(directionLigne);
			cheminnaif.add(directionColonne);
			i++;
			j++;
		}
		if (i < Math.abs(distanceLignes)) {
			for (int k = i; k < Math.abs(distanceLignes); k++) {
				cheminnaif.add(directionLigne);
			}
		}
		if (j < Math.abs(distanceColonnes)) {
			for (int k = j; k < Math.abs(distanceColonnes); k++) {
				cheminnaif.add(directionColonne);
			}
		}
		return cheminnaif;
	}
	/**
	 * Méthode aStar, implémente l'algorithme A* pour le plus court chemin
	 * @return path chemin source -->destination
	 */
	public LinkedList<Direction> aStar() {
		double[][] nbPas = nbPas();
		double[][] cout = cout();
		/**
		 * HashMap du chemin dont les clés sont les filles des case explorées dans la chemin et les valeurs sont les parents.
		 */
		HashMap<Case, Case> pathMap = new HashMap<Case, Case>();
		LinkedList<Case> queue = new LinkedList<Case>();
		HashMap<Case, Case> pathMapReversed = new HashMap<Case, Case>();
		LinkedList<Direction> path = new LinkedList<Direction>();
		queue.add(source);
		while(!(queue.isEmpty())) {
			Case caseCourante = queue.poll(); // case prioritaire
			double min = Double.POSITIVE_INFINITY;
			for(Direction d : Direction.values()) {
				if (carte.voisinExiste(caseCourante, d) && robot.hasAccessto(carte.getVoisin(caseCourante, d).getNature())) {
					Case caseFille = carte.getVoisin(caseCourante, d);
					double pasTemp = get(nbPas, caseCourante) + 1.0; // on incrémente le nb de pas
					double coutTemp = pasTemp + heuristic(caseFille);
					if (coutTemp < get(cout, caseFille)) {
						nbPas[caseFille.getLigne()][caseFille.getColonne()] = pasTemp;
						cout[caseFille.getLigne()][caseFille.getColonne()] = coutTemp;
						if(get(cout, caseFille) < min) {
							/**
							 * La case Fille la plus proche est prioritaire dans la recherche du chemin
							 */
							queue.addFirst(caseFille);
							min = get(cout,caseFille);
						}
						else {
							/**
							 * si la case est loin de la destination par rapport aux autres filles, elle n'est pas prioritaire
							 */
							queue.add(caseFille);
						}
						pathMap.put(caseFille, caseCourante);
					}
				}
			}
		}
		/**
		 * Pour l'instant on a un HashMap du chemin mais à l'envers.
		 * Donc on va le renverser à l'aide de cette boucle.
		 */
		Case caseCourante = destination;
		while (! caseCourante.equals(source)) {
			pathMapReversed.put(pathMap.get(caseCourante), caseCourante);
			caseCourante = pathMap.get(caseCourante);
			}
		Case next = pathMapReversed.get(source);
		path.add(carte.getDirection(source, next));
		/**
		 * On veut une LinkedList<Direction> comme chemin, la boucle suivante nous permet de tranformer le chemin de HashMap vers 
		 * LinkedList<Direction>
		 */
		while (! next.equals(destination)) {
			path.add(carte.getDirection(next, pathMapReversed.get(next)));
			next = pathMapReversed.get(next);
		}
		return path;	
		
	}
}

