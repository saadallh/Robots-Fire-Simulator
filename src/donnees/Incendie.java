package donnees;

/**
 * Classe Incendie, une classe qui nous donne toutes les informations d'une incendie:
 * ses coordonnées dans la carte
 * Son intensité (nb de litres nécessaire pour éteindre le feu
 */
public class Incendie {
	private Case position;
	private double intensite;
	private boolean affecte; // true si l'incendie est affectée à un robot.
	private double intensiteCourante;
	
	
	/**
	 * Constructeur public, qui crée une nouvelle incendie 
	 * @param position case dans laquelle l'incendie se situe dans la carte
	 * @param intensite volume nécessaire pour arrêter l'incendie
	 */
	public Incendie(Case position, double intensite) {
		this.position = position;
		this.intensite = intensite;
		this.affecte = false;
		this.intensiteCourante = intensite;
	}
	
	
	public double getIntensiteCourante() {
		return intensiteCourante;
	}


	public void setIntensiteCourante(double intensiteCourante) {
		this.intensiteCourante = intensiteCourante;
	}


	public boolean isAffecte() {
		return affecte;
	}



	public void setAffecte(boolean affecte) {
		this.affecte = affecte;
	}



	public String toString() {
		return " l'incendie se trouve dans la case " + position.toString() + " et a besoin de " + intensite 
				+ " litres d'eau pour s'eteindre";
	}
	public Case getPosition(){
		return position;
	}
	public double getIntensite() {
		return intensite;
	}
	
	
	/**
	 * Méthode qui réduit l'intensité d'une incendie grâce à l'aide d'un robot
	 * @param vol volume déversé
	 */
	public void eaudeversee(double vol) {
		intensite -= vol;
	}
	
	
	
	public void setPosition(Case new_position) {
		position = new_position;
	}
	public void setIntensite(double intensite) {
		this.intensite = intensite;
	}
	
	
	/**
	 * Méthode qui met à jour l'intensité de l'incendie,
	 * @param vol volume qu'il faut deverser en plus
	 */
	public void updateIntensite(double vol) {
		intensite += vol;
	}
	
}
