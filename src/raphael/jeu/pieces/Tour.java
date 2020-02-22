package raphael.jeu.pieces;
import java.util.ArrayList;

import raphael.jeu.CouleurPiece;
import raphael.jeu.Coup;
import raphael.jeu.Coup.TypeCoup;
import raphael.jeu.Piece;
import raphael.jeu.Utilitaire;

public class Tour extends Piece {

	private final int [] DEPLACEMENTS = { -10, 10, -1, 1 };
	private boolean aDejaBouge = false;
	
	public Tour(Tour tour) {
		super(tour);
		aDejaBouge = tour.aDejaBouge;
	}
	
	public Tour(CouleurPiece couleur) {
		super(couleur);
	}
	
	public Tour(CouleurPiece couleur, int position) {
		super(couleur, position);
	}

	@Override
	public ArrayList<Coup> listeCoups(boolean allerProfond) {
		ArrayList<Coup> liste = new ArrayList<Coup>(8);
		Coup.TypeCoup type = aDejaBouge ? TypeCoup.NORMAL : 
			(Utilitaire.indexToColumn(getPosition()) == 7 ? Coup.TypeCoup.PETIT_ROQUE_ANNULE
					: Coup.TypeCoup.GRAND_ROQUE_ANNULE);
		
		for (int deplacement : DEPLACEMENTS) {
			boolean quitter = false;
			int i = 0; // Coef multiplicateur pour le deplacement
			while(!quitter) {
				int to = positionTAB120(deplacement * ++i);
				
				if(to != -1) {
					if(getPlateau().getCase(to) != null)
						quitter = true;
					if(getPlateau().getCase(to) == null || getPlateau().getCase(to).getCouleur() != getCouleur())
						liste.add(new Coup(getPosition(), to, type));
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
	protected Tour makeCopy() {
		return new Tour(this);
	}
}
