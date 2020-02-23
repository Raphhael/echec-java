package raphael.jeu;

import java.util.ArrayList;
import java.util.List;

public class ListeDeCoups extends ArrayList<Coup> implements List<Coup> {
	private static final long serialVersionUID = 6090708960341022931L;
	
	public static int compteurFrom = 0;
	public static int compteurTo = 0;
	
	private ListeDeCoups dejaCalcule[] = new ListeDeCoups[64];

	/**
	 * Chercher le nombre de coups commencant par un point
	 * dans la liste courante.
	 * 
	 * @param	indice	L'indice de départ des coups
	 * @return			La liste des coups commencant par indice
	 */
	public ListeDeCoups chercherCoupsFrom(int indice) {
		ListeDeCoups selection = new ListeDeCoups();
		compteurFrom++;
		for (int i = 0; i < size(); i++) {
			Coup coup = get(i);
			if(coup.getFrom() == indice)
				selection.add(coup);
		}
		
		return selection;
	}

	/**
	 * Chercher le nombre de coups se terminant à un point.
	 * 
	 * Étant donné que cette fonction est énormément utilisée pour une 
	 * même liste de coups, on sauvegarde le résultat dans l'attribut
	 * <code>dejaCalcule</code> à l'indice "indice".
	 * Ainsi, on commencera par rechercher dans cet indice avant de faire 
	 * tout le calcul.
	 * 
	 * @param	indice	L'indice d'arrivée des coups
	 * @return			La liste des coups finissant par indice
	 */
	public ListeDeCoups chercherCoupsTo(int indice) {
		ListeDeCoups selection = dejaCalcule[indice];
		if(selection != null) {
			compteurTo++;
			return selection;
		}
		
		selection = new ListeDeCoups();
		for (int i = 0; i < size(); i++) {
			Coup coup = get(i);
			if(coup.getTo() == indice)
				selection.add(coup);
		}
		
		dejaCalcule[indice] = selection;
		return selection;
	}
}
