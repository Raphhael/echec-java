package raphael.algo;

public class AlgorithmeException extends Exception {
	private static final long serialVersionUID = 7198552889369543689L;
	
	public AlgorithmeException(String message) {
		super(message);
	}
	
	public static class NoMoreChoicesException extends AlgorithmeException {
		private static final long serialVersionUID = 5106240539562784774L;

		public NoMoreChoicesException() {
			super("Algorithme lancé mais aucuns choix disponibles "
					+ "(profondeur de 0, déjà dans un état final...)");
		}
	}
}
