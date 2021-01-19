package wolox.training.services;

import java.io.IOException;
import java.util.Map;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import wolox.training.constants.ExceptionsConstants;
import wolox.training.constants.ServicesConstants;
import wolox.training.dtos.BookDTO;
import wolox.training.dtos.OpenLibraryBookResponseDTO;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.models.Book;

@Service
public class OpenLibraryService {

	private final RestTemplate restTemplate = new RestTemplate();

	/**
	 * This method receives a {@link Book} ISBN not encountered in the database and searches for it in an external service
	 * @param isbn: The {@link Book} ISBN the external API requires
	 * @return A {@link BookDTO} with all the data
	 * @throws BookNotFoundException: When the API doesn't find the requested ISBN
	 */
	public BookDTO searchBook(String isbn) throws BookNotFoundException, IOException {
		ResponseEntity<Map<String, OpenLibraryBookResponseDTO>> response = restTemplate.exchange(String.format(ServicesConstants.OPENLIBRARY_SERVICE_URL, isbn),
				HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, OpenLibraryBookResponseDTO>>() {});

		if(response.getStatusCode().equals(HttpStatus.OK) && response.getBody() != null && !response.getBody().isEmpty()) {
			Map<String, OpenLibraryBookResponseDTO> responseMap = response.getBody();
			return new BookDTO(isbn, responseMap.get("ISBN:" + isbn));
		} else throw new BookNotFoundException(ExceptionsConstants.BOOK_BY_ISBN_NOT_FOUND);

	}

}
