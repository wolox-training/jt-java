package wolox.training.models;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import wolox.training.constants.ExceptionsConstants;
import wolox.training.exceptions.BookAlreadyOwnedException;

@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;

	@NotNull(message = "User username cannot be null")
	@Column(nullable = false)
	private String username;

	@NotNull(message = "User name cannot be null")
	@Column(nullable = false)
	private String name;

	@NotNull(message = "User birthdate cannot be null")
	@Column(nullable = false)
	private Date birthdate;

	@ManyToMany
	@JoinTable(
			name = "user_book",
			joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id")
	)
	private List<Book> books;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<Book> getBooks() {
		return Collections.unmodifiableList(books);
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}

	/**
	 * Add a book to the list of books of the user
	 * @param book: The {@link Book} that's going to be added
	 * @throws BookAlreadyOwnedException: When the book that's being added already is owned by the user
	 */
	public void addBook(Book book) throws BookAlreadyOwnedException {
		if (books.contains(book)) {
			throw new BookAlreadyOwnedException(String.format(ExceptionsConstants.BOOK_ALREADY_OWNED, id, book.getId()));
		}
		books.add(book);
	}

	/**
	 * Removes the specified {@link Book} from the list of books
	 * @param book: The {@link Book} that's going to be removed
	 */
	public void removeBook(Book book) {
		this.books.remove(book);
	}
}
