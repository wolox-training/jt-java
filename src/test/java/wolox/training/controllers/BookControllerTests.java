package wolox.training.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import wolox.training.constants.TestsConstants;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.exceptions.DatabaseException;
import wolox.training.exceptions.IdMismatchException;
import wolox.training.factories.BookTestFactory;
import wolox.training.models.Book;
import wolox.training.repositories.BookRepository;
import wolox.training.security.CustomAuthenticationService;

@RunWith(SpringRunner.class)
@WebMvcTest(BookController.class)
class BookControllerTests {

	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private BookRepository bookRepository;
	@MockBean
	private CustomAuthenticationService authenticationService;

	private final ObjectMapper mapper = new ObjectMapper();

	private final String apiURL = "/api/books";
	private final String testUser = "testUser";
	private final int bookId = 1;
	private final int nonExistingBookId = 500;

	@BeforeEach
	public void setUp() {
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
	}

	@WithMockUser(value = testUser)
	@Test
	void whenGetBooks_thenReturnJsonArray() throws Exception {
		Book book = BookTestFactory.getBook(TestsConstants.SIMPLE_FACTORY_REQUEST);
		List<Book> books = Collections.singletonList(book);
		given(bookRepository.findAll()).willReturn(books);

		mvc.perform(get(apiURL))
				.andExpect(status().isOk())
				.andExpect(result ->
						assertEquals(mapper.writeValueAsString(books), result.getResponse().getContentAsString()));
	}

	@WithMockUser(value = testUser)
	@Test
	void givenId_whenGetBook_thenReturnBook() throws Exception {
		Book book = BookTestFactory.getBook(TestsConstants.SIMPLE_FACTORY_REQUEST);
		given(bookRepository.findById(bookId)).willReturn(Optional.of(book));

		mvc.perform(get(apiURL + "/" + bookId))
				.andExpect(status().isOk())
				.andExpect(result ->
						assertEquals(mapper.writeValueAsString(book), result.getResponse().getContentAsString()));
	}

	@WithMockUser(value = testUser)
	@Test
	void givenBookAndId_whenGetBook_thenReturnBookNotFoundException() throws Exception {
		Book book = BookTestFactory.getBook(TestsConstants.SIMPLE_FACTORY_REQUEST);
		given(bookRepository.findById(bookId)).willReturn(Optional.of(book));

		mvc.perform(get(apiURL + "/" + nonExistingBookId))
				.andExpect(status().isNotFound())
				.andExpect(result ->
						assertTrue(result.getResolvedException() instanceof BookNotFoundException));
	}

	@Test
	void givenBook_whenCreateBook_thenReturnBook() throws Exception {
		Book book = BookTestFactory.getBook(TestsConstants.SIMPLE_FACTORY_REQUEST);
		when(bookRepository.save(any())).thenReturn(book);

		String bookString = mapper.writeValueAsString(book);

		mvc.perform(post(apiURL)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(bookString))
				.andExpect(status().isCreated())
				.andExpect(result ->
						assertEquals(mapper.writeValueAsString(book), result.getResponse().getContentAsString()));
	}

	@Test
	void givenBook_whenCreateBook_thenReturnDatabaseException() throws Exception {
		Book book = BookTestFactory.getBook(TestsConstants.ERROR_FACTORY_REQUEST);
		when(bookRepository.save(any())).thenReturn(DatabaseException.class);

		String bookString = mapper.writeValueAsString(book);

		mvc.perform(post(apiURL)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(bookString))
				.andExpect(status().isBadRequest())
				.andExpect(result ->
						assertTrue(result.getResolvedException() instanceof DatabaseException));
	}

	@WithMockUser(value = testUser)
	@Test
	void givenBookAndId_whenUpdateBook_thenReturnBook() throws Exception {
		Book book = BookTestFactory.getBook(TestsConstants.UPDATE_FACTORY_REQUEST);
		when(bookRepository.existsById(any())).thenReturn(true);
		when(bookRepository.save(any())).thenReturn(book);

		String bookString = mapper.writeValueAsString(book);

		mvc.perform(put(apiURL + "/" + bookId)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(bookString))
				.andExpect(status().isOk())
				.andExpect(result ->
						assertEquals(mapper.writeValueAsString(book), result.getResponse().getContentAsString()));
	}

	@WithMockUser(value = testUser)
	@Test
	void givenBookAndId_whenUpdateBook_thenReturnIdMismatchException() throws Exception {
		Book book = BookTestFactory.getBook(TestsConstants.UPDATE_FACTORY_REQUEST);
		when(bookRepository.existsById(any())).thenReturn(true);

		String bookString = mapper.writeValueAsString(book);

		mvc.perform(put(apiURL + "/" + 9)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(bookString))
				.andExpect(status().isBadRequest())
				.andExpect(result ->
						assertTrue(result.getResolvedException() instanceof IdMismatchException));
	}

	@WithMockUser(value = testUser)
	@Test
	void givenBookAndId_whenUpdateBook_thenReturnNotFoundException() throws Exception {
		Book book = BookTestFactory.getBook(TestsConstants.UPDATE_FACTORY_REQUEST);
		String bookString = mapper.writeValueAsString(book);

		mvc.perform(put(apiURL + "/" + bookId)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(bookString))
				.andExpect(status().isNotFound())
				.andExpect(result ->
						assertTrue(result.getResolvedException() instanceof BookNotFoundException));
	}

	@WithMockUser(value = testUser)
	@Test
	void givenBookAndId_whenUpdateBook_thenReturnDatabaseException() throws Exception {
		Book book = BookTestFactory.getBook(TestsConstants.UPDATE_FACTORY_REQUEST);
		when(bookRepository.existsById(any())).thenReturn(true);
		when(bookRepository.save(any())).thenReturn(DatabaseException.class);

		String bookString = mapper.writeValueAsString(book);

		mvc.perform(put(apiURL + "/" + bookId)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(bookString))
				.andExpect(status().isBadRequest())
				.andExpect(result ->
						assertTrue(result.getResolvedException() instanceof DatabaseException));
	}

	@WithMockUser(value = testUser)
	@Test
	void givenBookId_whenDeleteBook_thenReturnNoContent() throws Exception {
		when(bookRepository.existsById(any())).thenReturn(true);

		mvc.perform(delete(apiURL + "/" + bookId))
				.andExpect(status().isNoContent());
	}

	@WithMockUser(value = testUser)
	@Test
	void givenBookId_whenDeleteBook_thenReturnNoTFoundException() throws Exception {
		mvc.perform(get(apiURL + "/" + nonExistingBookId))
				.andExpect(status().isNotFound())
				.andExpect(result ->
						assertTrue(result.getResolvedException() instanceof BookNotFoundException));

	}
}
