package raphael.algo.structures;

public interface Noeud {
	ListeDeNoeuds<? extends Noeud> successeurs();
	Noeud		parent();
	boolean 	estTerminal();
	int			evaluation(Joueur joueur);
	long		hash();
}
