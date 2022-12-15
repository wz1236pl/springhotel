package springboot.hotele.security;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import springboot.hotele.models.Gosc;
import springboot.hotele.repository.GoscRepo;
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider{
    @Autowired
    private GoscRepo gRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String email = authentication.getName();
        final String password = authentication.getCredentials().toString();
        System.out.println("huj1");
        Gosc gosc = gRepo.findByEmail(email);
        System.out.println(password);
        if(passwordEncoder.matches(password, gosc.getPassword())){
            System.out.println("huj2");
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
