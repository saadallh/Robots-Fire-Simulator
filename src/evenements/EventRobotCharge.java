package evenements;
import robots.*;
/**
 * CLasse EventRobotCharge, fille de Evenement, c'est l'evenement du remplissage du reservoir du robot.
 */
public class EventRobotCharge extends Evenement {
	private Robot robot;
	/**
	 * Constructeur public, crée un nouveau évenement EventRobotCharge.
	 * @param date
	 * @param robot
	 */
	public EventRobotCharge(long date , Robot robot) {
		super(date);
		this.robot = robot;
	}
	/**
	 * remplir le reservoir d'un robot
	 */
	public void execute () {
		System.out.println(this.getDate() + " charge son reservoir ") ;
		robot.remplirEau();
	}
}
