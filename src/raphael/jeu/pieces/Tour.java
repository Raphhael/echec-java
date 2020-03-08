package raphael.jeu.pieces;
import raphael.jeu.CouleurPiece;
import raphael.jeu.Coup;
import raphael.jeu.Coup.TypeCoup;
import raphael.jeu.ListeDeCoups;
import raphael.jeu.Piece;
import raphael.jeu.Plateau;
import raphael.jeu.Utilitaire;

public class Tour extends Piece {

	private static final int [] DEPLACEMENTS = { -10, 10, -1, 1 };
	
	public Tour(CouleurPiece couleur) {
		super(couleur);
	}

	public static ListeDeCoups listeCoups(Plateau plateau, int position,  int piece) {
		ListeDeCoups liste = new ListeDeCoups();
		boolean aDejaBouge = getABouge(piece);

		Coup.TypeCoup type = aDejaBouge ? TypeCoup.NORMAL : 
			(Utilitaire.indexToColumn(position) == 7 ? Coup.TypeCoup.PETIT_ROQUE_ANNULE
					: Coup.TypeCoup.GRAND_ROQUE_ANNULE);
		
		for (int deplacement : DEPLACEMENTS) {
			boolean quitter = false;
			int i = 0; // Coef multiplicateur pour le deplacement
			while(!quitter) {
				int to = positionTAB120(deplacement * ++i, position);
				
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
