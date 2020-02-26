package raphael.algo;

import java.util.List;

public interface Noeud {
	List<Noeud> successeurs();
	boolean 	estTerminal();
	int			evaluation(Joueur joueur);
	long		hash();

	static abstract class NoeudUtilitaire implements Noeud {
		public List<Noeud> successeurs() { return null; }
		public boolean     estTerminal() { return false; }
		public long    	   hash() 		 { return 0L; }
	}
	static class NoeudMin extends NoeudUtilitaire {
		public int evaluation(Joueur joueur) { return Integer.MIN_VALUE; }
	}
	static class NoeudMax extends NoeudUtilitaire {
		public int evaluation(Joueur joueur) { return Integer.MAX_VALUE; }
	}
}
