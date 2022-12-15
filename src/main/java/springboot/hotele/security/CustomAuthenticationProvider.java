package springboot.hotele.security;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import springboot.hotele.models.Gosc;
import springboot.hotele.repository.GoscRepo;

public class CustomAuthenticationProvider implements AuthenticationProvider{
    @Autowired
    private GoscRepo gRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String email = authentication.getName();
        final String password = authentication.getCredentials().toString();

        Gosc gosc = gRepo.findByEmailIs(email);
        if(passwordEncoder.matches(password, gosc.getPassword())){
            return new UsernamePasswordAuthenticationToken(email, password, new ArrayList<>());
        }else{
            throw new BadCredentialsException("Login error!");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
    
}
