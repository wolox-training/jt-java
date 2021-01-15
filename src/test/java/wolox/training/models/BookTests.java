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
	void givenPublisherAndYearAndGenre_whenSearchBooks_thenReturnBooks() {

		String searchPublisher = TestsConstants.BOOK_TEST_PUBLISHER;
		String searchYear = TestsConstants.BOOK_TEST_YEAR;
		String searchGenre = TestsConstants.BOOK_TEST_GENRE;

		Book normalBook = BookTestFactory.getBook(TestsConstants.SIMPLE_FACTORY_REQUEST);
		Book diffPublisherBook = BookTestFactory.getBook(TestsConstants.BOOK_DIFFERENT_PUBLISHER);
		Book diffYearBook = BookTestFactory.getBook(TestsConstants.BOOK_DIFFERENT_YEAR);
		Book diffGenreBook = BookTestFactory.getBook(TestsConstants.BOOK_DIFFERENT_GENRE);
		Book nullGenreBook = BookTestFactory.getBook(TestsConstants.BOOK_WITHOUT_GENRE);

		entityManager.persist(normalBook);
		entityManager.persist(diffPublisherBook);
		entityManager.persist(diffYearBook);
		entityManager.persist(diffGenreBook);
		entityManager.persist(nullGenreBook);
		entityManager.flush();

		List<Book> fullParametersList = Collections.singletonList(normalBook);
		assertThat(bookRepository.findAllByPublisherAndYearAndGenre(searchPublisher, searchYear, searchGenre))
				.isEqualTo(fullParametersList);

		List<Book> noParametersList = Arrays.asList(normalBook, diffPublisherBook, diffYearBook, diffGenreBook, nullGenreBook);
		assertThat(bookRepository.findAllByPublisherAndYearAndGenre(null, null, null))
				.isEqualTo(noParametersList);

		List<Book> noGenreParameterList = Arrays.asList(normalBook, diffGenreBook, nullGenreBook);
		assertThat(bookRepository.findAllByPublisherAndYearAndGenre(searchPublisher, searchYear, null))
				.isEqualTo(noGenreParameterList);

		List<Book> noPublisherParameterList = Arrays.asList(normalBook, diffPublisherBook);
		assertThat(bookRepository.findAllByPublisherAndYearAndGenre(null, searchYear, searchGenre))
				.isEqualTo(noPublisherParameterList);

		List<Book> noYearParameterList = Arrays.asList(normalBook, diffYearBook);
		assertThat(bookRepository.findAllByPublisherAndYearAndGenre(searchPublisher, null, searchGenre))
				.isEqualTo(noYearParameterList);
	}

	@Test
	void givenParameters_whenFindAll_thenReturnBooks() {
		String searchGenre = TestsConstants.BOOK_TEST_GENRE;
		String searchAuthor = TestsConstants.BOOK_TEST_AUTHOR;
		String searchImage = TestsConstants.BOOK_TEST_IMAGE;
		String searchTitle = TestsConstants.BOOK_TEST_TITLE;
		String searchSubtitle = TestsConstants.BOOK_TEST_SUBTITLE;
		String searchPublisher = TestsConstants.BOOK_TEST_PUBLISHER;
		String searchYear = TestsConstants.BOOK_TEST_YEAR;
		int searchPages = TestsConstants.BOOK_TEST_PAGES;
		String searchIsbn = TestsConstants.BOOK_TEST_ISBN;

		Book normalBook = BookTestFactory.getBook(TestsConstants.SIMPLE_FACTORY_REQUEST);
		Book diffPublisherBook = BookTestFactory.getBook(TestsConstants.BOOK_DIFFERENT_PUBLISHER);
		Book diffYearBook = BookTestFactory.getBook(TestsConstants.BOOK_DIFFERENT_YEAR);
		Book diffGenreBook = BookTestFactory.getBook(TestsConstants.BOOK_DIFFERENT_GENRE);
		Book nullGenreBook = BookTestFactory.getBook(TestsConstants.BOOK_WITHOUT_GENRE);
		Book diffAuthorBook = BookTestFactory.getBook(TestsConstants.BOOK_DIFFERENT_AUTHOR);
		Book diffImageBook = BookTestFactory.getBook(TestsConstants.BOOK_DIFFERENT_IMAGE);
		Book diffTitleBook = BookTestFactory.getBook(TestsConstants.BOOK_DIFFERENT_TITLE);
		Book diffSubtitleBook = BookTestFactory.getBook(TestsConstants.BOOK_DIFFERENT_SUBTITLE);
		Book diffPagesBook = BookTestFactory.getBook(TestsConstants.BOOK_DIFFERENT_PAGES);
		Book diffIsbnBook = BookTestFactory.getBook(TestsConstants.BOOK_DIFFERENT_ISBN);

		entityManager.persist(normalBook);
		entityManager.persist(diffPublisherBook);
		entityManager.persist(diffYearBook);
		entityManager.persist(diffGenreBook);
		entityManager.persist(nullGenreBook);
		entityManager.persist(diffAuthorBook);
		entityManager.persist(diffImageBook);
		entityManager.persist(diffTitleBook);
		entityManager.persist(diffSubtitleBook);
		entityManager.persist(diffPagesBook);
		entityManager.persist(diffIsbnBook);
		entityManager.flush();

		List<Book> filterByGenreList = Arrays.asList(normalBook, diffPublisherBook, diffYearBook, diffAuthorBook,
				diffImageBook, diffTitleBook, diffSubtitleBook, diffPagesBook, diffIsbnBook);
		assertThat(bookRepository.findAll(TestsConstants.BOOK_TEST_GENRE, null, null,
				null, null, null, null, 0, null))
				.isEqualTo(filterByGenreList);

		List<Book> filterByAuthorList = Collections.singletonList(diffAuthorBook);
		assertThat(bookRepository.findAll(null, TestsConstants.BOOK_TEST_AUTHOR, null,
				null, null, null, null, 0, null))
				.isEqualTo(filterByAuthorList);

		List<Book> filterByPagesList = Collections.singletonList(diffPagesBook);
		assertThat(bookRepository.findAll(null, null, null,
				null, null, null, null, TestsConstants.BOOK_TEST_PAGES, null))
				.isEqualTo(filterByPagesList);

		List<Book> filterByPublisherAndYearList = Collections.singletonList(diffYearBook);
		assertThat(bookRepository.findAll(null, null, null,
				null, null, TestsConstants.BOOK_TEST_PUBLISHER, TestsConstants.BOOK_TEST_DIFFERENT_YEAR, 0, null))
				.isEqualTo(filterByPublisherAndYearList);
	}

}
