package raphael.algo;

import java.util.List;

public interface Noeud {
	public abstract List<Noeud> successeurs();
	public abstract boolean 	estTerminal();
	public abstract int			evaluation(Joueur joueur);

	static abstract class NoeudUtilitaire implements Noeud {
		public List<Noeud> successeurs() { return null; }
		public boolean     estTerminal() { return false; }
	}
	static class NoeudMin extends NoeudUtilitaire {
		public int evaluation(Joueur joueur) { return Integer.MIN_VALUE; }
	}
	static class NoeudMax extends NoeudUtilitaire {
		public int evaluation(Joueur joueur) { return Integer.MAX_VALUE; }
	}
}
