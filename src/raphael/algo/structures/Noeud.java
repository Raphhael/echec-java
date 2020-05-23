package raphael.algo.structures;

public interface Noeud {
	/**
	 * Liste des fils du noeud courant
	 * 
	 * @return Une liste de noeuds
	 */
	ListeDeNoeuds<? extends Noeud> successeurs();
	
	/**
	 * Le noeud père du noeud courant
	 * 
	 * @return le noeud père
	 */
	Noeud		parent();
	
	/**
	 * Vérifie si le noeud est une feuille (aucun fils) de l'arbre
	 * 
	 * @return vrai si le noeud est une feuille, faux sinon
	 */
	boolean 	estTerminal();
	
	/**
	 * Retourne une note, une estimation du noeud courant pour le joueur *joueur*
	 * 
	 * @param joueur Le joueur pour qui on fait l'estimation
	 * @return Une valeur
	 */
	int			evaluation(Joueur joueur);
	
	/**
	 * À qui le tour
	 * 
	 * @return le joueur qui doit jouer
	 */
	Joueur		getTrait();
	
	/**
	 * L'empreinte unique du noeud courant
	 * 
	 * @return L'empreinte
	 */
	long		hash();
}
