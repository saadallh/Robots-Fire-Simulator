package chefpompier;

import donnees.*;
import simulation.*;
import robots.*;

/**
 * Classe abstraite ChefPompier, dont le role est de transmettre les ordres aux pompiers présents sur la carte.
 */
public abstract class ChefPompier {
	
	protected DonneesSimulation donnees;
	
	/**
	 * Constructeur public, crée un nouveau chefPompier
	 * @param donnees
	 */
	public ChefPompier(DonneesSimulation donnees) {
		this.donnees = donnees;
	}


	public DonneesSimulation getDonnees() {
		return donnees;
	}
	public void setDonnees(DonneesSimulation donnees) {
		this.donnees = donnees;
	}
	/**
	 * Méthode abstraite stratégie, qui génère une stratégie pour eteindre toutes les incendies.
	 * @param simulateur
	 * @param robotTab
	 * @param incendieTab
	 */
	public abstract void strategie(Simulateur simulateur,Robot[] robotTab, Incendie[] incendieTab);
}
