package robots;
import donnees.*;
/**
 * Classe Rroue, une classe fille de la classe Robot, qui définit un nouveau type de robot : robot à roues
 * qui a un reservoir de 5000 L et une vitesse par défaut de 80 km/h
 */
public class Rroue extends Robot {
	private double reservoir;
	private double reservoirCourant;

	/**
	 * Constructeur public, qui crée un nouveau robot à roues, avec une vitesse par défaut de 80 km/h
	 * @param position case dans laquelle le robot se trouve.
	 * @param reservoir volume d'eau contenu dans le reservoir du robot à roues.
	 */
	public Rroue(Case position, Carte carte, double reservoir) {
		super(80, position, carte);
		this.reservoir = reservoir;
		this.reservoirCourant = reservoir;
	}
	/**
	 * Constructeur public, qui crée un nouveau robot à roue avec une vitesse donnée
	 * @param vitesse vitesse du robot à roues
	 * @param position case dans laquelle le robot se trouve.
	 * @param reservoir volume d'eau contenu dans le reservoir du robot à roues
	 */
	public Rroue(double vitesse, Case position, Carte carte, double reservoir) {
		super(vitesse, position, carte);
		this.reservoir = reservoir;
		this.reservoirCourant = reservoir;
	}
	@Override
	public String toString() {
		return "le robot à roues a " + reservoir  + " litres d'eau dans son réservoir, et est dans la position " + 
				super.getPosition().toString() + " et roule à une vitesse de " + vitesse;
	}
	@Override
	public double getVitesse(NatureTerrain nature) {
		return vitesse;
	}
	@Override
	public void deverserEau(int vol) {
		reservoir -= vol;
	}
	public void deverserEauCourant(int vol) {
		reservoirCourant -= vol;
	}
	/**
	 * méthode qui remplit le reservoir du robot à roues au maximum.
	 */
	public void remplirEau() {
		reservoir = 5000;
	}
	public double getReservoir() {
		return reservoir;
	}
	@Override
	public double getReservoirCourant() {
		// TODO Auto-generated method stub
		return reservoirCourant;
	}
	public void remplirEauCourant() {
		reservoirCourant = 5000;
	}
	@Override
	public boolean hasAccessto(NatureTerrain nature) {
		switch(nature) {
			case TERRAIN_LIBRE:
				return true;
			case HABITAT:
				return true;
			default:
				return false;
		}
	}
	
	@Override
	public String returnType() {
		return "RROUE";
	}
	
	@Override
	public double waterBar() {
		double currentWaterPercentage = (reservoir/5000)*100; 
		int waterBar;
		if (currentWaterPercentage < 25) {
			waterBar = 0;
		}
		else if (currentWaterPercentage < 50 && currentWaterPercentage >= 25) {
			waterBar = 25;
		}
		else if (currentWaterPercentage < 75 && currentWaterPercentage >= 50) {
			waterBar = 50;
		}
		else if (currentWaterPercentage < 100 && currentWaterPercentage >= 75) {
			waterBar = 75;
		}
		else {
			waterBar = 100;
		}
		
		return waterBar;
	}
	/**
	 * méthode tempsCharge, retourne le temps nécessaire pour remplir le réservoir
	 * @return tempscharge
	 */
	public long tempsCharge() {
		return (long) (10 * 60); //5min
	}
	
	/**
	 * Méthode tempsEteinte, renvoie le temps nécessaire pour verser un volume donné.
	 * @param litresAverser
	 * @return tempsEteinte
	 */
	public long tempsEteinte(double litresAverser) {
		return (long) (5 * litresAverser)/100; 
	}



}
