package wolox.training.models;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.google.common.base.Strings;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import wolox.training.constants.ExceptionsConstants;
import wolox.training.constants.PreconditionsConstants;
import wolox.training.exceptions.BookAlreadyOwnedException;

@Entity
@Table(name = "users")
@ApiModel
@JsonInclude(Include.NON_NULL)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Data
@NoArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@ApiModelProperty(notes = "Unique id, provided by the database")
	private int id;

	@NotNull(message = "User username cannot be null")
	@Column(nullable = false)
	@ApiModelProperty(notes = "User's unique username", required = true)
	private String username;

	@NotNull(message = "User name cannot be null")
	@Column(nullable = false)
	@ApiModelProperty(notes = "User's name", required = true)
	private String name;

	@NotNull(message = "User password cannot be null")
	@Column(nullable = false)
	@ApiModelProperty(notes = "User's password", required = true)
	private String password;

	@NotNull(message = "User birthdate cannot be null")
	@Column(nullable = false)
	@ApiModelProperty(notes = "User's day of birth", required = true)
	@JsonFormat(pattern = "yyyy-MM-dd")
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	@NonNull
	private LocalDate birthdate;

	@ManyToMany
	@ApiModelProperty(notes = "Books: Books assigned to the user", required = true)
	private List<Book> books;

	public void setName(String name) {
		checkArgument(!Strings.isNullOrEmpty(name), PreconditionsConstants.NAME_CANT_BE_NULL);
		this.name = name;
	}

	public void setUsername(String username) {
		checkArgument(!Strings.isNullOrEmpty(username), PreconditionsConstants.USERNAME_CANT_BE_NULL);
		this.username = username;
	}

	public List<Book> getBooks() {
		return books != null ? Collections.unmodifiableList(books) : Collections.unmodifiableList(new ArrayList<>());
	}

	/**
	 * Add a book to the list of books of the user
	 * @param book: The {@link Book} that's going to be added
	 * @throws BookAlreadyOwnedException: When the book that's being added already is owned by the user
	 */
	public void addBook(Book book) throws BookAlreadyOwnedException {
		if (books == null) {
			books = new ArrayList<>();
		}
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
