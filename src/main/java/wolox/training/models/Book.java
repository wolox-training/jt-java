package wolox.training.models;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "books")
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;

	private String genre;

	@NotNull(message = "Book author cannot be null")
	@Column(nullable = false)
	private String author;

	@NotNull(message = "Book image url cannot be null")
	@Column(nullable = false)
	private String image;

	@NotNull(message = "Book title cannot be null")
	@Column(nullable = false)
	private String title;

	@NotNull(message = "Book subtitle cannot be null")
	@Column(nullable = false)
	private String subtitle;

	@NotNull(message = "Book publisher cannot be null")
	@Column(nullable = false)
	private String publisher;

	@NotNull(message = "Book year cannot be null")
	@Column(nullable = false)
	private String year;

	@NotNull(message = "Book pages cannot be null")
	@Column(nullable = false)
	private Integer pages;

	@NotNull(message = "Book isbn cannot be null")
	@Column(nullable = false)
	private String isbn;

	@ManyToMany(mappedBy = "books")
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
		this.author = author;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public void setPages(Integer pages) {
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
