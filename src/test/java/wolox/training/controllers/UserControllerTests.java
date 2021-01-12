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
import java.time.LocalDate;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import wolox.training.exceptions.ActionNotFoundException;
import wolox.training.exceptions.BookAlreadyOwnedException;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.exceptions.DatabaseException;
import wolox.training.exceptions.IdMismatchException;
import wolox.training.exceptions.UserNotFoundException;
import wolox.training.models.Book;
import wolox.training.models.User;
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UserRepository;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
class UserControllerTests {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private UserRepository userRepository;
	@MockBean
	private BookRepository bookRepository;

	private final ObjectMapper mapper = new ObjectMapper();

	private final String apiURL = "/api/users";
	private final int userId = 5;
	private final int nonExistingUserId = 500;
	private final int bookId = 1;
	private final int nonExistingBookId = 500;
	private User user;
	private User userWithErrors;
	private User userToUpdate;
	private Book book;

	@BeforeEach
	public void setUp() {
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
	}

	@BeforeEach
	public void init() {

		this.user = new User();
		this.user.setName("TestName");
		this.user.setUsername("testUsername");
		this.user.setBirthdate(LocalDate.now());

		this.userWithErrors = new User();
		this.userWithErrors.setName("TestName");
		this.userWithErrors.setUsername("testUsernameIsTooLong");
		this.userWithErrors.setBirthdate(LocalDate.now());

		this.userToUpdate = new User();
		this.userToUpdate.setId(5);
		this.userToUpdate.setName("TestName");
		this.userToUpdate.setUsername("testUsername");
		this.userToUpdate.setBirthdate(LocalDate.now());

		book = new Book();
		book.setImage("http://someurl.com");
		book.setGenre("Book genre");
		book.setPages(500);
		book.setPublisher("Some publisher");
		book.setTitle("Book title");
		book.setSubtitle("Book subtitle");
		book.setYear("2000");
		book.setIsbn("Some isbn");
		book.setAuthor("Book author");
	}

	@Test
	void whenGetUsers_thenReturnJsonArray() throws Exception {
		List<User> users = Collections.singletonList(user);
		given(userRepository.findAll()).willReturn(users);

		mvc.perform(get(apiURL))
				.andExpect(status().isOk())
				.andExpect(result ->
						assertEquals(mapper.writeValueAsString(users), result.getResponse().getContentAsString()));
	}

	@Test
	void givenId_whenGetUser_thenReturnUser() throws Exception {
		given(userRepository.findById(userId)).willReturn(Optional.of(user));

		mvc.perform(get(apiURL + "/" + userId))
				.andExpect(status().isOk())
				.andExpect(result ->
						assertEquals(mapper.writeValueAsString(user), result.getResponse().getContentAsString()));
	}

	@Test
	void givenUserAndId_whenGetUser_thenReturnUserNotFoundException() throws Exception {
		given(userRepository.findById(userId)).willReturn(Optional.of(user));

		mvc.perform(get(apiURL + "/" + nonExistingUserId))
				.andExpect(status().isNotFound())
				.andExpect(result ->
						assertTrue(result.getResolvedException() instanceof UserNotFoundException));
	}

	@Test
	void givenUser_whenCreateUser_thenReturnUser() throws Exception {
		when(userRepository.save(any())).thenReturn(user);

		String userString = mapper.writeValueAsString(user);

		mvc.perform(post(apiURL)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(userString))
				.andExpect(status().isCreated())
				.andExpect(result ->
						assertEquals(mapper.writeValueAsString(user), result.getResponse().getContentAsString()));
	}

	@Test
	void givenUser_whenCreateUser_thenReturnDatabaseException() throws Exception {
		when(userRepository.save(any())).thenReturn(DatabaseException.class);

		String userString = mapper.writeValueAsString(userWithErrors);

		mvc.perform(post(apiURL)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(userString))
				.andExpect(status().isBadRequest())
				.andExpect(result ->
						assertTrue(result.getResolvedException() instanceof DatabaseException));
	}

	@Test
	void givenUserAndId_whenUpdateUser_thenReturnUser() throws Exception {
		when(userRepository.existsById(any())).thenReturn(true);
		when(userRepository.save(any())).thenReturn(userToUpdate);

		String userString = mapper.writeValueAsString(userToUpdate);

		mvc.perform(put(apiURL + "/" + userId)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(userString))
				.andExpect(status().isOk())
				.andExpect(result ->
						assertEquals(mapper.writeValueAsString(userToUpdate), result.getResponse().getContentAsString()));
	}

	@Test
	void givenUserAndId_whenUpdateUser_thenReturnIdMismatchException() throws Exception {
		when(userRepository.existsById(any())).thenReturn(true);

		String userString = mapper.writeValueAsString(userToUpdate);

		mvc.perform(put(apiURL + "/" + 9)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(userString))
				.andExpect(status().isBadRequest())
				.andExpect(result ->
						assertTrue(result.getResolvedException() instanceof IdMismatchException));
	}

	@Test
	void givenUserAndId_whenUpdateUser_thenReturnNotFoundException() throws Exception {
		String userString = mapper.writeValueAsString(userToUpdate);

		mvc.perform(put(apiURL + "/" + nonExistingUserId)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(userString))
				.andExpect(status().isNotFound())
				.andExpect(result ->
						assertTrue(result.getResolvedException() instanceof UserNotFoundException));
	}

	@Test
	void givenUserAndId_whenUpdateUser_thenReturnDatabaseException() throws Exception {
		when(userRepository.existsById(any())).thenReturn(true);
		when(userRepository.save(any())).thenReturn(DatabaseException.class);

		String userString = mapper.writeValueAsString(userToUpdate);

		mvc.perform(put(apiURL + "/" + userId)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(userString))
				.andExpect(status().isBadRequest())
				.andExpect(result ->
						assertTrue(result.getResolvedException() instanceof DatabaseException));
	}

	@Test
	void givenUserId_whenDeleteUser_thenReturnNoContent() throws Exception {
		when(userRepository.existsById(any())).thenReturn(true);

		mvc.perform(delete(apiURL + "/" + userId))
				.andExpect(status().isNoContent());
	}

	@Test
	void givenUserId_whenDeleteUser_thenReturnNoTFoundException() throws Exception {
		mvc.perform(get(apiURL + "/" + nonExistingUserId))
				.andExpect(status().isNotFound())
				.andExpect(result ->
						assertTrue(result.getResolvedException() instanceof UserNotFoundException));

	}

	@Test
	void givenUserIdAndBookId_whenAddBookToUser_thenReturnUserWithBooks() throws Exception {
		when(userRepository.findById(any())).thenReturn(Optional.of(user));
		when(bookRepository.findById(any())).thenReturn(Optional.of(book));
		when(userRepository.save(any())).thenReturn(user);

		mvc.perform(put(apiURL + "/" + userId + "/books/" + bookId + "?action=add"))
				.andExpect(status().isOk())
				.andExpect(result ->
						assertEquals(mapper.writeValueAsString(user), result.getResponse().getContentAsString()));
	}

	@Test
	void givenUserIdAndBookId_whenAddBookToUser_thenReturnBookAlreadyOwnedException() throws Exception {
		when(userRepository.findById(any())).thenReturn(Optional.of(user));
		when(bookRepository.findById(any())).thenReturn(Optional.of(book));
		when(userRepository.save(any())).thenReturn(user);

		user.addBook(book);

		mvc.perform(put(apiURL + "/" + userId + "/books/" + bookId + "?action=add"))
				.andExpect(status().isBadRequest())
				.andExpect(result ->
						assertTrue(result.getResolvedException() instanceof BookAlreadyOwnedException));
	}

	@Test
	void givenUserIdAndBookId_whenRemoveBookToUser_thenReturnUserWithBooks() throws Exception {
		when(userRepository.findById(any())).thenReturn(Optional.of(user));
		when(bookRepository.findById(any())).thenReturn(Optional.of(book));
		when(userRepository.save(any())).thenReturn(user);

		user.addBook(book);

		mvc.perform(put(apiURL + "/" + userId + "/books/" + bookId + "?action=remove"))
				.andExpect(status().isOk())
				.andExpect(result ->
						assertEquals(mapper.writeValueAsString(user), result.getResponse().getContentAsString()));
	}

	@Test
	void givenUserIdAndBookId_whenModifyingBooks_thenReturnUserNotFoundException() throws Exception {
		when(bookRepository.findById(any())).thenReturn(Optional.of(book));

		mvc.perform(put(apiURL + "/" + nonExistingUserId + "/books/" + bookId + "?action=add"))
				.andExpect(status().isNotFound())
				.andExpect(result ->
						assertTrue(result.getResolvedException() instanceof UserNotFoundException));
	}

	@Test
	void givenUserIdAndBookId_whenModifyingBooks_thenReturnBookNotFoundException() throws Exception {
		when(userRepository.findById(any())).thenReturn(Optional.of(user));

		mvc.perform(put(apiURL + "/" + userId + "/books/" + nonExistingBookId + "?action=add"))
				.andExpect(status().isNotFound())
				.andExpect(result ->
						assertTrue(result.getResolvedException() instanceof BookNotFoundException));
	}

	@Test
	void givenUserIdAndBookId_whenModifyingBooks_thenReturnActionNotFoundException() throws Exception {
		when(userRepository.findById(any())).thenReturn(Optional.of(user));
		when(bookRepository.findById(any())).thenReturn(Optional.of(book));

		mvc.perform(put(apiURL + "/" + userId + "/books/" + bookId + "?action=modify"))
				.andExpect(status().isBadRequest())
				.andExpect(result ->
						assertTrue(result.getResolvedException() instanceof ActionNotFoundException));
	}

}
