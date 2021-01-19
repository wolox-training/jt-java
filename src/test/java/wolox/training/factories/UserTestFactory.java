package wolox.training.factories;

import java.time.LocalDate;
import wolox.training.constants.TestsConstants;
import wolox.training.models.Student;
import wolox.training.models.User;

public class UserTestFactory {

	public static User getUser(String type) {

		User user = new Student();
		user.setName("TestName");
		user.setUsername("testUsername");
		user.setPassword("testPassword");
		user.setBirthdate(LocalDate.now());
		user.setType("student");

		switch (type) {
			case TestsConstants.UPDATE_FACTORY_REQUEST:
				user.setId(5);
				break;
			case TestsConstants.ERROR_FACTORY_REQUEST:
				user.setUsername("testUsernameIsReallyReallyLong");
				break;
			case TestsConstants.USER_DIFFERENT_NAME:
				user.setName("nombreDePrueba");
				user.setBirthdate(LocalDate.parse("1999-04-13"));
				break;
			case TestsConstants.USER_DIFFERENT_DATE:
				user.setName("TestUser");
				user.setBirthdate(LocalDate.parse("1961-06-28"));
				break;
			case TestsConstants.PROFESSOR_USER:
				user.setType("professor");
				break;
		}

		return user;
	}

}
