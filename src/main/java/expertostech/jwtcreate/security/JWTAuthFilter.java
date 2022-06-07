package expertostech.jwtcreate.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import expertostech.jwtcreate.data.UserDetail;
import expertostech.jwtcreate.model.User;

public class JWTAuthFilter extends UsernamePasswordAuthenticationFilter {
	
	
	public static final int TOKEN_EXPIRACAO = 600_000;
	public static final String TOKEN_SENHA = "249fd9a9-5687-4bb7-8117-cc04f639de07";
			
	private final AuthenticationManager authenticationManager;
	
	public JWTAuthFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, 
			HttpServletResponse response){
		try {
			User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
			
			return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					user.getLogin(), user.getPassword(), new ArrayList<>()));
			
		}catch(IOException e) {
			throw new RuntimeException("Authenticate user failed.", e);
		}
	}
	

	 @Override
	    protected void successfulAuthentication(HttpServletRequest request,
	                                            HttpServletResponse response,
	                                            FilterChain chain,
	                                            Authentication authResult) throws IOException, ServletException {

	        UserDetail userData = (UserDetail) authResult.getPrincipal();
	        
	        String token = JWT.create().withSubject(userData.getUsername())
	        				.withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRACAO))
	        				.sign(Algorithm.HMAC512(TOKEN_SENHA));
	        
	        response.getWriter().write(token);
	        response.getWriter().flush();
	    }
	
}
