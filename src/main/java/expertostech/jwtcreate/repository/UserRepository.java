package expertostech.jwtcreate.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import expertostech.jwtcreate.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	
	public Optional<User> findByLogin(String login);
	
}
