package raphael.algo;

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
		
		
		if(estMax) {
			int max = Integer.MIN_VALUE;
			
			for (Noeud fils : noeud.successeurs()) {
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
			for (Noeud fils : noeud.successeurs()) {
				min = Math.min(min, alphaBeta(fils, profondeur - 1, true, alpha, beta));

				if(min <= alpha)
					return min;
				
				beta = Math.min(beta, min);
			}
			return min;
		}
	}
}
