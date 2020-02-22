package raphael.jeu.pieces;

import java.util.ArrayList;
import java.util.List;

import raphael.jeu.CouleurPiece;
import raphael.jeu.Coup;
import raphael.jeu.Piece;

public class Cavalier extends Piece {
	private final int [] DEPLACEMENTS = { -12,-21,-19,-8,12,21,19,8 };
	
	public Cavalier(Cavalier cavalier) {
		super(cavalier);
	}
	
	public Cavalier(CouleurPiece couleur) {
		super(couleur);
	}
	
	public Cavalier(CouleurPiece couleur, int position) {
		super(couleur, position);
	}

	@Override
	public List<Coup> listeCoups(boolean allerProfond) {
		ArrayList<Coup> liste = new ArrayList<Coup>(8);
		
		for (int deplacement : DEPLACEMENTS) {
			int to = positionTAB120(deplacement);

			if((to != -1 && getPlateau().getCase(to) == null) 
					|| (to != -1 && getPlateau().getCase(to).getCouleur() != getCouleur()))
				liste.add(new Coup(getPosition(), to));
		}
		
		return liste;
	}
	
	@Override
	protected Cavalier makeCopy() {
		return new Cavalier(this);
	}

	@Override
	protected int getValue() {
		return 320;
	}
}
