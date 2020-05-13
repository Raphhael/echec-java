package raphael.algo.structures;

import java.util.ArrayList;

public abstract class ListeDeNoeuds<T extends Noeud> extends ArrayList<T>{
	private static final long serialVersionUID = 9070678041340365638L;
	
	public abstract void sort(Joueur joueur);
}
