package wolox.training.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.stream.Collectors;
import wolox.training.dtos.OpenLibraryBookResponseDTO.OpenLibraryBookAuthorDTO;
import wolox.training.dtos.OpenLibraryBookResponseDTO.OpenLibraryBookPublisherDTO;
import wolox.training.models.Book;

public class BookDTO {

	private String isbn;
	private String title;
	private String subtitle;
	@JsonProperty("publish_date")
	private String publishDate;
	private Integer pages;
	@JsonProperty("imege_url")
	private String imageUrl;
	private List<String> publishers;
	private List<String> authors;

	public BookDTO(Book book) {
		this.isbn = book.getIsbn();
		this.title = book.getTitle();
		this.subtitle = book.getSubtitle();
		this.publishDate = book.getYear();
		this.pages = book.getPages();
		this.publishers = List.of(book.getPublisher());
		this.authors = List.of(book.getAuthor());
		this.imageUrl = book.getImage();
	}

	public BookDTO(String isbn, OpenLibraryBookResponseDTO openLibraryBook) {
		this.isbn = isbn;
		this.title = openLibraryBook.getTitle();
		this.subtitle = openLibraryBook.getSubtitle();
		this.publishDate = openLibraryBook.getPublishDate();
		this.pages = openLibraryBook.getPages();
		this.publishers = openLibraryBook.getPublishers().stream().map(
				OpenLibraryBookPublisherDTO::getName).collect(
				Collectors.toList());
		this.authors = openLibraryBook.getAuthors().stream().map(OpenLibraryBookAuthorDTO::getName).collect(
				Collectors.toList());
		this.imageUrl = openLibraryBook.getCover().getMedium();
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
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

	public String getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}

	public Integer getPages() {
		return pages;
	}

	public void setPages(Integer pages) {
		this.pages = pages;
	}

	public List<String> getPublishers() {
		return publishers;
	}

	public void setPublishers(List<String> publishers) {
		this.publishers = publishers;
	}

	public List<String> getAuthors() {
		return authors;
	}

	public void setAuthors(List<String> authors) {
		this.authors = authors;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
}
