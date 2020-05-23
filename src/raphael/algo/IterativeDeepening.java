package raphael.algo;

import raphael.algo.AlgorithmeException.NoMoreChoicesException;
import raphael.algo.structures.Noeud;
import raphael.algo.structures.TTElement;

/**
 * Algorithme Iterative-deepening
 */
public class IterativeDeepening extends AlphaBetaMemory {

	public Noeud start(Noeud noeud) throws NoMoreChoicesException {
		int profondeur = 0;
		int score;
		TTElement ttElement = null;

		do {
			ttElement = getTable().retrieveBest(profondeur);
			score = alphaBeta(noeud, ++profondeur, true, Constantes.MOINS_INFINI, Constantes.PLUS_INFINI);
			if (score > Constantes.MAX_SCORE_SEUIL)
				return getTable().retrieveBest(profondeur).getMove();
		} while (exit_loop(profondeur));

		if (ttElement == null || ttElement.getMove() == null)
			throw new NoMoreChoicesException();

		return ttElement.getMove();
	}

	/**
	 * Condition de sortie de la boucle
	 * 
	 * @param profondeur La profondeur actuelle de recherche
	 * @return vrai si on doit rester dans la boucle, faux sinon
	 */
	public boolean exit_loop(int profondeur) {
		return profondeur <= getProfondeurMax();
	}
}
