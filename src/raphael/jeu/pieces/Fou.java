package raphael.jeu.pieces;

import java.util.ArrayList;
import java.util.List;

import raphael.jeu.CouleurPiece;
import raphael.jeu.Coup;
import raphael.jeu.Piece;

public class Fou extends Piece {

	private final int [] DEPLACEMENTS = { -11,-9,11,9 };

	public Fou(Fou fou) {
		super(fou);
	}
	
	public Fou(CouleurPiece couleur) {
		super(couleur);
	}
	public Fou(CouleurPiece couleur, int position) {
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
		return 330;
	}

	@Override
	protected byte getZobriestValue() {
		return 0x06;
	}
	
	@Override
	protected Fou makeCopy() {
		return new Fou(this);
	}
}
