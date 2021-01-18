package wolox.training.models;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkArgument;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.google.common.base.Strings;
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

import lombok.Data;
import lombok.NoArgsConstructor;
import wolox.training.constants.PreconditionsConstants;
import wolox.training.dtos.BookDTO;

@Entity
@Table(name = "books")
@ApiModel
@JsonInclude(Include.NON_NULL)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Data
@NoArgsConstructor
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

	public Book(BookDTO bookDTO) {
		this.isbn = bookDTO.getIsbn();
		this.year = bookDTO.getPublishDate();
		this.title = bookDTO.getTitle();
		this.subtitle = bookDTO.getSubtitle();
		this.pages = bookDTO.getPages();
		this.image = bookDTO.getImageUrl();
		this.author = bookDTO.getAuthors().get(0);
		this.publisher = bookDTO.getPublishers().get(0);
	}

	public void setAuthor(String author) {
		checkArgument(!Strings.isNullOrEmpty(author), PreconditionsConstants.AUTHOR_CANT_BE_NULL);
		this.author = author;
	}


	public void setImage(String image) {
		checkArgument(!Strings.isNullOrEmpty(image), PreconditionsConstants.IMAGE_URL_CANT_BE_NULL);
		this.image = image;
	}


	public void setTitle(String title) {
		checkArgument(!Strings.isNullOrEmpty(title), PreconditionsConstants.TITLE_CANT_BE_NULL);
		this.title = title;
	}

	public void setSubtitle(String subtitle) {
		checkArgument(!Strings.isNullOrEmpty(subtitle), PreconditionsConstants.SUBTITLE_CANT_BE_NULL);
		this.subtitle = subtitle;
	}

	public void setPublisher(String publisher) {
		checkArgument(!Strings.isNullOrEmpty(publisher), PreconditionsConstants.PUBLISHER_CANT_BE_NULL);
		this.publisher = publisher;
	}

	public void setYear(String year) {
		checkArgument(!Strings.isNullOrEmpty(year), PreconditionsConstants.YEAR_CANT_BE_NULL);
		this.year = year;
	}

	public void setIsbn(String isbn) {
		checkArgument(!Strings.isNullOrEmpty(isbn), PreconditionsConstants.ISBN_CANT_BE_NULL);
		this.isbn = isbn;
	}

	public void setPages(Integer pages) {
		checkArgument(pages > 0, PreconditionsConstants.PAGES_CANT_BE_0);
		this.pages = pages;
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
