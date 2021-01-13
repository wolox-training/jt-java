package wolox.training.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class OpenLibraryBookResponseDTO {

	private String title;
	private String subtitle;
	@JsonProperty("number_of_pages")
	private Integer pages;
	@JsonProperty("publish_date")
	private String publishDate;
	private List<OpenLibraryBookAuthorDTO> authors;
	private List<OpenLibraryBookPublisherDTO> publishers;
	private OpenLibraryBookCoverDTO cover;

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

	public Integer getPages() {
		return pages;
	}

	public void setPages(Integer pages) {
		this.pages = pages;
	}

	public String getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}

	public List<OpenLibraryBookAuthorDTO> getAuthors() {
		return authors;
	}

	public void setAuthors(
			List<OpenLibraryBookAuthorDTO> authors) {
		this.authors = authors;
	}

	public List<OpenLibraryBookPublisherDTO> getPublishers() {
		return publishers;
	}

	public void setPublishers(
			List<OpenLibraryBookPublisherDTO> publishers) {
		this.publishers = publishers;
	}

	public OpenLibraryBookCoverDTO getCover() {
		return cover;
	}

	public void setCover(OpenLibraryBookCoverDTO cover) {
		this.cover = cover;
	}

	static class OpenLibraryBookAuthorDTO {
		private String url;
		private String name;

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	static class OpenLibraryBookPublisherDTO {
		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	static class OpenLibraryBookCoverDTO {
		private String medium;

		public String getMedium() {
			return medium;
		}

		public void setMedium(String medium) {
			this.medium = medium;
		}
	}
}
