package wolox.training.constants;

public final class ExceptionsConstants {

	private ExceptionsConstants() {
	}

	public static final String BOOK_NOT_FOUND = "Requested book has not been found";
	public static final String ID_MISMATCH = "Provided param id and Book id doesn't match";
	public static final String DATA_SAVE_INTEGRITY_VIOLATION = "An error ocurred while trying to save data";
	public static final String BOOK_ALREADY_OWNED = "The user %s already owns book %s";
}
