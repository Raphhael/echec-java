package raphael.algo;

import java.util.List;

import raphael.jeu.Etat;
import raphael.jeu.Utilitaire;

public class AlphaBeta implements Algorithme{
	private static Noeud bon;
	private static Joueur joueur;
	private static int profOri;
	private static int calc = 0;

	public Noeud start(Noeud noeud, int profondeur, Joueur joueur) {
		AlphaBeta.joueur = joueur;
		profOri = profondeur;
		int best = alphaBeta(noeud, profondeur, true, Integer.MIN_VALUE, Integer.MAX_VALUE);
		System.out.println("Meilleur score : " + best);
		System.out.println("calc " + calc + " successeurs()");
		return bon;
	}
	
	public static int alphaBeta(Noeud noeud, int profondeur, boolean estMax, int alpha, int beta) {
		if(profondeur == 0 || noeud.estTerminal())
			return noeud.evaluation(joueur);
		calc++;
		
		List<Noeud> succ = noeud.successeurs();
		if(profondeur > 1) {
			succ.sort((o1, o2) -> {
				int row1 = Math.abs(Utilitaire.indexToRow(((Etat) o1).getCoupPrecedent().getFrom()));
				int row2 = Math.abs(Utilitaire.indexToRow(((Etat) o2).getCoupPrecedent().getFrom()));
				int col1 = Math.abs(Utilitaire.indexToColumn(((Etat) o1).getCoupPrecedent().getFrom()) - 4);
				int col2 = Math.abs(Utilitaire.indexToColumn(((Etat) o2).getCoupPrecedent().getFrom()) - 4);
				if(row1 == 1 || row1 == 6) {
					if(row2 != 1 && row2 != 6)
						return 1;
				}
				else {
					if(row2 == 1 || row2 == 6)
						return -1;
				}
				return col2 == col1 ? 0 : (col2 < col1 ? 1 : -1);
			});
		}
		
		if(estMax) {
			int max = Integer.MIN_VALUE;
			
			for (Noeud fils : succ) {
				int remonte = alphaBeta(fils, profondeur - 1, false, alpha, beta);
				if(max < remonte) {
					max = remonte;
					if(profOri == profondeur)
						bon = fils;
				}
				if(max >= beta)
					return max;
				
				alpha = Math.max(alpha, max);
			}
			return max;
		}
		else {
			int min = Integer.MAX_VALUE;
			for (Noeud fils : succ) {
				min = Math.min(min, alphaBeta(fils, profondeur - 1, true, alpha, beta));

				if(min <= alpha)
					return min;
				
				beta = Math.min(beta, min);
			}
			return min;
		}
	}
}
