package wolox.training.models;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
	private PageRequest page = PageRequest.of(0, 10, Sort.by("id"));

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
		assertThrows(IllegalArgumentException.class, () -> user.setUsername(null));
	}

	@Test
	void givenCharSequenceAndDateRange_whenSearch_thenReturnUsers() {

		String searchCharSequence = TestsConstants.USER_TEST_CHARSEQUENCE;
		LocalDate searchBeginDate = LocalDate.parse(TestsConstants.USER_TEST_BEGIN_DATE);
		LocalDate searchEndDate = LocalDate.parse(TestsConstants.USER_TEST_END_DATE);

		User normalUser = UserTestFactory.getUser(TestsConstants.SIMPLE_FACTORY_REQUEST);
		User diffNameUser = UserTestFactory.getUser(TestsConstants.USER_DIFFERENT_NAME);
		User diffBirthdayUser = UserTestFactory.getUser(TestsConstants.USER_DIFFERENT_DATE);

		entityManager.persist(normalUser);
		entityManager.persist(diffNameUser);
		entityManager.persist(diffBirthdayUser);

		List<User> fullParameterList = Collections.singletonList(diffBirthdayUser);
		assertThat(userRepository.findAllBirthdateBetweenAndNameLike(searchCharSequence, searchBeginDate, searchEndDate, page).getContent())
				.isEqualTo(fullParameterList);

		List<User> noParametersList = Arrays.asList(normalUser, diffNameUser, diffBirthdayUser);
		assertThat(userRepository.findAllBirthdateBetweenAndNameLike("", null, null, page).getContent())
				.isEqualTo(noParametersList);

		List<User> noCharSequenceParameterList = Arrays.asList(diffNameUser, diffBirthdayUser);
		assertThat(userRepository.findAllBirthdateBetweenAndNameLike("", searchBeginDate, searchEndDate, page).getContent())
				.isEqualTo(noCharSequenceParameterList);

		List<User> noBeginDateParameterList = Collections.singletonList(diffBirthdayUser);
		assertThat(userRepository.findAllBirthdateBetweenAndNameLike(searchCharSequence, null, searchEndDate, page).getContent())
				.isEqualTo(noBeginDateParameterList);

		List<User> noEndDateParameterList = Arrays.asList(normalUser ,diffBirthdayUser);
		assertThat(userRepository.findAllBirthdateBetweenAndNameLike(searchCharSequence, searchBeginDate, null, page).getContent())
				.isEqualTo(noEndDateParameterList);

	}
}
