package raphael.jeu;

import java.util.ArrayList;
import java.util.List;

import raphael.algo.Joueur;
import raphael.algo.Noeud;
import raphael.jeu.Coup.TypeCoup;
import raphael.jeu.pieces.Dame;

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
	private CouleurPiece trait;			/* A qui de jouer */
	private Plateau plateau;
	
	private boolean petitRoqueBlanc;	/* Vrai si faisables */
	private boolean petitRoqueNoir;		/* Faux sinon */
	private boolean grandRoqueBlanc;
	private boolean grandRoqueNoir;
	
	private Coup 	coupPrecedent;
	private Etat	etatPrecedent;
	
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
	}
	
	public Etat(Plateau plateau, CouleurPiece couleur) {
		this.trait = couleur;
		this.plateau = plateau;
		this.plateau.setEtat(this);
	}
	
	/**
	 * Constructeur de copie (deep copy) d'un état existant
	 * 
	 * @param etat	L'état à partir duquel on créé la copie
	 * @param coup	Le coup qui a mené ici
	 */
	public Etat(Etat etat, Coup coup) {
		this(new Plateau(
					etat.getPlateau(),
/* Le roi B ou */	etat.getTrait() == CouleurPiece.BLANC && coup.is(TypeCoup.NORMAL),
/* N à bougé ? */	etat.getTrait() == CouleurPiece.NOIR && coup.is(TypeCoup.NORMAL)
			),etat.getTrait());
		
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
	
	/**
	 * Calcule la liste des coups précédents qui ont menés jusqu'ici.
	 * 
	 * @return la lliste des coups précédents.
	 */
	public List<Coup> coupsPrecedents() {
		Etat ancien = this;
		List<Coup> liste = new ArrayList<>();
		do {
			liste.add(ancien.coupPrecedent);
			ancien = ancien.etatPrecedent;
		} while (ancien.coupPrecedent != null);
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
		for(; ancien!= null; num++)
			ancien = ancien.etatPrecedent;
		return num;
	}

	
	/***** Getter / Setter *****/
	
	public CouleurPiece getTrait() { return trait; }
	public CouleurPiece changerTrait() { return trait = trait.oppose(); }
	
	public Plateau 		getPlateau() { return plateau; }

	public Coup 		getCoupPrecedent() { return coupPrecedent; }

	public boolean 	isPetitRoqueBlanc() {	return petitRoqueBlanc; }
	public boolean 	isPetitRoqueNoir()  {	return petitRoqueNoir; }
	public boolean 	isGrandRoqueBlanc() {	return grandRoqueBlanc; }
	public boolean 	isGrandRoqueNoir()  {	return grandRoqueNoir; }
	public void 	annulerPetitRoqueBlanc() {	petitRoqueBlanc = false; }
	public void 	annulerPetitRoqueNoir()  {	petitRoqueNoir  = false; }
	public void 	annulerGrandRoqueBlanc() {	grandRoqueBlanc = false; }
	public void 	annulerGrandRoqueNoir()  {	grandRoqueNoir  = false; }

	
	
	/****** Implémentation des méthodes de Noeud *******/

	@Override
	public List<Noeud> successeurs() {
		List<Noeud> succ = new ArrayList<Noeud>();
		for (Coup coup : plateau.calculerCoups(trait, true)) {
			Etat e = coup.jouer(this);
			if(e != this)
				succ.add(e);
		}
		return succ;
	}

	@Override
	public boolean estTerminal() {
		return echecEtMat();
	}

	@Override
	public int evaluation(Joueur joueur) {
		int total = 0;
		
		/**** Avantage des pièces ********/
		for (Piece piece : plateau.getCases()) {
			if(piece == null)
				continue;
			if(piece.getCouleur() == joueur)
				total += piece.getValue();
			else
				total -= piece.getValue();
		}
		
		/* Initialisation des vars */
		CouleurPiece maCouleur = (CouleurPiece) joueur;
		ListeDeCoups coupsAdv = plateau.calculerCoups(maCouleur.oppose(), false);
		ListeDeCoups coupsPerso = plateau.calculerCoups(maCouleur, false);
		int avancement = getNumeroDeCoup();
		
		
		/**** Avantage lorsqu'on a plus de choix de coups [-15; 15]********/
		total = coupsPerso.size() - coupsAdv.size();
		
		
		/**** Avancer en début de game [2; 15]********/
		if(avancement < 15) {
			for (Coup coup : coupsPerso) {
				if(maCouleur == CouleurPiece.BLANC && coup.getFrom() > coup.getTo()) 
					total += plateau.getCase(coup.getFrom()).getValue() * Constantes.COEF_AVANCEMENT_PIECES;
				if(maCouleur == CouleurPiece.NOIR && coup.getFrom() < coup.getTo()) 
					total += plateau.getCase(coup.getFrom()).getValue() * Constantes.COEF_AVANCEMENT_PIECES;
			}
		}
		
		/*** Controle du centre si début de game [+-100] ***/
		if(avancement < 15) {
			//cases du centre : 27, 28, 35, 36
			total += Constantes.COEF_CONTROLE_CENTRE * (plateau.nbDAttaques(27, maCouleur) - plateau.nbDAttaques(27, maCouleur.oppose()));
			total += Constantes.COEF_CONTROLE_CENTRE * (plateau.nbDAttaques(27, maCouleur) - plateau.nbDAttaques(27, maCouleur.oppose()));
			total += Constantes.COEF_CONTROLE_CENTRE * (plateau.nbDAttaques(27, maCouleur) - plateau.nbDAttaques(27, maCouleur.oppose()));
			total += Constantes.COEF_CONTROLE_CENTRE * (plateau.nbDAttaques(27, maCouleur) - plateau.nbDAttaques(27, maCouleur.oppose()));
		}
		
		/*** Importance du roque en début de game [-60; 100]***/
		if(avancement < 20) {
			byte finiRoque = 0x00;
			for (Coup coup : coupsPrecedents()) {
				if(coup == null)
					continue;
				if(coup.is(TypeCoup.ROQUE_PETIT_BLANC) || coup.is(TypeCoup.ROQUE_GRAND_BLANC)) {
					total += Constantes.COEF_ROQUER * (maCouleur == CouleurPiece.BLANC ? 1 : -Constantes.COEF_EMPECHER_ROQUE_ENNEMI);
					finiRoque ++;
				}
				if(coup.is(TypeCoup.ROQUE_PETIT_NOIR) || coup.is(TypeCoup.ROQUE_GRAND_NOIR)) {
					total += Constantes.COEF_ROQUER * (maCouleur == CouleurPiece.NOIR ? 1 : -Constantes.COEF_EMPECHER_ROQUE_ENNEMI);
					finiRoque ++;
				}
				if(finiRoque >= 0x02)
					break;
			}
		}
		
		/*** Pas bouger la dame en début de game***/
		if(avancement < 16) {
			boolean dameBlanche = plateau.getCase(59) instanceof Dame && plateau.getCase(59).getCouleur() == CouleurPiece.BLANC;
			boolean dameNoire = plateau.getCase(3) instanceof Dame && plateau.getCase(3).getCouleur() == CouleurPiece.NOIR;
			if(maCouleur == CouleurPiece.NOIR)
				total += (dameNoire ? 1 : -1) * Constantes.COEF_FORCER_DAME + 
						 (dameBlanche ? -1 : 1) * Constantes.COEF_FORCER_DAME_ENNEMIE_A_BOUGER;
			else
				total += (dameBlanche ? 1 : -1) * Constantes.COEF_FORCER_DAME + 
						 (dameNoire ? -1 : 1) * Constantes.COEF_FORCER_DAME_ENNEMIE_A_BOUGER;
		}
		
		
		/*** Pas de fous devant les pions centraux ***/
		// TODO : fous devant pions centraux
		
		
		return total;
	}
	
	

	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Plateau : ").append(System.lineSeparator())
		  .append(plateau.toString())
		  .append("Trait :  ").append(trait.name());
		return sb.toString();
	}
}
