package wolox.training.factories;

import wolox.training.constants.TestsConstants;
import wolox.training.models.Book;

public class BookTestFactory {

	public static Book getBook(String type) {
		Book book = new Book();
		book.setImage("http://someurl.com");
		book.setGenre("Book genre");
		book.setPages(500);
		book.setPublisher("Some publisher");
		book.setTitle("Book title");
		book.setSubtitle("Book subtitle");
		book.setYear("2000");
		book.setIsbn("Some isbn");
		book.setAuthor("Book author");

		switch (type) {
			case TestsConstants.UPDATE_FACTORY_REQUEST:
				book.setId(1);
				break;
			case TestsConstants.ERROR_FACTORY_REQUEST:
				book.setYear("20000");
				break;
			case TestsConstants.BOOK_WITHOUT_GENRE:
				book.setGenre(null);
				break;
			case TestsConstants.BOOK_DIFFERENT_PUBLISHER:
				book.setPublisher("Different publisher");
				break;
			case TestsConstants.BOOK_DIFFERENT_YEAR:
				book.setYear("1999");
				break;
			case TestsConstants.BOOK_DIFFERENT_GENRE:
				book.setGenre("Different genre");
				break;
		}
		return book;
	}
}
