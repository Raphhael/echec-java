package raphael.jeu;

import raphael.jeu.pieces.Roi;

/**
 * Représente le plateau (niveau logique)
 * Constitiué d'un tableau de Piece (null quand pas de pieces)
 * Un plateau est lié à un Etat.
 */
public class Plateau {
	private Piece []	 cases;
	private Etat		 etat;
	
	private ListeDeCoups listeCoupsBlancs;		/*	On sauvegarde les		 */
	private ListeDeCoups listeCoupsNoirs;		/*	listes de coups déjà	 */
	private ListeDeCoups listeCoupsBlancsNoDeep;/*	calculées car c'est une	 */
	private ListeDeCoups listeCoupsNoirsNoDeep;	/*	opération hyper couteuse */
	
	private int			 positionRoiBlanc = -1;	/* Position des rois màj que */
	private int			 positionRoiNoir = -1;	/* lors d'un coups spécial	 */
	
	public static int compteur1 = 0;
	public static int compteur2 = 0;

	
	/******************** Constructeurs *********************/
	
	public Plateau(Piece [] cases) {
		this.cases = cases;
	}
	
	/*
	 * Si on ne lui passe rien il charge un plateau par défaut
	 */
	public Plateau() {
		this(Constantes.PLATEAU_DEFAUT);
		
		for (int i = 0; i < cases.length; i++)
			if(cases[i] != null) {
				cases[i].setPosition(i);
				cases[i].setPlateau(this);
			}
	}
	
	/*
	 * Équivalent au clone() mais plus souple
	 */
	public Plateau(Plateau plateau, boolean majRoiBlanc, boolean majRoiNoir) {
		Piece []copie = new Piece[plateau.getCases().length];
		for (int i = 0; i < copie.length; i++) {
			if (plateau.getCase(i) != null) {
				copie[i] = (Piece) plateau.getCase(i).makeCopy();
				copie[i].setPlateau(this);
			}
		}
		this.listeCoupsBlancs = null;
		this.listeCoupsNoirs = null;
		this.cases = copie;
		if(!majRoiBlanc)
			positionRoiBlanc = plateau.getPositionRoi(CouleurPiece.BLANC);
		if(!majRoiNoir)
			positionRoiNoir = plateau.getPositionRoi(CouleurPiece.NOIR);
	}
	
	
	/**
	 * 
	 * Calculer tous les coups possibles pour une couleur donnée
	 * 
	 * Le flag goDeep permet d'éviter les boucles infinies, car il y a 
	 * des appels récursifs dans calculerCoups. Ainsi on peut faire :
	 *   <code>calculerCoup(BLANC, true)</code> 
	 *   puis <code>calculerCoup(NOIR, false)</code>
	 *   
	 * Le résultat des 4 combinaisons possibles (NOIR / BLANC / true / false)
	 * sont stockées dans les attributs <code>listeCoups[couleur][flag]</code>
	 * 
	 * 
	 * @param 	couleur	Couleur des coups à calculer
	 * @param 	goDeep	si vrai, on fait les appels récursifs, sinon non
	 * @return			La liste des coups possibles
	 */
	public ListeDeCoups calculerCoups(CouleurPiece couleur, boolean goDeep) {
		if(couleur == CouleurPiece.BLANC) {
			if(goDeep && listeCoupsBlancs != null) {return listeCoupsBlancs;}
			else if(!goDeep && listeCoupsBlancsNoDeep != null) { compteur2++; return listeCoupsBlancsNoDeep;}
		}
		else {
			if(goDeep && listeCoupsNoirs != null) return listeCoupsNoirs;
			else if(!goDeep && listeCoupsNoirsNoDeep != null) return listeCoupsNoirsNoDeep;
		}
		
		ListeDeCoups coups  = new ListeDeCoups();
		
		for (int i = 0; i < getCases().length; i++) {
			Piece p = getCase(i);
			if(p == null) 
				continue;
			
			if(p.getCouleur() == couleur) {
				for (Coup coup : p.listeCoups(goDeep)) {
					if(goDeep) {
						// Si le flag est activé, on fais une simulation du coup :
						// On joue, et si on voit que ça marche on ajoute le coup
						// TROP COUTEUX...
						if(coup.jouer(getEtat()) != getEtat())
							coups.add(coup);
					}
					else
						coups.add(coup);
				}
			}
		}
		
		if(couleur == CouleurPiece.BLANC) {
			if(goDeep) listeCoupsBlancs = coups;
			else listeCoupsBlancsNoDeep = coups;
		}
		else {
			if(goDeep) listeCoupsNoirs = coups;
			else listeCoupsNoirsNoDeep = coups;
		}
		
		return coups;
	}

	/**
	 * Le joueur est en échec si son roi est attaqué par l'ennemi.
	 * 
	 * @param	couleur 	Couleur de la personne qui est ou pas en echec
	 * @return				Vrai si la personne est en echec, faux sinon
	 */
	public boolean enEchec(CouleurPiece couleur) {
		return estAttaquee(getPositionRoi(couleur), couleur.oppose());
	}
	
	/**
	 * Ligne d'une pièce dont on connait la position dans un tableau
	 * à une seule dimension.
	 * 
	 * @param 	i la position dans un tableau unidimentionnel.
	 * @return	la ligne
	 */
	public static int indexToRow(int i) {
		return (int)(i / 8);
	}
	
	/**
	 * Ligne d'une pièce dont on connait la position dans un tableau
	 * à une seule dimension.
	 * 
	 * @param 	i la position dans un tableau unidimentionnel.
	 * @return	la ligne
	 */
	public static int indexToColumn(int i) {
		return i - 8 * (int)(i / 8);
	}


	
	/**
	 * Est-ce que cette case est attaquée par une couleur ?
	 * 
	 * @param indiceCase	indice de la case
	 * @param couleur		couleur qui devrait ou pas attaquer cette case
	 * @return				vrai si couleur attaque indiceCase, faux sinon
	 */
	public boolean estAttaquee(int indiceCase, CouleurPiece couleur) {
		return nbDAttaques(indiceCase, couleur) > 0;
	}
	
	/**
	 * Combien d'attaques sur cette case ?
	 * 
	 * @param indiceCase	Case dont on veut savoir le nombre d'attaques
	 * @param couleur		Couleur du joueur qui attaque
	 * @return				Le nombre d'attaques
	 */
	public int nbDAttaques(int indiceCase, CouleurPiece couleur) {
		return 	  calculerCoups(couleur, false)
				  .chercherCoupsTo(indiceCase)
				  .size();
	}

	/**
	 * Retrouver où est le roi ?
	 * On utilise la variable positionRoi[couleur] si elle est différente de
	 * -1 pour ne pas tout recalculer.
	 * 
	 * @param	couleur	Couleur du roi à trouver
	 * @return			La position [0; 64[
	 */
	public int getPositionRoi(CouleurPiece couleur) {
		if(couleur == CouleurPiece.BLANC && positionRoiBlanc != -1)
			return positionRoiBlanc;
		if(couleur == CouleurPiece.NOIR && positionRoiNoir != -1)
			return positionRoiNoir;
		
		
		for (int i = 0; i < cases.length; i++) {
			if(cases[i] != null && cases[i].getCouleur() == couleur
					&& cases[i] instanceof Roi)
				return i;
		}
		return -2;
	}
	
	/****** Getters and setters *******/

	public Etat getEtat() { return etat; }
	public void setEtat(Etat etat) { this.etat = etat; }
	
	public Piece[] getCases() { return cases; }
	public Piece   getCase(int i) { return cases[i]; }
	public void    setCase(int i, Piece piece) {
		cases[i] = piece;
		piece.setPosition(i);
	}
	public void removeCase(int i) {
		cases[i] = null;
	}

	
	
	/**************************** DÉBUG **************************************/
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		int i = 0;
		for (Piece piece : cases) {
			sb.append(piece == null ? " " : piece.getClass().getSimpleName().charAt(0))
			  .append(" ");
			
			if(++i%8 == 0)
				sb.append(System.lineSeparator());
		}
		return sb.toString();
	}
	
	public String toStringFull() {
		StringBuffer sb = new StringBuffer();
		int i = 0;
		for (Piece piece : cases) {
			sb.append(piece == null ? "null" : piece.toString())
			  .append(" ");
			
			if(++i%8 == 0)
				sb.append(System.lineSeparator());
		}
		return sb.toString();
	}
}
