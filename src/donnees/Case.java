package donnees;
import gui.ImageElement;

import java.util.Objects;

import gui.GUISimulator;
/**
 * classe case, qui nous permet de caractériser une case par :
 * la ligne et la colonne  où elle se trouve
 * et la nature de terrain dans cette case 
 */
public class Case {
	private int ligne;
	private int colonne;
	private NatureTerrain nature;
		
	/**
	 * Constructeur public qui crée un case sans connaitre la  nature de terrain
	 * @param ligne ligne où elle se triuve dans la carte
	 * @param colonne colonne où elle se trouve dans la carte
	 */
	public Case(int ligne, int colonne) {
		this.ligne = ligne;
		this.colonne = colonne;
		this.nature = null;
	}
	/**
	 * Constructeur public qui crée une case avec tous ses attributs 
	 * @param ligne ligne où elle se trouve dans la carte
	 * @param colonne colonne où elle se trouve dans la carte
	 * @param nature nature du terrain de la case
	 */
	public Case(int ligne, int colonne, NatureTerrain nature) {
		this.ligne = ligne;
		this.colonne = colonne;
		this.nature = nature;
	}
	public String toString() {
		return " la case de nature " + nature + " se situe dans la ligne " + ligne + " et la colonne " + colonne;
	}
	public int getLigne() {
		return ligne;
	}
	public int getColonne() {
		return colonne;
	}
	public NatureTerrain getNature() {
		return nature;
	}
	/**
	 * Méthode qui sert a définir la nature du terrain d'une case créé par le premier constructeur, 
	 * ou bien changer la nature du terrain ( ce qui n'est pas utile dans notre cas d'utilisation de la carte)
	 * @param nature (nouvelle) nature du terrain de la case
	 */
	public void setNature(NatureTerrain nature) {
		this.nature = nature;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Case other = (Case) obj;
		return colonne == other.colonne && ligne == other.ligne;
	}
	
}
