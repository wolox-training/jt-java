package wolox.training.controllers;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
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
	private User user;

	@BeforeEach
	public void setUp() {
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
	}

	@BeforeEach
	public void init() {

		this.user = new User();
		this.user.setName("Test user name");
		this.user.setUsername("testUserUsername");
		this.user.setBirthdate(LocalDate.now());

	}

	@Test
	public void givenUsers_whenGetUsers_thenReturnJsonArray() throws Exception {

		List<User> users = Collections.singletonList(user);
		given(userRepository.findAll()).willReturn(users);

		String expected = mapper.writeValueAsString(users);

		MvcResult result = mvc.perform(get(apiURL))
				.andExpect(status().isOk())
				.andExpect(response -> Assertions
						.assertEquals(expected, response.getResponse().getContentAsString()))
				.andReturn();

	}

}
