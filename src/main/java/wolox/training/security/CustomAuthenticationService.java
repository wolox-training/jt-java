package wolox.training.security;

import java.util.ArrayList;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import wolox.training.models.User;
import wolox.training.repositories.UserRepository;

@Service
public class CustomAuthenticationService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> optional = userRepository.findByUsername(username);
		if (optional.isEmpty()) {
			throw new UsernameNotFoundException("Username not found");
		}

		User u = optional.get();
		return org.springframework.security.core.userdetails.User
				.builder()
				.username(u.getUsername())
				.password(u.getPassword())
				.authorities(new ArrayList<>())
				.build();
	}
}
