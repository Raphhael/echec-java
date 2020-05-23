package raphael.algo;

import raphael.algo.structures.Noeud;

/**
 * Algorithme Iterative-deepening avec temps max
 */
public class IterativeDeepeningFixedTime extends IterativeDeepening {
	@Override
	public boolean exit_loop(int profondeur) {
		return !timesUp();
	}

	@Override
	public boolean stopCondition(Noeud noeud, int profondeur) {
		return super.stopCondition(noeud, profondeur) || timesUp();
	}

	@Override
	public int getProfondeurMax() {
		return Constantes.PLUS_INFINI;
	}
}
