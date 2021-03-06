package raphael.jeu;

import raphael.algo.structures.Joueur;
import raphael.algo.structures.ListeDeNoeuds;

public class ListeDEtats extends ListeDeNoeuds<Etat>{
	private static final long serialVersionUID = 1790518454383628313L;

	@Override
	public void sort(Joueur joueur) {
		this.sort((e1, e2) -> {
			int balance_1 = e1.getPieceBalance() * (joueur == CouleurPiece.BLANC ? 1 : -1);
			int balance_2 = e2.getPieceBalance() * (joueur == CouleurPiece.BLANC ? 1 : -1);
			if(balance_1 > balance_2)
				return 1;
			else if(balance_1 < balance_2)
				return -1;
			else {
				int row1 = Math.abs(Utilitaire.indexToRow(e1.getCoupPrecedent().getFrom()));
				int row2 = Math.abs(Utilitaire.indexToRow(e2.getCoupPrecedent().getFrom()));
				int col1 = Math.abs(Utilitaire.indexToColumn(e1.getCoupPrecedent().getFrom()) - 4);
				int col2 = Math.abs(Utilitaire.indexToColumn(e2.getCoupPrecedent().getFrom()) - 4);
				if(row1 == 1 || row1 == 6) {
					if(row2 != 1 && row2 != 6)
						return 1;
				}
				else {
					if(row2 == 1 || row2 == 6)
						return -1;
				}
				return col2 == col1 ? 0 : (col2 < col1 ? 1 : -1);
			}
		});
	}
}
