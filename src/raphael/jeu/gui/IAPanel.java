package raphael.jeu.gui;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;
import raphael.algo.AlphaBeta;
import raphael.algo.AlphaBetaMemory;
import raphael.algo.Constantes;
import raphael.algo.IterativeDeepeningAlphaBeta;
import raphael.algo.MTDf;
import raphael.algo.MTDfFixedTime;
import raphael.algo.Minimax;
import raphael.algo.structures.Algorithme;

public class IAPanel extends FlowPane {
	private static Algorithme algoChoisi;
	private int profondeur;
	private Label tempsExecution;
	private Label profondeurLabel;

	public IAPanel(Board board) {
		super(Orientation.VERTICAL);
		getStyleClass().addAll("bordered");
		
		Label tempsMax = new Label("Temps d'execution maximum en secondes");
		TextField tempsMaxIn = new TextField("10");

		Button autoPlay = new Button("IA, Joue !");
		autoPlay.getStyleClass().add("autoplay");
		autoPlay.setOnAction((ActionEvent event) -> {
			try {
				Constantes.TIMEOUT_MILLIS = 1000 * Integer.parseInt(tempsMaxIn.getText());
				setTempsExecution(board.autoPlay(algoChoisi, profondeur));
			} catch(NumberFormatException e) {
				tempsMaxIn.setText("Format de nombre invalide");
			}
		});

		tempsExecution = new Label();

		getChildren().addAll(ChoixGroupe.instance(), new ChoixProfondeur(), tempsMax, tempsMaxIn, autoPlay, tempsExecution);
		setAlgoChoisi(ChoixGroupe.instance().getSelection());
	}

	/**
	 * Classe comprise du groupe de boutons et du label permettant
	 * de choisir la profondeur maximale.
	 */
	private class ChoixProfondeur extends FlowPane {
		public ChoixProfondeur() {
			super(Orientation.HORIZONTAL);

			profondeurLabel = new Label();
			Button plus = new Button("+");
			Button moins = new Button("-");
			plus.setOnAction((ActionEvent event) -> {
				setProfondeur(++profondeur);
			});
			moins.setOnAction((ActionEvent event) -> {
				setProfondeur(--profondeur);
			});
			setProfondeur(3);
			this.getChildren().addAll(profondeurLabel, moins, plus);
		}
	}



	
	/**
	 * Boutons radios pour choisir l'algo
	 */
	private static class ChoixGroupe extends FlowPane {
		private static ChoixGroupe instance;
		ToggleGroup toggleGroup;
		List<AlgoRadioButton> listeAlgorithmes;

		private ChoixGroupe() {
			super(Orientation.VERTICAL);
			toggleGroup = new ToggleGroup();
			listeAlgorithmes = new ArrayList<AlgoRadioButton>();

			ajoutAlgo("MiniMax", Minimax.class, toggleGroup, true);
			ajoutAlgo("Alpha-Bêta Memory", AlphaBetaMemory.class, toggleGroup);
			ajoutAlgo("Alpha-Bêta", AlphaBeta.class, toggleGroup);
			ajoutAlgo("Alpha-Bêta itératif", IterativeDeepeningAlphaBeta.class, toggleGroup);
			ajoutAlgo("MTD-f profondeur", MTDf.class, toggleGroup);
			ajoutAlgo("MTD-f temps", MTDfFixedTime.class, toggleGroup);

			toggleGroup.selectedToggleProperty()
					.addListener((ObservableValue<? extends Toggle> observable, Toggle o, Toggle n) -> {
						setAlgoChoisi(getSelection());
					});
			getChildren().addAll(listeAlgorithmes);

			setPrefHeight(25 * listeAlgorithmes.size());
			setVgap(5);
		}

		private Algorithme getSelection() {
			return SmallFactory.get((String) toggleGroup.getSelectedToggle().getUserData());
		}
		private List<AlgoRadioButton> getAlgorithmes() {
			return listeAlgorithmes;
		}
		public static ChoixGroupe instance() {
			if(instance == null)
				instance = new ChoixGroupe();
			return instance;
		}
		private void ajoutAlgo(String name, Class<? extends Algorithme> algo, ToggleGroup groupe ) {
			ajoutAlgo(name, algo, groupe, false);
		}
		private void ajoutAlgo(String name, Class<? extends Algorithme> algo, ToggleGroup groupe, boolean selected) {
			listeAlgorithmes.add(new AlgoRadioButton(name, algo, groupe, selected));
		}
		
	}

	/**
	 * Factory String --> Algo
	 */
	private static class SmallFactory {
		public static Algorithme get(String algoName) {
			Class<? extends Algorithme> classe = null;
			for (AlgoRadioButton btn: ChoixGroupe.instance().getAlgorithmes())
				if(btn.equals((Object) algoName))
					classe = btn.getAlgo();

			if (classe == null)
				System.err.println("No algo found for " + algoName);
			
			try {
				return classe.getConstructor().newInstance();
			} catch (ReflectiveOperationException e) {
				return null;
			}
		}
	}

	private void setProfondeur(int profondeur) {
		this.profondeur = profondeur;
		profondeurLabel.setText("Profondeur : " + profondeur + "  ");
	}

	private void setTempsExecution(double temps) {
		tempsExecution.setText("Exécuté en " + temps + " secondes.");
	}

	private static void setAlgoChoisi(Algorithme algo) {
		algoChoisi = algo;
	}

}
