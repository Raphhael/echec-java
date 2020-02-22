package raphael.jeu.pieces;

import java.util.ArrayList;
import java.util.List;

import raphael.jeu.CouleurPiece;
import raphael.jeu.Coup;
import raphael.jeu.Piece;

public class Dame extends Piece {

	private final int [] DEPLACEMENTS = { -11,-9, 11, 9, -10, 10, -1, 1 };

	public Dame(Dame dame) {
		super(dame);
	}
	
	public Dame(CouleurPiece couleur) {
		super(couleur);
	}
	
	public Dame(CouleurPiece couleur, int position) {
		super(couleur, position);
	}

	@Override
	public List<Coup> listeCoups(boolean allerProfond) {
		ArrayList<Coup> liste = new ArrayList<Coup>(8);
		
		for (int deplacement : DEPLACEMENTS) {
			boolean quitter = false;
			int i = 0; // Coef multiplicateur pour le deplacement
			
			while(!quitter) {
				int to = positionTAB120(deplacement * ++i);
				
				if(to != -1) {
					if(getPlateau().getCase(to) != null)
						quitter = true;
					if(getPlateau().getCase(to) == null || getPlateau().getCase(to).getCouleur() != getCouleur())
						liste.add(new Coup(getPosition(), to));
				}
				else
					quitter = true;
			}
		}
		
		return liste;
	}


	@Override
	protected int getValue() {
		return 900;
	}
	@Override
	protected Dame makeCopy() {
		return new Dame(this);
	}
}
