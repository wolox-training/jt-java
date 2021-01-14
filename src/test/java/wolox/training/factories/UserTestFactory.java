package wolox.training.factories;

import java.time.LocalDate;
import wolox.training.constants.TestsConstants;
import wolox.training.models.User;

public class UserTestFactory {

	public static User getUser(String type) {

		User user = new User();
		user.setName("TestName");
		user.setUsername("testUsername");
		user.setPassword("testPassword");
		user.setBirthdate(LocalDate.now());

		switch (type) {
			case TestsConstants.UPDATE_FACTORY_REQUEST:
				user.setId(5);
				break;
			case TestsConstants.ERROR_FACTORY_REQUEST:
				user.setUsername("testUsernameIsReallyReallyLong");
				break;
		}

		return user;
	}

}
