package raphael.jeu.gui;

import java.util.List;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import raphael.algo.AlgorithmeException;
import raphael.algo.Metrique;
import raphael.algo.structures.Algorithme;
import raphael.jeu.Constantes;
import raphael.jeu.Coup;
import raphael.jeu.Etat;
import raphael.jeu.ListeDeCoups;
import raphael.jeu.Piece;
import raphael.jeu.Plateau;

/**
 * Échéquier avec les cases et boutons
 */
public class Board extends GridPane {
	private Etat etat = new Etat(Constantes.START);				// Etat du jeu
	private Button[] btnListe;					// Cases
	
	private boolean    pieceSelectionnee = false;
	private int 	   indexPieceSelectionnee;
	private List<Coup> listeCoupsSelection;
	
	public Board() {
		btnListe = new Button[8 * 8];
		for(int i = 0; i < 8*8; i++)
			btnListe[i] = new Button();
		
		drawBoard();
		syncEtat();
	}

	/**
	 * Synchroniser les éléments de l'échéquier avec l'état courant
	 */
	private void syncEtat() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				int piece = etat.getPlateau().getCase(i * 8 + j);
				Button btn = btnListe[8 * i + j];
				btn.getStyleClass().clear();
				
				if (piece != 0) {
					btn.getStyleClass().addAll("case", "case_"+Piece.toFENString(piece));
				}
				else
					btn.getStyleClass().addAll("case", "case_vide");
			}
		}
	}
	
	public void precedent() {
		Etat precendent = etat.getEtatPrecedent();
		if(precendent != null) {
			etat = precendent;
			syncEtat();
		}
	}
	
	/**
	 * Demander à l'IA de jouer
	 * 
	 * @param algo			L'algo à utiliser pour l'IA
	 * @param profondeur	La profondeur si besoin
	 * 
	 * @return le temps d'execution de l'algo
	 */
	public double autoPlay(Algorithme algo) {
//		new Thread(() -> {
//    		for(int i = 0; i < 20; i++) {
//	            Platform.runLater(() -> {
//	    			long t1 = System.currentTimeMillis();
//	    			etat = (Etat) algo.start(etat, profondeur, etat.getTrait());
//	    			long t2 = System.currentTimeMillis();
//	    			System.out.println((((double)(t2 - t1)) / 1000.));
//	    			syncEtat();
//	            });
//	            try {
//					Thread.sleep(200);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//    		}
//	    }).start();
//		return 1.;
		long t1 = System.currentTimeMillis();
		try {
			etat = (Etat) algo.initAndRun(etat);
		} catch (AlgorithmeException e) {
			System.err.println("Une erreur est survenue : "
								+ e.getClass().getSimpleName()
								+ " -> " + e.getMessage());
		}
		long t2 = System.currentTimeMillis();
		pieceSelectionnee = false;
		syncEtat();
		
		/*----- DEBUG -----*/
		System.out.println("CompteurTo = " + ListeDeCoups.compteurTo);
		System.out.println("Nb nouveaux plateaux = " + Plateau.metric_1);
		System.out.println(new Metrique());
		Metrique.reset();
		/*----- END DEBUG -----*/
		
		return ((double)(t2 - t1)) / 1000.;
	}

	private void drawBoard() {
		int size = (int)(ContainerPane.ZOOM * 100);
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				Button btn = btnListe[8 * i + j];
				btn.setMaxSize(size, size);
				btn.setMinSize(size, size);
				btn.getStyleClass().set(0, "case");
				btn.setStyle(
						"-fx-font-size: " + (int)(ContainerPane.ZOOM * 30) + "; "
						+ "-fx-background-color: #" + (((i % 2 == 0 ? 0 : 1) + j) % 2 == 0 ? "bababa" : "6e6e6e") + "; ");
				getChildren().add(btn);
				setRowIndex(btn, i);
				setColumnIndex(btn, j);
				
				btn.setOnAction((ActionEvent e) -> {
					buttonAction((Button) e.getSource());
				});
			}
		}
	}
	
	private int findIndexButton(Button button) {
		for(int i = 0; i < 8 * 8; i++) {
			if(button == btnListe[i])
				return i;
		}
		return -1;
	}
	
	private void buttonAction(Button btn) {
		indexPieceSelectionnee = findIndexButton(btn);
		if(!pieceSelectionnee) {
			listeCoupsSelection = etat.getPlateau()
									  .calculerCoups(etat.getTrait(), true)
									  .chercherCoupsFrom(indexPieceSelectionnee);
			if(listeCoupsSelection.size() > 0) {
				pieceSelectionnee = true;
				setBorder(btnListe[indexPieceSelectionnee], Color.YELLOW);
				for (Coup coup : listeCoupsSelection)
					setBorder(btnListe[coup.getTo()], Color.RED);
			}
			else {
				cleanSelection();
				pieceSelectionnee = false;
			}
		}
		else {
			cleanSelection();
			for (Coup coup : listeCoupsSelection) {
				if(coup.getTo() == indexPieceSelectionnee) {
					
					etat = coup.jouer(etat);
					syncEtat();
					pieceSelectionnee = false;
					if(etat.estTerminal())
						System.out.println("FINI !");
				}
			}
			if(pieceSelectionnee) { // Mauvais déplacement apres selection
				pieceSelectionnee = false;
				buttonAction(btn);
			}
		}
	}
	
	private void setBorder(Button btn, Color color) {
		btn.setBorder(new Border(new BorderStroke(color, BorderStrokeStyle.SOLID, null, new BorderWidths(5))));
	}
	
	private void cleanSelection() {
		for (Button button : btnListe) {
			button.setBorder(null);
		}
	}
	
	public Etat getEtat() {
		return etat;
	}
	
	public void setEtat(Etat etat) {
		this.etat = etat;
		syncEtat();
	}
}
