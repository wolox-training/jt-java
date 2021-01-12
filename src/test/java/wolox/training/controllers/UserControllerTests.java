package wolox.training.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import wolox.training.exceptions.UserNotFoundException;
import wolox.training.models.User;
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UserRepository;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private UserRepository userRepository;
	@MockBean
	private BookRepository bookRepository;

	private final ObjectMapper mapper = new ObjectMapper();

	private final String apiURL = "/api/users";
	private final int userId = 1;
	private final int nonExistingUserId = 1000;
	private User user;

	@BeforeEach
	public void setUp() {
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
	}

	@BeforeEach
	public void init() {

		this.user = new User();
		this.user.setName("TesName");
		this.user.setUsername("testUsername");
		this.user.setBirthdate(LocalDate.now());

	}

	@Test
	void givenUsers_whenGetUsers_thenReturnJsonArray() throws Exception {
		List<User> users = Collections.singletonList(user);
		given(userRepository.findAll()).willReturn(users);

		mvc.perform(get(apiURL))
				.andExpect(status().isOk())
				.andExpect(result -> assertEquals(mapper.writeValueAsString(users), result.getResponse().getContentAsString()));
	}

	@Test
	void givenUserAndId_whenGetUser_thenReturnUser() throws Exception {
		given(userRepository.findById(userId)).willReturn(Optional.of(user));

		mvc.perform(get(apiURL + "/" + userId))
				.andExpect(status().isOk())
				.andExpect(result -> assertEquals(mapper.writeValueAsString(user), result.getResponse().getContentAsString()));
	}

	@Test
	void givenUserAndId_whenGetUser_thenReturnUserNotFoundException() throws Exception {
		given(userRepository.findById(userId)).willReturn(Optional.of(user));

		mvc.perform(get(apiURL + "/" + nonExistingUserId))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof UserNotFoundException));
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



}
