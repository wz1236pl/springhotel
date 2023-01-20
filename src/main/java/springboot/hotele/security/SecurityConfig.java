package springboot.hotele.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

@AutoConfigureBefore
@Configuration
@EnableWebSecurity
public class SecurityConfig{
    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{                 //określa co kto może
        http.csrf().disable()
            .authorizeRequests()
            .antMatchers("/register").permitAll()                                                   //do logowania i rejestracji dajemy dostęp wszystkim
            .antMatchers("/login").permitAll()
            .antMatchers("/").permitAll()                                                           //na pusty endpoint też
            .antMatchers("/gosc/edytujDane").hasAnyAuthority("GOSC","PRACOWNIK")    //do edycji własnego profilu dajemy dostęp adminowi i userowi
            .antMatchers("/gosc/**").hasAnyAuthority("GOSC")                        //ograniczamy dostęp tylko dla zalogowanego gościa (admin nie może rezerwować)
            .antMatchers("/pracownik/**").hasAnyAuthority("PRACOWNIK")               //to samo dla admina    jak coś to "/**" to że po / wszystkie inne linki 
            .and()
            .formLogin()
            .loginPage("/login")        //wywołuje ten endpoint jako podstawowy logowania
            .and()
            .logout()
            .logoutSuccessUrl("/")          //przenosi na to url po wylogowaniu
            .and()
            .exceptionHandling()
            .accessDeniedHandler(accessDeniedHandler());        // obsługa tego jak ktoś chce wejść na stronę na którą nie ma uprawnień
        
        return http.build();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception{
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(customAuthenticationProvider);
        return authenticationManagerBuilder.build();                                                //ustawiasz własnego providera dla logowania
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web) -> web.ignoring().antMatchers("/h2/**");           //nie pozwalasz żeby spring zabezpieczał ci konsole bazy(baza ma swoje zabezpieczenia)
    }

    @Bean
    public static PasswordEncoder passwordEncoder(){                            //ustawiasz jak hashujesz hasła (jakim encoderem)
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new CustomAccessDeniedHandler();
    }

}
