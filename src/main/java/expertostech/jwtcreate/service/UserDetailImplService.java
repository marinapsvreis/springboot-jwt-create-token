package expertostech.jwtcreate.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import expertostech.jwtcreate.data.UserDetail;
import expertostech.jwtcreate.model.User;
import expertostech.jwtcreate.repository.UserRepository;

@Component
public class UserDetailImplService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = userRepository.findByLogin(username);
		
		if(user.isEmpty()) {
			throw new UsernameNotFoundException("User " + username + " not found.");
		}
		
		return new UserDetail(user);
	}

}
