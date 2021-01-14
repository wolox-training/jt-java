package wolox.training.models;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

}
