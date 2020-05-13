package raphael.jeu;

import java.util.ArrayList;
import java.util.List;

import raphael.algo.structures.Joueur;
import raphael.algo.structures.ListeDeNoeuds;
import raphael.algo.structures.Noeud;
import raphael.jeu.Coup.TypeCoup;

/**
 * Représente l'état "logique" du jeu :
 * <ul> <li> Le plateau<li>
 * 		<li> Le coup précédent<li>
 * 		<li> L'état précédent<li>
 * 		<li> Le trait actuel<li>
 * 		<li> Les roques<li> </ul>
 * 
 * L'état correspond à un noeud dans les algos d'exploration.
 *
 */
public class Etat implements Noeud {
	
	private static final long TRAIT = Constantes.GENERATOR.nextLong();
	private static final long PRB = Constantes.GENERATOR.nextLong();
	private static final long PRN = Constantes.GENERATOR.nextLong();
	private static final long GRB = Constantes.GENERATOR.nextLong();
	private static final long GRN = Constantes.GENERATOR.nextLong();
	
	private CouleurPiece trait;			/* A qui de jouer */
	private Plateau plateau;
	
	private boolean petitRoqueBlanc;	/* Vrai si faisables */
	private boolean petitRoqueNoir;		/* Faux sinon */
	private boolean grandRoqueBlanc;
	private boolean grandRoqueNoir;
	
	private Coup 	coupPrecedent;
	private Etat	etatPrecedent;

	private boolean	evaluationNoireCalculee;	/* Si deja calculée, on la stocke	*/
	private int		evaluationNoireValue;	/* afin de ne pas la recalculer		*/
	private boolean	evaluationBlancheCalculee;	/* Si deja calculée, on la stocke	*/
	private int		evaluationBlancheValue;	/* afin de ne pas la recalculer		*/
	private boolean	pieceBalanceCalculee;
	private int		sommePiecesBlanches;
	private int		sommePiecesNoires;
		
	/****************** Constructeurs *****************/
	
	/**
	 * État par défaut au début de jeu
	 */
	public Etat() {
		this(new Plateau(), Constantes.COULEUR_DEFAUT);
		this.petitRoqueBlanc = true;
		this.petitRoqueNoir  = true;
		this.grandRoqueBlanc = true;
		this.grandRoqueNoir  = true;
		plateau.zobristXOR(GRB);
		plateau.zobristXOR(GRN);
		plateau.zobristXOR(PRB);
		plateau.zobristXOR(PRN);
		plateau.zobristXOR(TRAIT);
	}
	
	public Etat(Plateau plateau, CouleurPiece couleur) {
		this.trait = couleur;
		this.plateau = plateau;
		this.plateau.setEtat(this);
	}
	
	public Etat(String fen) {
    	String[] chunks = fen.split(" ");
    	
    	plateau = new Plateau(chunks[0]);
		this.plateau.setEtat(this);
    	
    	if      (chunks[1].equals("w")) trait = CouleurPiece.BLANC;
        else if (chunks[1].equals("b")) trait = CouleurPiece.NOIR;
        else
            throw new IllegalArgumentException("Malformatted fen string: expected 'to play' as second field but found ");
    	
    	
    	if (chunks[2].equals("-")) {
    		petitRoqueBlanc = false;
    		grandRoqueBlanc = false;
    		petitRoqueNoir = false;
    		grandRoqueNoir = false;
    	}
    	else {
    		for (int i = 0; i < chunks[2].length(); i++) {
				char c = chunks[2].charAt(i);
				switch(c) {
				case 'K': petitRoqueBlanc = true;
				case 'Q': grandRoqueBlanc = true;
				case 'k': petitRoqueNoir = true;
				case 'q': grandRoqueNoir = true;
				}
			}
    	}
		plateau.zobristXOR(GRB);
		plateau.zobristXOR(GRN);
		plateau.zobristXOR(PRB);
		plateau.zobristXOR(PRN);
		plateau.zobristXOR(TRAIT);
	}
	
	/**
	 * Constructeur de copie (deep copy) d'un état existant
	 * 
	 * @param etat	L'état à partir duquel on créé la copie
	 * @param coup	Le coup qui a mené ici
	 */
	public Etat(Etat etat, Coup coup) {
		this(new Plateau(etat.getPlateau(), 
				etat.getTrait() == CouleurPiece.BLANC && !coup.is(TypeCoup.NORMAL),
				etat.getTrait() == CouleurPiece.NOIR && !coup.is(TypeCoup.NORMAL)
			), etat.getTrait());
		this.petitRoqueBlanc = etat.isPetitRoqueBlanc();
		this.petitRoqueNoir  = etat.isPetitRoqueNoir();
		this.grandRoqueBlanc = etat.isGrandRoqueBlanc();
		this.grandRoqueNoir  = etat.isGrandRoqueNoir();
		this.coupPrecedent = coup;
		this.etatPrecedent = etat;
	}
	
	/**
	 * Raccourcis
	 * @return Vrai si le joueur est en échec, faux sinon
	 */
	public boolean enEchec() {
		return plateau.enEchec(trait);
	}
	
	/**
	 * Il y a echec et mat et le joueur est en echec
	 * et n'a plus de choix possibles.
	 * 
	 * @return Vrai si le joueur qui a le trait est echec et mat
	 */
	public boolean echecEtMat() {
		return enEchec() && plateau.calculerCoups(trait, true).isEmpty();
	}
	
	public boolean pat() {
		return !enEchec() && plateau.calculerCoups(trait, true).isEmpty();
	}
	
	public boolean threefold() {
		Etat ancien = this;
		int allerRetoursComplets = 0;
		int i = 0;
		while(allerRetoursComplets < 4) {
			if(ancien.coupPrecedent == null)
				return false;
			if(i++ % 4 == 0) {
				allerRetoursComplets++;
				if(!ancien.getPlateau().equals(this.getPlateau()))
					return false;
			}
			ancien = ancien.etatPrecedent;
			
		}
		return true;
	}
	
	/**
	 * Calcule la liste des coups précédents qui ont menés jusqu'ici.
	 * 
	 * @return la lliste des coups précédents.
	 */
	public List<Coup> coupsPrecedents() {
		Etat ancien = this;
		List<Coup> liste = new ArrayList<>();
		while (ancien.coupPrecedent != null) {
			liste.add(ancien.coupPrecedent);
			ancien = ancien.etatPrecedent;
		} 
		return liste;
	}

	
	/**
	 * Nombre de coup depuis le début de partie.
	 * Un peu plus rapide que la coupsPrecedents().size()
	 * 
	 * @return le nombre de coups depuis le début de la partie
	 */
	public int getNumeroDeCoup() {
		Etat ancien = etatPrecedent;
		int num = 0;
		for(; ancien != null; num++)
			ancien = ancien.etatPrecedent;
		return num;
	}

	
	/***** Getter / Setter *****/
	
	public CouleurPiece getTrait() { return trait; }
	public CouleurPiece changerTrait() { plateau.zobristXOR(TRAIT); return trait = trait.oppose(); }
	
	public Plateau 		getPlateau() { return plateau; }

	public Coup 		getCoupPrecedent() { return coupPrecedent; }
	public Etat 		getEtatPrecedent() { return etatPrecedent; }

	public boolean 	isPetitRoqueBlanc() {	return petitRoqueBlanc; }
	public boolean 	isPetitRoqueNoir()  {	return petitRoqueNoir; }
	public boolean 	isGrandRoqueBlanc() {	return grandRoqueBlanc; }
	public boolean 	isGrandRoqueNoir()  {	return grandRoqueNoir; }
	public void 	annulerPetitRoqueBlanc() {	petitRoqueBlanc = false; plateau.zobristXOR(PRB); }
	public void 	annulerPetitRoqueNoir()  {	petitRoqueNoir  = false; plateau.zobristXOR(GRN); }
	public void 	annulerGrandRoqueBlanc() {	grandRoqueBlanc = false; plateau.zobristXOR(GRB); }
	public void 	annulerGrandRoqueNoir()  {	grandRoqueNoir  = false; plateau.zobristXOR(GRN); }


	
	/****** Implémentation des méthodes de Noeud *******/
	
	
	@Override
	public ListeDeNoeuds<Etat> successeurs() {
		ListeDeNoeuds<Etat> succ = new ListeDEtats();
		ListeDeCoups liste = plateau.calculerCoups(trait, true);
		
		for (int i = 0, max = liste.size(); i < max; i++) {
			Etat e = liste.get(i).jouer(this);
			if(e != this)
				succ.add(e);
		}
		return succ;
	}
	
	@Override
	public Noeud parent() {
		return etatPrecedent;
	}
	

	@Override
	public boolean estTerminal() {
		getPieceBalance(CouleurPiece.BLANC);
		return echecEtMat()
				|| threefold()
				|| (sommePiecesBlanches < 450 && sommePiecesNoires < 450)
				|| pat();
	}
	
	public void calcPieceBalance() {
		if(pieceBalanceCalculee) return;
		int max = plateau.getCases().length;
		
		for (int i = 0; i < max; i++) {
			int piece = plateau.getCase(i);
			if(piece == 0)
				continue;
			else if(Piece.getCouleur(piece) == CouleurPiece.BLANC)
				sommePiecesBlanches += Piece.getEvalValue(piece);
			else
				sommePiecesNoires += Piece.getEvalValue(piece);
		}
		pieceBalanceCalculee = true;
	}
	
	public int getPieceBalance(CouleurPiece joueur) {
		if(!pieceBalanceCalculee)
			calcPieceBalance();
		
		return CouleurPiece.BLANC == joueur ?
				sommePiecesBlanches - sommePiecesNoires
				: sommePiecesNoires - sommePiecesBlanches;
	}

	@Override
	public int evaluation(Joueur joueur) {

		if(evaluationBlancheCalculee && joueur == CouleurPiece.BLANC)
			return evaluationBlancheValue;
		else if(evaluationNoireCalculee && joueur == CouleurPiece.NOIR)
			return evaluationNoireValue;
		
		int total = 0;
		
		/* Initialisation des vars */
		CouleurPiece maCouleur = (CouleurPiece) joueur;
		ListeDeCoups coupsAdv = plateau.calculerCoups(maCouleur.oppose(), false);
		ListeDeCoups coupsPerso = plateau.calculerCoups(maCouleur, false);
		int coupsPersoLen = coupsPerso.size();
		int coupsAdvLen = coupsAdv.size();
		int avancement = getNumeroDeCoup();
		boolean debutJeu = avancement < 15;
		boolean finJeu = avancement > 20;
		
//		if(debutJeu) {
		int q = 0;
		for(int i = 0; i < 64 && q <= 9; i++)
			if(plateau.getCase(i) != 0)
				q++;

		debutJeu = debutJeu && q > 8;
		finJeu = finJeu || q < 7;
//		}
		
		if(echecEtMat()) {
			int ret = trait == maCouleur ? -99999 : 99999;
			for (Coup coup : coupsPrecedents()) {
				System.out.print(coup.toString() + " - ");
			}
			System.out.println("enleve " + 10 * avancement + " -> " + getFEN() + "(" + (ret - 10 * avancement)+ ")");
			ret -= 10 * avancement;
			return ret;
		}
		if(threefold()) {
			int ret = getPieceBalance(maCouleur) > 0 ? -99999 : 99999;
			ret -= 10 * avancement;
			return ret;
		}
		if(pat()) {
			int ret = getPieceBalance(maCouleur) > 0 ? -99999 : 99999;
			ret -= 10 * avancement;
			return ret;
		}

		/**** Avantage des pièces ********/
		total += getPieceBalance(maCouleur);
		
		/**** Tactique fin de jeu ****/
		if(finJeu) {
			if(getPieceBalance(maCouleur) > 400)
				/**** Mettre le roi dans un coin [0; 56] ****/
				total +=
					7 * Math.abs(3 - Utilitaire.indexToRow(plateau.getPositionRoi(maCouleur.oppose())))
					+ 7 * Math.abs(3 - Utilitaire.indexToColumn(plateau.getPositionRoi(maCouleur.oppose())));
			
			/**** Accentuer ce parametre ***/
			total += coupsPersoLen - coupsAdvLen;
			
		}
		
		/**** Avantage lorsqu'on a plus de choix de coups [-15; 15]********/
		total += coupsPersoLen - coupsAdvLen;
		
		
		/**** Avancer en début de game [2; 15]********/
		if(debutJeu) {
			for (int i = 0; i < coupsPersoLen; i++) {
				Coup coup = coupsPerso.get(i);
				if(maCouleur == CouleurPiece.BLANC && coup.getFrom() > coup.getTo()) 
					total += Piece.getEvalValue(plateau.getCase(coup.getFrom())) * Constantes.COEF_AVANCEMENT_PIECES;
				if(maCouleur == CouleurPiece.NOIR && coup.getFrom() < coup.getTo()) 
					total += Piece.getEvalValue(plateau.getCase(coup.getFrom())) * Constantes.COEF_AVANCEMENT_PIECES;
			}
		}

		/*** Controle du centre si début de game [+-100] ***/
		if(debutJeu) {
			//cases du centre : 27, 28, 35, 36
			total += Constantes.COEF_CONTROLE_CENTRE * (plateau.nbDAttaques(27, maCouleur) - plateau.nbDAttaques(27, maCouleur.oppose()));
			total += Constantes.COEF_CONTROLE_CENTRE * (plateau.nbDAttaques(28, maCouleur) - plateau.nbDAttaques(28, maCouleur.oppose()));
			total += Constantes.COEF_CONTROLE_CENTRE * (plateau.nbDAttaques(35, maCouleur) - plateau.nbDAttaques(35, maCouleur.oppose()));
			total += Constantes.COEF_CONTROLE_CENTRE * (plateau.nbDAttaques(36, maCouleur) - plateau.nbDAttaques(36, maCouleur.oppose()));
		}
		
		/*** Importance du roque en début de game [-60; 100]***/
		if(debutJeu) {
			if(maCouleur == CouleurPiece.BLANC) {
				if(petitRoqueNoir || grandRoqueNoir)
					total -= Constantes.COEF_EMPECHER_ROQUE_ENNEMI;
				if(petitRoqueBlanc || grandRoqueBlanc)
					total += Constantes.COEF_ROQUER;
			}
			else if(maCouleur == CouleurPiece.NOIR) {
				if(petitRoqueNoir || grandRoqueNoir)
					total += Constantes.COEF_ROQUER;
				if(petitRoqueBlanc || grandRoqueBlanc)
					total -= Constantes.COEF_EMPECHER_ROQUE_ENNEMI;
			}
		}
		
		/*** Pas bouger la dame en début de game ***/
		if(debutJeu) {
			boolean dameBlanche = (0x2F & plateau.getCase(59)) == 1 && Piece.getCouleur(plateau.getCase(59)) == CouleurPiece.BLANC;
			boolean dameNoire = (0x2F & plateau.getCase(3)) == 1 && Piece.getCouleur(plateau.getCase(3)) == CouleurPiece.NOIR;
			if(maCouleur == CouleurPiece.NOIR) {
				total += Constantes.COEF_FORCER_DAME * (dameNoire ? 1 : -1);
				total += Constantes.COEF_FORCER_DAME_ENNEMIE_A_BOUGER * (dameBlanche ? -1 : 1);
			}
			else {
				total += Constantes.COEF_FORCER_DAME * (dameBlanche ? 1 : -1);
				total += Constantes.COEF_FORCER_DAME_ENNEMIE_A_BOUGER * (dameNoire ? -1 : 1);
			}
		}
		
		/***
		 * Début : Pas de fous devant les pions centraux
		 * Tout le temps : Attention pion dame
		 * ***/
		for(int i = 8; i < 16; i++) {
			if(maCouleur == CouleurPiece.NOIR) {
				if(	(0x2F & plateau.getCase(i)) == 32) {  // si pion
					if(Piece.getCouleur(plateau.getCase(i)) == CouleurPiece.NOIR) {
						// si pion noir et fou noir au dessous
						if(debutJeu && (0x2F & plateau.getCase(i + 8)) == 8 && Piece.getCouleur(plateau.getCase(i + 8)) == CouleurPiece.NOIR) {
							total -= 40;
						}
					}
					else { // C'est un pion blanc
						total -= 200;
					}
				}
			}
			else {
				// si pion blanc
				if(	(0x2F & plateau.getCase(i)) == 32 && Piece.getCouleur(plateau.getCase(i)) == CouleurPiece.BLANC)  
					total += 100;
			}
		}
		for(int i = 40; i < 48; i++) {
			if(maCouleur == CouleurPiece.BLANC) {
				if(	(0x2F & plateau.getCase(i+8)) == 32) {  // si pion
					if(Piece.getCouleur(plateau.getCase(i+8)) == CouleurPiece.BLANC) {
						// si pion blanc et fou blanc au dessus
						if(debutJeu && (0x2F & plateau.getCase(i)) == 8 && Piece.getCouleur(plateau.getCase(i)) == CouleurPiece.BLANC)
							total -= 40;
					}
					else { // C'est un pion noir
						total -= 200;
					}
				}
			}
			else {
				// si pion noir
				if(	(0x2F & plateau.getCase(i)) == 32 && Piece.getCouleur(plateau.getCase(i)) == CouleurPiece.BLANC)  
					total += 100;
			}
		}

		if(joueur == CouleurPiece.BLANC) {
			evaluationBlancheValue = total;
			evaluationBlancheCalculee = true;
		}
		else if(joueur == CouleurPiece.NOIR) {
			evaluationNoireValue = total;
			evaluationNoireCalculee = true;
		}
		return total;
	}

	
	@Override
	public String toString() {
		return coupPrecedent == null ? "null" : coupPrecedent.toString();
//		StringBuffer sb = new StringBuffer();
//		sb.append("Plateau : ").append(System.lineSeparator())
//		  .append(plateau.toString())
//		  .append("Trait :  ").append(trait.name());
//		return sb.toString();
	}
	
	public String print() {
		if(getCoupPrecedent() != null)
			return getCoupPrecedent().toString();
		return "";
	}
	
    public String getFEN() {
    	StringBuffer sb = new StringBuffer(plateau.getFEN());
    	sb.append(' ').append(trait == CouleurPiece.BLANC ? 'w' : 'b');
    	
    	sb.append(' ');
    	if(!petitRoqueBlanc && !grandRoqueBlanc && 
		   !petitRoqueNoir && !grandRoqueNoir)
    		sb.append("-");
    	else {
    		if(petitRoqueBlanc) sb.append("K");
    		if(grandRoqueBlanc) sb.append("Q");
    		if(petitRoqueNoir) sb.append("k");
    		if(grandRoqueBlanc) sb.append("q");
    	}
        sb.append(" - 0 1");
    	
    	return sb.toString();
    }


	@Override
	public long hash() {
		return plateau.getZobristHash();
	}
}
