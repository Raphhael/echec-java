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
import raphael.algo.Algorithme;
import raphael.jeu.CouleurPiece;
import raphael.jeu.Coup;
import raphael.jeu.Etat;
import raphael.jeu.ListeDeCoups;
import raphael.jeu.Piece;
import raphael.jeu.Plateau;

/**
 * Échéquier avec les cases et boutons
 */
public class Board extends GridPane {
	private Etat etat = new Etat();				// Etat du jeu
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
				Piece piece = etat.getPlateau().getCase(i * 8 + j);
				Button btn = btnListe[8 * i + j];
				if (piece != null) {
					btn.setText(String.valueOf(piece.getClass().getSimpleName().charAt(0)));
					btn.setTextFill(piece.getCouleur() == CouleurPiece.NOIR ? Color.BLACK : Color.WHITE);
				}
				else
					btn.setText("");
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
	 */
	public double autoPlay(Algorithme algo, int profondeur) {
		long t1 = System.currentTimeMillis();
		etat = (Etat) algo.start(etat, profondeur, etat.getTrait());
		long t2 = System.currentTimeMillis();
		pieceSelectionnee = false;
		syncEtat();
		System.out.println("Compteur1 = " + Plateau.compteur1);
		System.out.println("Compteur2 = " + Plateau.compteur2);
		System.out.println("CompteurFrom = " + ListeDeCoups.compteurFrom);
		System.out.println("CompteurTo = " + ListeDeCoups.compteurTo);
		

//		System.out.println("Calcul1 = " + Etat.calcul1);
//		System.out.println("Calcul2 = " + Etat.calcul2);
		
		return ((double)(t2 - t1)) / 1000.;
	}

	private void drawBoard() {
		int size = (int)(ContainerPane.ZOOM * 100);
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				Button btn = btnListe[8 * i + j];
				btn.setMaxSize(size, size);
				btn.setMinSize(size, size);
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
					if(etat.echecEtMat()) {
						System.out.println("FINI !");
					}
					System.out.println("HashCode = " + etat.hash());
//					autoPlay();
					// System.out.println("ETAT : " + etat.evaluation());
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
}
