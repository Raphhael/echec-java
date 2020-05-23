package raphael.algo.structures;

import java.util.ArrayList;

public abstract class ListeDeNoeuds<T extends Noeud> extends ArrayList<T>{
	private static final long serialVersionUID = 9070678041340365638L;
	
	/**
	 * Tri la liste de noeuds dans l'ordre décroissant (du 
	 * plus important au moins important)
	 * 
	 * @param joueur
	 */
	public abstract void sort(Joueur joueur);
}
