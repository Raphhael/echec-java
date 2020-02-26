package raphael.jeu.gui;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;
import raphael.algo.Algorithme;
import raphael.algo.AlphaBeta;
import raphael.algo.Minimax;

public class IAPanel extends FlowPane{
	private Algorithme  algoChoisi;
	private int			profondeur;
	private Label		tempsExecution;
	private Label		profondeurLabel;
	
	public IAPanel(Board board) {
		super(Orientation.HORIZONTAL);

		Button autoPlay = new Button("IA, Joue !");
		autoPlay.setStyle("-fx-font-size: " + (int)(ContainerPane.ZOOM * 30) + "; ");
		autoPlay.setOnAction((ActionEvent event) -> {
			setTempsExecution(board.autoPlay(algoChoisi, profondeur));
		});

		tempsExecution = new Label();
		profondeurLabel = new Label();
		Button plus = new Button("+");
		Button moins = new Button("-");
		Button precedent = new Button("Précédent");
		plus .setOnAction((ActionEvent event) -> { setProfondeur(++profondeur); });
		moins.setOnAction((ActionEvent event) -> { setProfondeur(--profondeur); });
		precedent.setOnAction((ActionEvent event) -> { board.precedent(); });
		setProfondeur(3);
				
		getChildren().addAll(precedent, new ChoixGroupe(), profondeurLabel,
							 moins, plus, autoPlay, tempsExecution);

	}
	
	/**
	 * Boutons radios pour choisir l'algo
	 */
	private class ChoixGroupe extends FlowPane {
		ToggleGroup toggleGroup;
		
		public ChoixGroupe() {
			super(Orientation.VERTICAL);
			toggleGroup = new ToggleGroup();
			
			RadioButton minimax = new RadioButton("MiniMax");
			minimax.setToggleGroup(toggleGroup);
			minimax.setUserData("minimax");
			 
			RadioButton ab = new RadioButton("Alpha-Bêta");
			ab.setToggleGroup(toggleGroup);
			ab.setSelected(true);
			ab.setUserData("alpha-beta");
			
			toggleGroup.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> observable, Toggle o, Toggle n) -> {
				setAlgoChoisi(getSelection());				
			});
			setAlgoChoisi(getSelection());
			getChildren().addAll(minimax, ab);
			
			setPrefWrapLength(BASELINE_OFFSET_SAME_AS_HEIGHT);
			setAlignment(Pos.CENTER);
		}
		
		private Algorithme getSelection() {
			return SmallFactory.get((String) toggleGroup.getSelectedToggle().getUserData());
		}
	}
	
	/**
	 * Factory String --> Algo
	 */
	private static class SmallFactory {
		public static Algorithme get(String algoName) {
			Class<? extends Algorithme> classe;

			if(algoName.equals("minimax"))	classe = Minimax.class;
			else							classe = AlphaBeta.class;
			
			try {
				return classe.getConstructor().newInstance();
			} catch (ReflectiveOperationException e) {
				return null;
			}
		}
	}
	
	private void setProfondeur(int profondeur) {
		this.profondeur = profondeur;
		profondeurLabel.setText("Profondeur : " + profondeur);
	}
	
	private void setTempsExecution(double temps) {
		tempsExecution.setText("Exécuté en " + temps + " secondes.");
	}
	
	private void setAlgoChoisi(Algorithme algo) {
		algoChoisi = algo;
	}

}
