package evenements;
import donnees.*;
import robots.*;
/**
 * Classe fille de ma classe Evenement, c'est l'evenement de l'extinction du feu.
 */
public class EventRobotFire extends Evenement {
	private Robot robot;
	private Incendie[] incendieTableau;
	/**
	 * Constructeur public, crée un nouveau evenement EventRobotFire
	 * @param date
	 * @param robot
	 * @param incendieTableau
	 */
	public EventRobotFire(long date , Robot robot, Incendie[] incendieTableau) {
		super(date);
		this.robot = robot;
		this.incendieTableau = incendieTableau;
	}
	/**
	 * eteindre immediatement l'incendie qui se trouve dans la case courante du robot
	 * tout en modifiant l'intensité et le reservoir
	 * si il n'y a pas d'incendie dans cette case une exception sera levée
	 */
	public void execute () {
		//Chercher l'incendie a eteidndre
		Incendie fireToKill = null;
		
		for (Incendie incendie : incendieTableau) {
			if (incendie.getPosition().equals(this.robot.getPosition())) {
				fireToKill = incendie;
				break; 	
			}
		}
		
		try {
		double reservoir = robot.getReservoir();
		if (fireToKill.getIntensite() - reservoir > 0) {
			fireToKill.setIntensite(fireToKill.getIntensite() - reservoir);
			System.out.println("Il reste " + fireToKill.getIntensite() + " pour l'éteindre");
			robot.deverserEau((int) reservoir);
		}
		else {
			//robot.deverserEau((int) fireToKill.getIntensite());
			System.out.println("Incendie éteinte GG");
			robot.deverserEau((int) fireToKill.getIntensite());
			fireToKill.setIntensite(0);
		}
		}catch(NullPointerException e) {
			System.out.println("La case n'a pas d'incendie");
		}
	}
		
}
