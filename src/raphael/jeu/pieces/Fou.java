package raphael.jeu.pieces;

import java.util.List;

import raphael.jeu.CouleurPiece;
import raphael.jeu.Coup;
import raphael.jeu.Piece;
import raphael.jeu.Plateau;

public class Fou extends Piece {

	private static final int [] DEPLACEMENTS = { -11,-9,11,9 };

	
	public Fou(CouleurPiece couleur) {
		super(couleur);
	}
	
	public static List<Coup> listeCoups(Plateau plateau, int position, int piece, List<Coup> liste) {
				
		for (int i = 0; i < DEPLACEMENTS.length; i++) {
			boolean quitter = false;
			int j = 0; // Coef multiplicateur pour le deplacement
			
			while(!quitter) {
				int to = positionTAB120(DEPLACEMENTS[i] * ++j, position);
				
				if(to != -1) {
					if(plateau.getCase(to) != 0)
						quitter = true;
					if(plateau.getCase(to) == 0 || Piece.getCouleur(plateau.getCase(to)) != Piece.getCouleur(piece))
						liste.add(new Coup(position, to));
				}
				else
					quitter = true;
			}
		}
		
		return liste;
	}

	@Override
	protected int getValue() {
		return 330;
	}

	@Override
	protected byte getZobriestValue() {
		return 0x06;
	}
	
	@Override
	public int bitvalue() {
		return 8;
	}
}
