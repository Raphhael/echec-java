package raphael.algo;

import raphael.algo.structures.Algorithme;
import raphael.algo.structures.ListeDeNoeuds;
import raphael.algo.structures.Noeud;

/**
 * Algorithme alpha-beta dans sa plus simple forme.
 */
public class AlphaBeta extends Algorithme {
	
	public Noeud start(Noeud noeud, int alpha, int beta) {
		ListeDeNoeuds<?> successeurs;
		Noeud            meilleurNoeud = null;
		int              currentValue  = Constantes.MOINS_INFINI;

		successeurs = noeud.successeurs();

		for (int i = 0; i < successeurs.size(); i++) {
			Noeud fils = successeurs.get(i);
			int   ab   = alphaBeta(fils, getProfondeurMax() - 1, false, alpha, beta);
			
			if (ab > currentValue || meilleurNoeud == null) {
				currentValue = ab;
				meilleurNoeud = fils;
			}

			if (currentValue >= beta)
				break;

			alpha = Math.max(alpha, currentValue);
		}

		return meilleurNoeud;
	}
	
	/**
	 * Le premier niveau (noeud max) est traité directement dans la
	 * fonction {@link AlphaBeta#start} pour récupérer facilement
	 * le meilleur coup.
	 */
	@Override
	public Noeud start(Noeud noeud) throws AlgorithmeException {
		return start(noeud, Constantes.MOINS_INFINI, Constantes.PLUS_INFINI);
	}

	public int alphaBeta(Noeud noeud, int profondeur, boolean estMax, int alpha, int beta) {
		ListeDeNoeuds<?> successeurs;
		int              currentValue;
		Metrique.update("nbAppelsRecursifs");
		
		if (stopCondition(noeud, profondeur))
			return noeud.evaluation(getJoueur());

		successeurs = noeud.successeurs();
		Metrique.update("nbSuccesseurs");

		if (estMax) {
			currentValue = Constantes.MOINS_INFINI;

			for (int i = 0; i < successeurs.size(); i++) {
				currentValue = Math.max(currentValue, alphaBeta(successeurs.get(i), profondeur - 1, false, alpha, beta));

				if (currentValue >= beta)
					break;

				alpha = Math.max(alpha, currentValue);
			}
		} else {
			currentValue = Constantes.PLUS_INFINI;

			for (int i = 0; i < successeurs.size(); i++) {
				currentValue = Math.min(currentValue, alphaBeta(successeurs.get(i), profondeur - 1, true, alpha, beta));
				if (currentValue <= alpha)
					break;

				beta = Math.min(beta, currentValue);
			}
		}
		return currentValue;
	}
}
