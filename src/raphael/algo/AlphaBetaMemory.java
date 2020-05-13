package raphael.algo;

import raphael.algo.AlgorithmeException.NoMoreChoicesException;
import raphael.algo.structures.Algorithme;
import raphael.algo.structures.ListeDeNoeuds;
import raphael.algo.structures.Noeud;
import raphael.algo.structures.TTElement;
import raphael.algo.structures.TTElement.TypeNoeud;
import raphael.algo.structures.TranspositionTable;

/**
 * Alpha Beta avec table de transposition
 */
public class AlphaBetaMemory extends Algorithme {
	private static TranspositionTable transpositionTable = new TranspositionTable();

	public Noeud start(Noeud noeud, int profondeurMax) throws NoMoreChoicesException {
		alphaBeta(noeud, profondeurMax, true, Constantes.MOINS_INFINI, Constantes.PLUS_INFINI);
		
		return findPV(profondeurMax);
	}

	public int alphaBeta(Noeud noeud, int profondeur, boolean estMax, int alpha, int beta) {
		Metrique.update("nbAppelsRecursifs");
		int alphaOriginal   = alpha;
		int betaOriginal    = beta;
		int currentValue    = estMax ? Constantes.MOINS_INFINI : Constantes.PLUS_INFINI;
		boolean done        = false;
		Noeud bestMove      = null;
		Noeud evalNoeud     = null;
		TTElement el        = transpositionTable.get(noeud.hash());
		TypeNoeud typeNoeud = TypeNoeud.EXACT;

		if (el != null) {
			if (el.getProfondeur() >= profondeur) {
				if (el.getType() == TypeNoeud.EXACT)
					return el.getEvaluation();
				else if (el.getType() == TypeNoeud.ALPHA)
					alpha = Math.max(alpha, el.getEvaluation());
				else if (el.getType() == TypeNoeud.BETA)
					beta = Math.min(beta, el.getEvaluation());

				if (alpha >= beta)
					return el.getEvaluation();
			}
		}

		if (stopCondition(noeud, profondeur))
			return noeud.evaluation(getJoueur());

		if (el != null && el.getMove() != null) {
			if (estMax) {
				bestMove     = el.getMove();
				currentValue = alphaBeta(bestMove, profondeur - 1, false, alpha, beta);

				if (currentValue >= beta)
					done = true;
			} else {
				bestMove     = el.getMove();
				currentValue = alphaBeta(bestMove, profondeur - 1, true, alpha, beta);

				if (currentValue <= alpha)
					done = true;
			}
		}
		
		if (!done) {
			ListeDeNoeuds<?> successeurs = noeud.successeurs();
			Metrique.update("nbSuccesseurs");

			if (estMax) {
				for (int i = 0; i < successeurs.size(); i++) {
					evalNoeud = successeurs.get(i);

					int ab = alphaBeta(evalNoeud, profondeur - 1, false, alpha, beta);

					if (ab >= currentValue) {
						bestMove = evalNoeud;
						currentValue = ab;
					}

					if (currentValue >= beta)
						break;

					alpha = Math.max(alpha, currentValue);
				}
			} else {
				for (int i = 0; i < successeurs.size(); i++) {
					evalNoeud = successeurs.get(i);
					int ab = alphaBeta(evalNoeud, profondeur - 1, true, alpha, beta);

					if (ab <= currentValue) {
						bestMove = evalNoeud;
						currentValue = ab;
					}

					if (currentValue <= alpha)
						break;

					beta = Math.min(beta, currentValue);
				}
			}
		}

		if (currentValue <= alphaOriginal)
			typeNoeud = TypeNoeud.BETA;
		else if (currentValue >= betaOriginal)
			typeNoeud = TypeNoeud.ALPHA;
		transpositionTable.put(noeud.hash(), new TTElement(profondeur, currentValue, typeNoeud, bestMove));
		
		return currentValue;
	}
	
	/**
	 * Nettoye la table de transposition à la fin d'un tour
	 */
	public void clean() {
		new Thread(()->{
			transpositionTable.maj();
		}).start();
	}
	
	/**
	 * Récupère la table de transposition utilisée
	 * 
	 * @return TranspositionTableInterface la table de transposition
	 */
	public static TranspositionTable getTable() {
		return transpositionTable;
	}
	
	/**
	 * Retrouver la variante principale, celle qui à été calculée comme la
	 * meilleure.
	 * 
	 * @param profondeurMax
	 * @return Le meilleur état du jeu à la profondeur profondeurMax
	 * 
	 * @throws NoMoreChoicesException En cas d'erreurs
	 */
	public Noeud findPV(int profondeurMax) throws NoMoreChoicesException {
		TTElement ttElement = transpositionTable.retrieveBest(profondeurMax);
		if(ttElement.getMove() == null)
			throw new NoMoreChoicesException();
		
		return ttElement.getMove();
	}
	
	@Override
	public void endRoutine() {
		transpositionTable.clear();
	}
}
