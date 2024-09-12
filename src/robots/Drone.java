package robots;
import donnees.*;

/**
 * La classe Drone, une classe fille de la classe robot, qui caractérise un nouveau type de robots:
 * Les drones, qui ont une vitesse maximale de 150 km/h, un réservoir de 10000 L et qui peuvent 
 * traverser tout type de terrain. 
 */
public class Drone extends Robot {

	private double reservoir;
	private double reservoirCourant;
	/**
	 * Constructeur public, qui crée un nouveau drone qui a une vitesse par défaut (ie 100 km/h)
	 * @param position case dans laquelle le drone se trouve dans la carte
	 * @param reservoir volume d'eau dans son reservoir
	 */
	public Drone(Case position, Carte carte, double reservoir) {
		super(100, position, carte);
		this.reservoir = reservoir;
		this.reservoirCourant = reservoir;
	}
	/**
	 * Constructeur public, qui crée un nouveau drone 
	 * @param vitesse vitesse du drone, qui ne doit pas dépasser 150 km/h
	 * @param position case dans laquelle le drone se trouve dans la carte
	 * @param reservoir volume d'eau dans son reservoir
	 */
	public Drone(double vitesse, Case position, Carte carte, double reservoir) {
		super(vitesse, position, carte);
		if (this.vitesse > 150) {
			this.vitesse = 150;
		}
		this.reservoir = reservoir;
		this.reservoirCourant = reservoir;
	}
	@Override
	public String toString() {
		return "le Drone a " + reservoir  + " litres d'eau dans son réservoir, et est dans la position " + 
				super.getPosition().toString() + " et vole à une vitesse de " + vitesse;
	}
	@Override
	public double getVitesse(NatureTerrain nature) {
		return vitesse;
	}
	public double getReservoir() {
		return reservoir;
	}
	public double getReservoirCourant() {
		return reservoirCourant;
	}
	@Override
	public void deverserEau(int vol) {
		reservoir -= vol;
	}
	public void deverserEauCourant(int vol) {
		reservoir -= vol;
	}
	/**
	 * Méthode remplirEau, qui remplit le réservoir du drone.
	 */
	public void remplirEau() {
		reservoir = 10000;
	}
	public void remplirEauCourant() {
		reservoir = 10000;
	}
	
	@Override
	public boolean hasAccessto(NatureTerrain nature) {
		return true;
	}
	
	
	public String returnType() {
		return "DRONE";
	}
	
	public double waterBar() {
		double currentWaterPercentage = (reservoir/10000)*100; 
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
		return (long) (30 * 60); //30min
	}
	
	/**
	 * Méthode tempsEteinte, renvoie le temps nécessaire pour verser un volume donné.
	 * @param litresAverser
	 * @return tempsEteinte
	 */
	public long tempsEteinte(double litresAverser) {
		return (long) (30 * litresAverser)/10000; 
	}
	
}
