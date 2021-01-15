package wolox.training.models;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import wolox.training.constants.TestsConstants;
import wolox.training.factories.BookTestFactory;
import wolox.training.repositories.BookRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
class BookTests {

	@Autowired
	private TestEntityManager entityManager;
	@Autowired
	private BookRepository bookRepository;

	private Book book;

	@BeforeEach
	public void init() {
		this.book = BookTestFactory.getBook(TestsConstants.SIMPLE_FACTORY_REQUEST);
	}

	@Test
	void givenBook_whenCreate_thenSuccess() {
		entityManager.persist(book);
		entityManager.flush();

		Optional<Book> newBook = bookRepository.findTopByAuthor("Book author");

		assertThat(newBook).isNotEmpty();
		assertThat(newBook.get().getTitle()).isEqualTo(book.getTitle());
	}

	@Test
	void givenBook_whenCreate_thenPreconditionsException() {
		assertThrows(IllegalArgumentException.class, () -> book.setTitle(null));
	}

	@Test
	void givenPublisherAndYearAndGenre_whenFindBooks_thenReturnBooks() {

		String searchPublisher = TestsConstants.BOOK_TEST_PUBLISHER;
		String searchYear = TestsConstants.BOOK_TEST_YEAR;
		String searchGenre = TestsConstants.BOOK_TEST_GENRE;

		Book fullBook = BookTestFactory.getBook(TestsConstants.SIMPLE_FACTORY_REQUEST);
		Book diffPublisherBook = BookTestFactory.getBook(TestsConstants.BOOK_DIFFERENT_PUBLISHER);
		Book diffYearBook = BookTestFactory.getBook(TestsConstants.BOOK_DIFFERENT_YEAR);
		Book diffGenreBook = BookTestFactory.getBook(TestsConstants.BOOK_DIFFERENT_GENRE);
		Book nullGenreBook = BookTestFactory.getBook(TestsConstants.BOOK_WITHOUT_GENRE);

		entityManager.persist(fullBook);
		entityManager.persist(diffPublisherBook);
		entityManager.persist(diffYearBook);
		entityManager.persist(diffGenreBook);
		entityManager.persist(nullGenreBook);
		entityManager.flush();

		List<Book> fullParametersList = Collections.singletonList(fullBook);
		assertThat(bookRepository.findAllByPublisherAndYearAndGenre(searchPublisher, searchYear, searchGenre))
				.isEqualTo(fullParametersList);

		List<Book> noParametersList = Arrays.asList(fullBook, diffPublisherBook, diffYearBook, diffGenreBook, nullGenreBook);
		assertThat(bookRepository.findAllByPublisherAndYearAndGenre(null, null, null))
				.isEqualTo(noParametersList);

		List<Book> noGenreParameterList = Arrays.asList(fullBook, diffGenreBook, nullGenreBook);
		assertThat(bookRepository.findAllByPublisherAndYearAndGenre(searchPublisher, searchYear, null))
				.isEqualTo(noGenreParameterList);

		List<Book> noPublisherParameterList = Arrays.asList(fullBook, diffPublisherBook);
		assertThat(bookRepository.findAllByPublisherAndYearAndGenre(null, searchYear, searchGenre))
				.isEqualTo(noPublisherParameterList);

		List<Book> noYearParameterList = Arrays.asList(fullBook, diffYearBook);
		assertThat(bookRepository.findAllByPublisherAndYearAndGenre(searchPublisher, null, searchGenre))
				.isEqualTo(noYearParameterList);
	}

}
