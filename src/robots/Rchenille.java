package robots;
import donnees.*;

/**
 * Classe Rchenille, une classe fille de la classe Robot, qui caractérise un nouveau type de robots:
 * Les robots à chenilles, qui not une vitesse maximale de 80km/h, un reservoir de 2000 L,
 * et qui ne peuvent pas traverser l'eau et les roches.
 */
public class Rchenille extends Robot {
	private double reservoir;
	private double reservoirCourant;
	/**
	 * Constructeur public, qui construit un robot à chenille avec une vitesse par défaut 60 km/h
	 * @param position case dans laquelle le robot à chenilles se trouve
	 * @param reservoir volume d'eau dans son reservoir.
	 */
	public Rchenille(Case position, Carte carte, double reservoir) {
		super(60, position, carte);
		this.reservoir = reservoir;
		this.reservoirCourant = reservoir;
	}
	/**
	 * Constructeur public, qui crée un robot à chenilles avec une vitesse donnée (qui ne doit pas dépasser 80 km/h)
	 * @param vitesse vitesse du robot
	 * @param position case dans laquelle le robot à chenilles se trouve
	 * @param reservoir volume d'eau dans le reservoir du robot à chenilles
	 */
	public Rchenille(double vitesse, Case position, Carte carte, double reservoir) {
		super(vitesse, position, carte);
		if (vitesse > 80) {
			this.vitesse = 80;
		}
		this.reservoir = reservoir;
		this.reservoirCourant = reservoir;
	}
	public String toString() {
		return " le robot à chenilles a " + reservoir + " litres dans son réservoir et se situe dans la position " + 
				super.getPosition().toString() + " et se déplace à " + vitesse;
	}
	@Override
	public double getVitesse(NatureTerrain nature) {
		switch(nature) {
			case FORET:
				return vitesse / 2;
			default :
				return vitesse;
		}
	}

	@Override
	public void deverserEau(int vol) {
		reservoir -= vol;
	}
	public double getReservoir() {
		return reservoir;
	}
	public void remplirEau() {
		reservoir = 2000;
	}
	public void remplirEauCourant() {
		reservoirCourant = 2000;
	}
	public double getReservoirCourant() {
		return reservoirCourant;
	}
	public void deverserEauCourant(int vol) {
		reservoirCourant -= vol;
	}
	@Override
	public boolean hasAccessto(NatureTerrain nature) {
		switch(nature) {
			case EAU:
				return false;
			case ROCHE:
				return false;
			default:
				return true;
		}
	}
	
	@Override
	public String returnType() {
		return "RCHENILLE";
	}
	
	@Override
	public double waterBar() {
		double currentWaterPercentage = (reservoir/2000)*100; 
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
	
	public long tempsCharge() {
		return (long) (5 * 60); //5min
	}
	
	public long tempsEteinte(double litresAverser) {
		return (long) (8 * litresAverser)/100; 
	}


}
