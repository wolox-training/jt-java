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

		this.book = new Book();
		this.book.setImage("http://someurl.com");
		this.book.setGenre("Book genre");
		this.book.setPages(500);
		this.book.setPublisher("Some publisher");
		this.book.setTitle("Book title");
		this.book.setSubtitle("Book subtitle");
		this.book.setYear("2000");
		this.book.setIsbn("Some isbn");
		this.book.setAuthor("Book author");
	
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
