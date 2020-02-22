package raphael.algo;

import raphael.jeu.Etat;

public class AlphaBeta implements Algorithme{
	private static Noeud bon;
	private static Joueur joueur;
	private static int profOri;
	private static int calc = 0;

	public Noeud start(Noeud noeud, int profondeur, Joueur joueur) {
		AlphaBeta.joueur = joueur;
		profOri = profondeur;
		Noeud best = alphaBeta(noeud, profondeur, true, Integer.MIN_VALUE, Integer.MAX_VALUE);
		System.out.println("Meilleur score : " + best.evaluation(joueur));
		System.out.println("Avec comme coups : " + ((Etat) best).coupsPrecedents());
		System.out.println("calc " + calc + " successeurs()");
		return bon;
	}
	
	public static Noeud alphaBeta(Noeud noeud, int profondeur, boolean estMax, int alpha, int beta) {
		if(profondeur == 0 || noeud.estTerminal())
			return noeud;
		calc++;
		
		
		if(estMax) {
			Noeud maximum = new Noeud.NoeudMin();
			int maxeval = maximum.evaluation(joueur);
			
			for (Noeud fils : noeud.successeurs()) {
				Noeud noeudRemonte = alphaBeta(fils, profondeur - 1, false, alpha, beta);
				int noeudRemonteEval = noeudRemonte.evaluation(joueur);
				if(maxeval < noeudRemonteEval) {
					maximum = noeudRemonte;
					maxeval = noeudRemonteEval;
					if(profOri == profondeur)
						bon = fils;
				}
				
				if(maxeval >= beta)
					return maximum;
				
				if(maxeval > alpha)
					alpha = maxeval;
			}
			return maximum;
		}
		else {
			Noeud minimum = new Noeud.NoeudMax();
			int mineval = minimum.evaluation(joueur);
			for (Noeud fils : noeud.successeurs()) {
				Noeud noeudRemonte = alphaBeta(fils, profondeur - 1, true, alpha, beta);
				int remonteval = noeudRemonte.evaluation(joueur);
				
				if(minimum.evaluation(joueur) > remonteval) {
					minimum = noeudRemonte;
					mineval = remonteval;
				}

				if(mineval <= alpha)
					return minimum;
				
				if(mineval <= beta)
					beta = mineval;
			}
			return minimum;
		}
	}
}
