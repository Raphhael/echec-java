package raphael.jeu.gui;

import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import raphael.algo.structures.Algorithme;

/**
 * Radiobutton personnalisé
 */
class AlgoRadioButton extends RadioButton {
	private Class<? extends Algorithme> algo;
	private String name;

	public AlgoRadioButton(String name, Class<? extends Algorithme> algo, ToggleGroup groupe, boolean selected) {
		super(name);
		this.name = name;
		this.algo = algo;
		setToggleGroup(groupe);
		setUserData(name);
		setSelected(selected);
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof String && obj.equals(name);
	}

	public Class<? extends Algorithme> getAlgo() {
		return algo;
	}

}