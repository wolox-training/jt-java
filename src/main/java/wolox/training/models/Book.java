package wolox.training.models;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkArgument;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import wolox.training.constants.PreconditionsConstants;

@Entity
@Table(name = "books")
@ApiModel
@JsonInclude(Include.NON_NULL)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@ApiModelProperty(notes = "Id: Unique id, provided by the database")
	private int id;

	@ApiModelProperty(notes = "Genre: Book genre, could be Fantasy, Science Fiction, etc")
	private String genre;

	@NotNull(message = "Book author cannot be null")
	@Column(nullable = false)
	@ApiModelProperty(notes = "Author: Book's writer", required = true)
	private String author;

	@NotNull(message = "Book image url cannot be null")
	@Column(nullable = false)
	@ApiModelProperty(notes = "Image URL: Book's cover image url", required = true)
	private String image;

	@NotNull(message = "Book title cannot be null")
	@Column(nullable = false)
	@ApiModelProperty(notes = "Title: Book's main title", required = true)
	private String title;

	@NotNull(message = "Book subtitle cannot be null")
	@Column(nullable = false)
	@ApiModelProperty(notes = "Subtitle: Book's secondary title", required = true)
	private String subtitle;

	@NotNull(message = "Book publisher cannot be null")
	@Column(nullable = false)
	@ApiModelProperty(notes = "Publisher: Book's publisher house", required = true)
	private String publisher;

	@NotNull(message = "Book year cannot be null")
	@Column(nullable = false)
	@ApiModelProperty(notes = "Year: Book's publication year", required = true)
	private String year;

	@NotNull(message = "Book pages cannot be null")
	@Column(nullable = false)
	@ApiModelProperty(notes = "Pages: Book's pages quantity", required = true)
	private Integer pages;

	@NotNull(message = "Book isbn cannot be null")
	@Column(nullable = false)
	@ApiModelProperty(notes = "ISBN: Book's unique ISBN", required = true)
	private String isbn;

	@ManyToMany(mappedBy = "books", fetch = FetchType.LAZY)
	List<User> users;

	public Book() {
	}

	public Book(int id, String genre, String author, String image, String title,
			String subtitle, String publisher, String year, int pages, String isbn) {
		this.id = id;
		this.genre = genre;
		this.author = author;
		this.image = image;
		this.title = title;
		this.subtitle = subtitle;
		this.publisher = publisher;
		this.year = year;
		this.pages = pages;
		this.isbn = isbn;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = checkNotNull(author, PreconditionsConstants.AUTHOR_CANT_BE_NULL);
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = checkNotNull(image, PreconditionsConstants.IMAGE_URL_CANT_BE_NULL);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = checkNotNull(title, PreconditionsConstants.TITLE_CANT_BE_NULL);
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = checkNotNull(subtitle, PreconditionsConstants.SUBTITLE_CANT_BE_NULL);
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = checkNotNull(publisher, PreconditionsConstants.PUBLISHER_CANT_BE_NULL);
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = checkNotNull(year, PreconditionsConstants.YEAR_CANT_BE_NULL);
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = checkNotNull(isbn, PreconditionsConstants.ISBN_CANT_BE_NULL);
	}

	public Integer getPages() {
		return pages;
	}

	public void setPages(Integer pages) {
		checkArgument(pages > 0, PreconditionsConstants.PAGES_CANT_BE_0);
		this.pages = pages;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	@Override
	public String toString() {
		return "Book{" +
				"id=" + id +
				", genre='" + genre + '\'' +
				", author='" + author + '\'' +
				", image='" + image + '\'' +
				", title='" + title + '\'' +
				", subtitle='" + subtitle + '\'' +
				", publisher='" + publisher + '\'' +
				", year='" + year + '\'' +
				", pages=" + pages +
				", isbn='" + isbn + '\'' +
				'}';
	}
}
