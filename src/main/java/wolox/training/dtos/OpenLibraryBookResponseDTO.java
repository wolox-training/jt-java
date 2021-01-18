package wolox.training.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
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

	@Data
	@NoArgsConstructor
	static class OpenLibraryBookAuthorDTO {
		private String url;
		private String name;
	}

	@Data
	@NoArgsConstructor
	static class OpenLibraryBookPublisherDTO {
		private String name;
	}

	@Data
	@NoArgsConstructor
	static class OpenLibraryBookCoverDTO {
		private String medium;
	}
}
