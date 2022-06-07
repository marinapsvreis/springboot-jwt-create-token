package expertostech.jwtcreate.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import expertostech.jwtcreate.model.User;
import expertostech.jwtcreate.service.UserService;

@RestController
@RequestMapping(value = "/api/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder encoder;

	@GetMapping("/findAll")
	public ResponseEntity<List<User>> findAll(){
		return ResponseEntity.ok(userService.findAll());
	}
	
	@PostMapping("/save")
	public ResponseEntity<User> save(@RequestBody User user){
		user.setPassword(encoder.encode(user.getPassword()));
		return ResponseEntity.ok(userService.save(user));
	}
	
	@GetMapping("/passwordValidate")
	public ResponseEntity<Boolean> passwordValidate(@RequestParam String login, @RequestParam String password){

		Optional<User> optUser = userService.findByLogin(login);
		if(optUser.isEmpty()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
		}
		
		User user = optUser.get();
		boolean valid = encoder.matches(password, user.getPassword());
		
		HttpStatus status = (valid) ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;
		
		return ResponseEntity.status(status).body(valid);
	}
	
}
