package wolox.training.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "books")
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;

	private String genre;

	@Column(nullable = false)
	private String author;

	@Column(nullable = false)
	private String image;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String subtitle;

	@Column(nullable = false)
	private String publisher;

	@Column(nullable = false)
	private String year;

	@Column(nullable = false)
	private int pages;

	@Column(nullable = false)
	private String isbn;

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
