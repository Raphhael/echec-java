package raphael.jeu;

import raphael.jeu.pieces.Dame;

/**
 * Un coup est défini par
 * <ul> <li>Position de départ (from)</li>
 * 		<li>Position de retour (to)</li>
 * 		<li>Type de coup</li> </ul>
 *
 * Le type de coup est une information supplémentaire :
 *  - roque, roque annulé...
 */
public class Coup {
	private int			from;
	private int			to;
	private TypeCoup	typeCoup;

	public Coup(int from, int to, TypeCoup typeCoup) {
		this.from = from;
		this.to = to;
		this.typeCoup = typeCoup;
	}
	
	public Coup(int from, int to) {
		this(from, to, TypeCoup.NORMAL);
	}

	public int getTo() {	return to; }
	public int getFrom() {	return from; }
	
	private static void roquer(Etat etat, int posRoiDep, int posRoiFin,
									 int posTourDep, int posTourFin) {
		int tour = etat.getPlateau().getCase(posTourDep);
		int roi = etat.getPlateau().getCase(posRoiDep);
		
		etat.getPlateau().removeCase(posRoiDep);
		etat.getPlateau().removeCase(posTourDep);
		
		etat.getPlateau().setCase(posRoiFin, roi);
		etat.getPlateau().setCase(posTourFin, tour);
	}
	/*
	 * Essai d'une méthode jouer plus légère que jouer pour vérifier 
	 * si un coup est valide ...
	 */
	public boolean isValid(Etat etat) {
		if(etat.getPlateau().getPositionRoi(etat.getTrait().oppose()) == to)
			return false;
		
		if(typeCoup != TypeCoup.NORMAL)
			return jouer(etat) != etat;
		
		Plateau plateau = etat.getPlateau();
		
		int sauvFrom = plateau.getCase(from);
		int sauvTo = plateau.getCase(to);
		ListeDeCoups sauvCoups = plateau.getListeDeCoups(etat.getTrait().oppose(), false);
		plateau.setListeDeCoups(null, false, etat.getTrait().oppose());
				
		plateau.removeCase(from);
		plateau.setCase(to, sauvFrom);
		
		boolean res = !plateau.enEchec(Piece.getCouleur(sauvFrom));
		
		plateau.setListeDeCoups(sauvCoups, false, etat.getTrait().oppose());
		
		plateau.setCase(from, sauvFrom);
		
		if(sauvTo != 0)	plateau.setCase(to, sauvTo);
		else			plateau.removeCase(to);
		
		return res;
	}
	
	/**
	 * Applique le coup sur un état courant
	 * 
	 * On fait la copie profonde (état, plateau, ...)
	 * et on applique le coup.
	 * Si le coup est correct (pas d'échec après l'avoir joué), on renvoie
	 * le nouvel état. Si il est incorrect, on renvoi l'ancien état.
	 * 
	 * @param	etatCourant L'état auquel appliquer ce coup
	 * @return				Le nouvel état si OK sinon l'ancien état
	 */
	public Etat jouer(Etat etatCourant) {
		Etat nouvelEtat = new Etat(etatCourant, this);
		
		switch (typeCoup) {
			case GRAND_ROQUE_ANNULE:
				if(nouvelEtat.getTrait() == CouleurPiece.BLANC)
					nouvelEtat.annulerGrandRoqueBlanc();
				else
					nouvelEtat.annulerGrandRoqueNoir();
				jouerNormalement(nouvelEtat);
				break;
			case PETIT_ROQUE_ANNULE:
				if(nouvelEtat.getTrait() == CouleurPiece.BLANC)
					nouvelEtat.annulerPetitRoqueBlanc();
				else
					nouvelEtat.annulerPetitRoqueNoir();
				jouerNormalement(nouvelEtat);
				break;
			case ROI_BOUGE:
				if(nouvelEtat.getTrait() == CouleurPiece.BLANC) {
					nouvelEtat.annulerPetitRoqueBlanc();
					nouvelEtat.annulerGrandRoqueBlanc();
				}
				else {
					nouvelEtat.annulerPetitRoqueNoir();
					nouvelEtat.annulerGrandRoqueNoir();
				}
				jouerNormalement(nouvelEtat);
				break;
			case PION_DAME:
				nouvelEtat.getPlateau().setCase(to, Piece.makePiece(new Dame(nouvelEtat.getTrait())));
				nouvelEtat.getPlateau().removeCase(from);
				break;
			case NORMAL:
				jouerNormalement(nouvelEtat);
				break;
			case ROQUE_PETIT_BLANC:
				roquer(nouvelEtat, 60, 62, 63, 61);
				nouvelEtat.annulerPetitRoqueBlanc();
				nouvelEtat.annulerGrandRoqueBlanc();
				break;
			case ROQUE_PETIT_NOIR:
				roquer(nouvelEtat, 4, 6, 7, 5);
				nouvelEtat.annulerPetitRoqueNoir();
				nouvelEtat.annulerGrandRoqueNoir();
				break;
			case ROQUE_GRAND_BLANC:
				roquer(nouvelEtat, 60, 58, 56, 59);
				nouvelEtat.annulerPetitRoqueBlanc();
				nouvelEtat.annulerGrandRoqueBlanc();
				break;
			case ROQUE_GRAND_NOIR:
				roquer(nouvelEtat, 4, 2, 0, 3);
				nouvelEtat.annulerPetitRoqueNoir();
				nouvelEtat.annulerGrandRoqueNoir();
				break;
			default:
				break;
		}
		if(nouvelEtat.enEchec())
			return etatCourant;
		else {
			nouvelEtat.changerTrait();
			return nouvelEtat;
		}
	}
	
	private void jouerNormalement(Etat etat) {
		etat.getPlateau().setCase(to, etat.getPlateau().getCase(from));
		etat.getPlateau().removeCase(from);
	}
	
	@Override
	public String toString() {
		return  Utilitaire.positionFormat(from) + 
				Utilitaire.positionFormat(to);
	}
	
	public enum TypeCoup {
		ROI_BOUGE,
		PETIT_ROQUE_ANNULE,
		GRAND_ROQUE_ANNULE,
		NORMAL,
		ROQUE_PETIT_BLANC,
		ROQUE_GRAND_BLANC,
		ROQUE_PETIT_NOIR,
		ROQUE_GRAND_NOIR,
		PION_DAME
	}
	
	public boolean is(TypeCoup typeCoup) {
		return typeCoup == this.typeCoup;
	}
}
