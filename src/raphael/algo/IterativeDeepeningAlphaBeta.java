package raphael.algo;

import raphael.algo.AlgorithmeException.NoMoreChoicesException;
import raphael.algo.structures.Noeud;
import raphael.algo.structures.TTElement;

/**
 * Algorithme Iterative-deepening
 */
public class IterativeDeepeningAlphaBeta extends AlphaBetaMemory {
	
	public Noeud start(Noeud noeud, int profondeurMax) throws NoMoreChoicesException {
		int profondeur = 0;
		TTElement ttElement = null;
		do {
			ttElement = getTable().retrieveBest(profondeur);
			alphaBeta(noeud, ++profondeur, true, Constantes.MOINS_INFINI, Constantes.PLUS_INFINI);
		} while(!timesUp());
		
		if(ttElement == null || ttElement.getMove() == null)
			throw new NoMoreChoicesException();
		return ttElement.getMove();
	}
	
	public boolean stopCondition(Noeud noeud, int profondeur) {
		return super.stopCondition(noeud, profondeur) || timesUp();
	}
}
