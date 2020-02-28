package raphael.jeu;

import java.util.List;

/**
 * Représente le plateau (niveau logique)
 * Constitiué d'un tableau de Piece (null quand pas de pieces)
 * Un plateau est lié à un Etat.
 */
public class Plateau {
	private int[]	 cases;	/* Tableau de pièces au format entier */
	private Etat	 etat;
	
	private ListeDeCoups listeCoupsBlancs;		/*	On sauvegarde les		 */
	private ListeDeCoups listeCoupsNoirs;		/*	listes de coups déjà	 */
	private ListeDeCoups listeCoupsBlancsNoDeep;/*	calculées car c'est une	 */
	private ListeDeCoups listeCoupsNoirsNoDeep;	/*	opération hyper couteuse */
	
	private int			 positionRoiBlanc = -1;	/* Position des rois màj que */
	private int			 positionRoiNoir = -1;	/* lors d'un coups spécial	 */
	
	public static int compteur1 = 0;
	public static int compteur2 = 0;
	
	private long zobristTable[][];
	private long zobristHash;

	
	/******************** Constructeurs *********************/
	
	public Plateau(int [] cases) {
		this.cases = cases;
	}
	
	/*
	 * Si on ne lui passe rien il charge un plateau par défaut
	 */
	public Plateau() {
		Piece []pieces = Constantes.PLATEAU_DEFAUT;
		cases = new int[pieces.length];
		
		zobristTable = new long[64][12];
		zobristHash = 0;
		
		for (int i = 0; i < cases.length; i++) {
			for(int k = 0; k < 12; k++)
				zobristTable[i][k] = Constantes.GENERATOR.nextLong();
			
			if(pieces[i] != null) {
				cases[i] = Piece.makePiece(pieces[i]);
				cases[i] = Piece.setPosition(cases[i], i);
				zobristHash ^= zobristTable[i][Piece.getZobriestValue(cases[i])];
			}
		}
		System.out.println("Code = " + zobristHash);
	}
	
	/*
	 * Équivalent au clone() mais plus souple
	 * 
	 * On passe aussi l'information sur est ce que le roi a bougé, grâce à 
	 * quoi on peut éviter aussi de recalculer sa position.
	 */
	public Plateau(Plateau plateau, boolean needMajRoiBlanc, boolean needMajRoiNoir) {
		this.cases = plateau.cases.clone();
		if(!needMajRoiBlanc)
			this.positionRoiBlanc = plateau.positionRoiBlanc;
		if(!needMajRoiNoir)
			this.positionRoiNoir = plateau.positionRoiNoir;
		
		this.zobristHash = plateau.zobristHash;
		this.zobristTable = plateau.zobristTable;
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
			if(cases[i] == 0) 
				continue;
			
			if(Piece.getCouleur(cases[i]) == couleur) {
				List<Coup> listePiece = Piece.listeCoups(this, cases[i], goDeep);
				
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
			if(Piece.getCouleur(cases[i]) == couleur && (cases[i] & 1) == 1) {

				if(couleur == CouleurPiece.BLANC)
					positionRoiBlanc = i;
				else
					positionRoiNoir = i;
				return i;
			}
		}
		
		return -1;
	}
	
	/**
	 * Met à jour la table de zobrist en fonction de l'état d'une case.
	 * La case ne doit pas être vide !
	 * 
	 * @param caseIdx	L'indice de la case
	 */
	private void zobristXOR(int caseIdx) {
		zobristHash ^= zobristTable[caseIdx][Piece.getZobriestValue(cases[caseIdx])];
	}
	public void zobristXOR(long value) {
		zobristHash ^= value;
	}
	
	/****** Getters and setters *******/

	public int getPositionRoiNoir() {  return positionRoiNoir; }
	public int getPositionRoiBlanc() { return positionRoiBlanc; }

	public long getZobristHash() {   return zobristHash; }
	public void setZobristHash(long hash) { zobristHash = hash;	}

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
	
	public Etat getEtat() {	return etat; }
	public void setEtat(Etat etat) { this.etat = etat; }
	
	public int[] getCases() {	  return cases; }
	public int   getCase(int i) { return cases[i]; }
	
	/**
	 * Place une piece à une certaine position
	 * 
	 * Si il existe déjà une piece à la position d'arrivée, on appelle 
	 * removeCase en cette cases afin de la préparer.
	 * 
	 * Toutes les mises à jour pour le hachage sont effectuées.
	 * La mise à jour de la position de la pièce est également effectuée.
	 * 
	 * @param i		La case de destination
	 * @param piece La piece à bouger
	 */
	public void    setCase(int i, int piece) {
		removeCase(i);
		cases[i] = piece;
		zobristXOR(i);
		Piece.setPosition(piece, i);
	}
	
	/**
	 * Supprimer la piece d'une case en effectuant la mise à jour
	 * du hash.
	 * 
	 * @param i		l'indice de la case à "vider"
	 */
	public void removeCase(int i) {
		if(cases[i] != 0)
			zobristXOR(i);
		cases[i] = 0;
	}

	
	/**************************** DÉBUG **************************************/
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		int i = 0;
		for (; i < cases.length; i++) {
			
			sb.append(Piece.toSmallString(cases[i])).append(" ");
			
			if((i+1)%8 == 0)
				sb.append(System.lineSeparator());
		}
		return sb.toString();
	}
}
