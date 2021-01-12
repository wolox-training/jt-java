package wolox.training.constants;

public final class ExceptionsConstants {

	private ExceptionsConstants() {
	}

	///// BOOKS
	public static final String BOOK_NOT_FOUND = "Requested book has not been found";
	public static final String BOOK_ALREADY_OWNED = "The user %s already owns book %s";
	public static final String USER_NOT_FOUND = "Requested user has not been found";
	public static final String ID_MISMATCH = "Provided param id and structure id doesn't match";
	public static final String DATA_SAVE_INTEGRITY_VIOLATION = "An error occurred while trying to save data";
	public static final String ACTION_NOT_FOUND = "The specified action has not been found";
	public static final String MODIFY_BOOK_LIST_PARAMETHER_NOT_PRESENT = "The required method needs a path paramether \"action\"";
}
