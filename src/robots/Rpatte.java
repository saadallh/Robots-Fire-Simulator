package robots;
import donnees.*;
/**
 * la classe Rpatte, une classe fille de la classe Robot, les robots à pattes n'ont pas de reservoir
 */
public class Rpatte extends Robot {
	/**
	 * Constructeur public, qui crée un nouveau Robot à pattes avec la vitesse par défaut 30 km/h
	 * @param position case dans laquelle le robot se trouve
	 */
	public Rpatte(Case position, Carte carte) {
		super(30, position, carte);
	}
	/**
	 * Constructeur public, crée un nouveau Robot à pattes avec une vitesse donnée 
	 * @param vitesse vitesse du robot à pattes
	 * @param position position dans laquelle le robot se trouve
	 */
	public Rpatte(double vitesse, Case position, Carte carte) {
		super(vitesse, position, carte);
	}
	@Override
	public double getVitesse(NatureTerrain nature) {
		switch(nature) {
			case ROCHE:
				return 10;
			default :
				return vitesse;
		}
	}

	@Override
	public void deverserEau(int vol) {
		return;
	}
	public void deverserEauCourant(int vol) {
		return;
	}
	public void remplirEau() {
		return;
	}
	public void remplirEauCourant() {
		return;
	}
	public double getReservoir() {
		return Double.MAX_VALUE;
	}
	
	@Override
	public boolean hasAccessto(NatureTerrain nature) {
		switch(nature) {
			case EAU:
				return false;
			default : 
				return true;
		}
	}
	
	@Override
	public double waterBar() {
		return 0;
	}
	
	@Override
	public String returnType() {
		return "RPATTE";
	}
	
	public long tempsCharge() {
		return (long)0; //pas besoin
	}
	
	public long tempsEteinte(double litresAverser) {
		return (long) (litresAverser)/10; 
	}

	public double getReservoirCourant() {
		return Double.MAX_VALUE;
	}
}
