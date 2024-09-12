package donnees;
import robots.*;
/**
 * Classe DonneesSimulation, une classe qui regroupe toutes les données dont le simulateur aura besoin, à savoir :
 * la carte, la taille des cases, le format et les cases (ou encore les natures des terrains)
 * Informations sur les incendies : leurs positions et leurs intensités
 * Informations sur les robots : leurs positions, leurs types, vitesse et réservoir
 * intensiteTotale: La somme des intensite des incendies sur la carte
 */
public class DonneesSimulation {

	private Carte carte;
	private Incendie[] incendies;
	private Robot[] robots;
	/**
	 * Constructeur public de classe, qui crée un objet DonneesSimulation avec tous ses attribus=ts, ce qui est logique
	 * car en l'absence d'un seul attribut il n'aura pas de simulation
	 * @param carte la carte sur laquelle la simulation aura lieu 
	 * @param incendies tableau de TOUTES les incendies dans la carte
	 * @param robots tableau de TOUS les robots dans la carte 
	 */
	public DonneesSimulation(Carte carte, Incendie[] incendies, Robot[] robots) {
		this.carte = carte;
		this.incendies = incendies;
		this.robots = robots;
		
	}
	public Carte getCarte() {
		return carte;
	}
	public void setCarte(Carte carte) {
		this.carte = carte;
	}
	public Incendie[] getIncendie() {
		return incendies;
	}
	public void setIncendie(Incendie[] incendies) {
		this.incendies = incendies;
	}
	public Incendie getIncendie(int i) {
		return incendies[i];
	}
	public Robot[] getrobot() {
		return robots;
	}
	public void setRobot(Robot[] robots) {
		this.robots = robots;
	}
	public Robot getrobot(int i) {
		return robots[i];
	}
	
}
