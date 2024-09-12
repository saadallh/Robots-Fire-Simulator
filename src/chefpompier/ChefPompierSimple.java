package chefpompier;
import java.util.*;
import donnees.*;
import simulation.*;
import robots.*;
/**
 * Classe ChefPompierSimple, classe fille de chefPompier, dont la strategie est d'affecter aléatoirement un robot disponible 
 * à une incendie.
 */
public class ChefPompierSimple extends ChefPompier{
	/**
	 * Constructeur public, qui crée un nouveau ChefPompierSimple dont la stratégie est élémentaire.
	 * @param donnees
	 */
	public ChefPompierSimple(Carte carte, DonneesSimulation donnees) {
		super(donnees);
	}

	/**
	 * Méthode qui vérifie si un robot est disponible ou pas
	 * @param robot
	 * @param destination
	 * @return boolean
	 */
	public boolean canGoElementaire(Robot robot, Simulateur simulateur) {
		if (robot.getDateArrive() +1 > simulateur.getDateSimulation()) {
			return false;
		}
		return true;
	}
	
	
	public void strategie(Simulateur simulateur,Robot[] robotTab, Incendie[] incendieTab) {
		for (Robot robot : robotTab) {
			if (canGoElementaire(robot, simulateur)) {
				for (Incendie incendie : incendieTab) {
					if (!(incendie.isAffecte()) && robot.hasAccessto(incendie.getPosition().getNature())) {
						incendie.setAffecte(true);
						while (incendie.getIntensiteCourante() != 0) {
							robot.programmeEvents(incendie.getPosition(), simulateur);
							System.out.println("**** incendie" + incendie + "affecte au robot" + robot + "******");
							robot.eteindreIncendie(simulateur, incendie);						
						}
						break;
					}
				}
			}
		}
	}		
}
	

