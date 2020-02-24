package raphael.jeu;

import java.util.List;

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
	 * 
	 * On passe aussi l'information sur est ce que le roi a bougé, grâce à 
	 * quoi on peut éviter aussi de recalculer sa position.
	 */
	public Plateau(Plateau plateau, boolean needMajRoiBlanc, boolean needMajRoiNoir) {
		Piece []copie = new Piece[plateau.getCases().length];
		for (int i = 0; i < copie.length; i++) {
			if (plateau.getCase(i) != null) {
				copie[i] = (Piece) plateau.getCase(i).makeCopy();
				copie[i].setPlateau(this);
			}
		}
		if(!needMajRoiBlanc)
			this.positionRoiBlanc = plateau.getPositionRoiBlanc();
		if(!needMajRoiNoir)
			this.positionRoiNoir = plateau.getPositionRoiNoir();
		
		this.cases = copie;
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
			if(listeCoupsBlancs != null) return listeCoupsBlancs;
			else if(!goDeep && listeCoupsBlancsNoDeep != null) return listeCoupsBlancsNoDeep;
		}
		else {
			if(listeCoupsNoirs != null) return listeCoupsNoirs;
			else if(!goDeep && listeCoupsNoirsNoDeep != null) return listeCoupsNoirsNoDeep;
		}
		
		ListeDeCoups coups  = new ListeDeCoups();
		
		for (int i = 0; i < cases.length; i++) {
			Piece p = cases[i];
			if(p == null) 
				continue;
			
			if(p.getCouleur() == couleur) {
				List<Coup> listePiece = p.listeCoups(goDeep);
				
				if(!goDeep) {
					coups.addAll(listePiece);
				}
				else {
					// Si le flag est activé, on fait une simulation du coup :
					// On joue, et si on voit que ça marche on ajoute le coup
					for (int j = 0, max = listePiece.size(); j < max; j++) {
						Coup coup = listePiece.get(j);
						if(coup.isValid(etat))
							coups.add(coup);
					}
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
	 * 
	 * On utilise la variable positionRoi[couleur] pour ne pas à recalculer
	 * à chaque fois la valeur.
	 * 
	 * @param	couleur	Couleur du roi à trouver
	 * @return			La position [0; 64[
	 */
	public int getPositionRoi(CouleurPiece couleur) {
		if(couleur == CouleurPiece.BLANC && positionRoiBlanc != -1)
			return positionRoiBlanc;
		else if(couleur == CouleurPiece.NOIR && positionRoiNoir != -1)
			return positionRoiNoir;;
		
		for (int i = 0; i < cases.length; i++) {
			if(cases[i] != null && cases[i].getCouleur() == couleur
												&& cases[i] instanceof Roi) {

				if(couleur == CouleurPiece.BLANC)
					positionRoiBlanc = i;
				else
					positionRoiNoir = i;
				return i;
			}
		}
		
		return -1;
	}
	
	/****** Getters and setters *******/

	public int getPositionRoiNoir() {  return positionRoiNoir; }
	public int getPositionRoiBlanc() { return positionRoiBlanc; }

	public ListeDeCoups getListeDeCoupNoirs(boolean deep) {
		return deep ? listeCoupsNoirs : listeCoupsNoirsNoDeep;
	}
	public ListeDeCoups getListeDeCoupBlancs(boolean deep) {
		return deep ? listeCoupsBlancs : listeCoupsBlancsNoDeep;
	}
	public void setListeDeCoupNoirs(ListeDeCoups liste, boolean deep) {
		if(deep)	listeCoupsNoirs = liste;
		else		listeCoupsNoirsNoDeep = liste;
	}
	public void setListeDeCoupBlancs(ListeDeCoups liste, boolean deep) {
		if(deep)	listeCoupsBlancs = liste;
		else		listeCoupsBlancsNoDeep = liste;
	}
	
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
