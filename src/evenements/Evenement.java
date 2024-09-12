package evenements;
import java.util.Objects;


/**
 * classe abstraite qui permet la gestion des evenements 
 */
public abstract class Evenement {

	private long Date;
	
	/**
	 * Constructeur public, crée un nouvel évenement.
	 * @param Date
	 */
	public Evenement(long Date) {
		this.Date = Date;
	}
	
	public long getDate() {
		return this.Date;
	}
	
	/**
	 * Méthode abstraite execute, execute l'évenement
	 */
	public abstract void execute();
	
	
	@Override
	public int hashCode() {
		return Objects.hash(Date);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Evenement other = (Evenement) obj;
		return Date == other.Date;
	}
}
