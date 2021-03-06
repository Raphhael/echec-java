package raphael.jeu.pieces;
import java.util.List;

import raphael.jeu.CouleurPiece;
import raphael.jeu.Coup;
import raphael.jeu.Coup.TypeCoup;
import raphael.jeu.Piece;
import raphael.jeu.Plateau;
import raphael.jeu.Utilitaire;

public class Tour extends Piece {

	private static final int [] DEPLACEMENTS = { -1, 10, -10, 1 };
	
	public Tour(CouleurPiece couleur) {
		super(couleur);
	}

	public static List<Coup> listeCoups(Plateau plateau, int position,  int piece, List<Coup> liste) {
		boolean aDejaBouge = getABouge(piece);

		Coup.TypeCoup type = aDejaBouge ? TypeCoup.NORMAL : 
			(Utilitaire.indexToColumn(position) == 7 ? Coup.TypeCoup.PETIT_ROQUE_ANNULE
					: Coup.TypeCoup.GRAND_ROQUE_ANNULE);
		
		for (int i = 0; i < DEPLACEMENTS.length; i++) {
			boolean quitter = false;
			int j = 0; // Coef multiplicateur pour le deplacement
			while(!quitter) {
				int to = positionTAB120(DEPLACEMENTS[i] * ++j, position);
				
				if(to != -1) {
					if(plateau.getCase(to) != 0)
						quitter = true;
					if(plateau.getCase(to) == 0 || Piece.getCouleur(plateau.getCase(to)) != Piece.getCouleur(piece))
						liste.add(new Coup(position, to, type));
				}
				else
					quitter = true;
			}
		}
		
		return liste;
	}

	@Override
	protected int getValue() {
		return 500;
	}

	@Override
	protected byte getZobriestValue() {
		return 0x00;
	}
	
	@Override
	public int bitvalue() {
		return 4;
	}
}
