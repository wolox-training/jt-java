package wolox.training.models;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import wolox.training.constants.TestsConstants;
import wolox.training.factories.UserTestFactory;
import wolox.training.repositories.UserRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
class UserTests {

	@Autowired
	private TestEntityManager entityManager;
	@Autowired
	private UserRepository userRepository;

	private User user;

	@BeforeEach
	public void init() {
		this.user = UserTestFactory.getUser(TestsConstants.SIMPLE_FACTORY_REQUEST);
	}

	@Test
	void givenUser_whenCreate_thenSuccess() {
		entityManager.persist(user);
		entityManager.flush();

		Optional<User> newUser = userRepository.findByUsername("testUsername");

		assertThat(newUser).isNotEmpty();
		assertThat(newUser.get().getUsername()).isEqualTo(user.getUsername());
	}

	@Test
	void givenUser_whenCreate_thenPreconditionsException() {
		assertThrows(NullPointerException.class, () -> user.setUsername(null));
	}

}
