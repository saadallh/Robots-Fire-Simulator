package chefpompier;
import java.util.*;
import donnees.*;
import simulation.*;
import robots.*;
/**
 * Classe ChefPompierEvloue, classe fille de chefPompier, dont la strategie est d'affecter le robot disponible 
 * le plus proche aux incendies.
 */
public class ChefPompierEvolue extends ChefPompier{

	/**
	 * Constructeur public, crée un nouveau chefPompierEvolue
	 * @param donnees
	 */
	public ChefPompierEvolue(Carte carte, DonneesSimulation donnees) {
		super(donnees);
	}


	
	
	/**
	 * Méthode qui retourne la disponibilité du robotr
	 * @param robot
	 * @param destination
	 * @return boolean
	 */
	public boolean canGo(Robot robot, Simulateur simulateur) {
		/*
		if (robot.getReservoir() == 0) {
			robot.programmeEvents(robot.closestWaterDestination(), simulateur);
			robot.remplirReservoir(simulateur);
			return false;
		}*/
		if (robot.getDateArrive() + 1 > simulateur.getDateSimulation()) {
			return false;
		}
		return true;
	}
	
	
	
	
	/**
	 * Méthode heuristicIncendie, nous renvoie la distance entre le robot et la case caseCourante
	 * @param caseCourante 
	 * @return heuristic : double
	 */
	public double heuristicIncendie(Case caseCourante, Case positionCourante) {
		double heuristic = Math.abs(caseCourante.getColonne() - positionCourante.getColonne()) + 
				Math.abs(caseCourante.getLigne() - positionCourante.getLigne());
		return heuristic;
	}
	
	/**
	 * Méthode closestIncendie, qui renvoie l'incendie la plus proche au robot de position positionCourante
	 * @return incendieProche
	 */
	public Incendie closestIncendie(Case positionCourante) {	
		Incendie[] incendieTableau = donnees.getIncendie();
		Case caseProche = incendieTableau[0].getPosition();
		Double min = Double.POSITIVE_INFINITY;
		for(Incendie incendie : incendieTableau) {
			if ( !(incendie.isAffecte()) && heuristicIncendie(incendie.getPosition(), positionCourante) <= min ){
				caseProche = incendie.getPosition();
				min = heuristicIncendie(caseProche, positionCourante);
			}
		}
		Incendie incendieProche = null;
		for (Incendie incendie : donnees.getIncendie()) {
			if (incendie.getPosition().equals(caseProche)) {
				incendieProche = incendie;
				break; 	
			}
		}
		
		return incendieProche;
	}
	
	/**
	 * Méthode qui renvoie le robot(disponible) le plus proche à l'incendie donnée.
	 * @param incendie
	 * @param simulateur
	 * @param robots
	 * @return robotProche
	 */
	public Robot closestRobot(Incendie incendie, Simulateur simulateur, Robot[] robots) {
		Robot robotProche = null;
        Double min = Double.POSITIVE_INFINITY;
        for(Robot r : robots) {
            if(canGo(r, simulateur) && r.hasAccessto(incendie.getPosition().getNature()) && heuristicIncendie(incendie.getPosition(), r.getPositionCourante()) < min) {
                min = heuristicIncendie(incendie.getPosition(), r.getPositionCourante());
                robotProche = r;
            }
        }
        return robotProche;
    }
	
	public void strategie(Simulateur simulateur,Robot[] robotTab, Incendie[] incendieTab) {

		for (Incendie incendie : incendieTab) {
			if (!(incendie.isAffecte())) {
				Robot robot = closestRobot(incendie, simulateur, robotTab);
				if (robot != null) {
					System.out.println("**** incendie" + incendie + "affecte au robot" + robot + "******");
					incendie.setAffecte(true);
					while (incendie.getIntensiteCourante() != 0) {
						robot.programmeEvents(incendie.getPosition(), simulateur);
						robot.eteindreIncendie(simulateur, incendie);		
					}
				}
			}
		}
	}
	
	/**
	 * Méthode de strategie qui pour chaque robot, elle affecte l'incendie la plus proche
	 * @param simulateur
	 * @param robotTab
	 * @param incendieTab
	 */
	public void strategie2(Simulateur simulateur,Robot[] robotTab, Incendie[] incendieTab) {

		for (Robot robot : robotTab) {
			if (canGo(robot, simulateur)) {
				Incendie incendie = closestIncendie(robot.getPositionCourante());
				if (!(incendie.isAffecte()) && robot.hasAccessto(incendie.getPosition().getNature())) {
					robot.programmeEvents(incendie.getPosition(), simulateur);
					System.out.println("**** incendie" + incendie + "affecte au robot" + robot + "******");
					incendie.setAffecte(true);
					robot.eteindreIncendie(simulateur, incendie);
					while (incendie.getIntensiteCourante() != 0) {
						robot.programmeEvents(incendie.getPosition(), simulateur);
						robot.eteindreIncendie(simulateur, incendie);		
					}
				}
			}
		}
	}
}


	

	

