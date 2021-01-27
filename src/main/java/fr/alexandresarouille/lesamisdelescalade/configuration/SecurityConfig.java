package fr.alexandresarouille.lesamisdelescalade.configuration;

import fr.alexandresarouille.lesamisdelescalade.entities.ClimbingSite;
import fr.alexandresarouille.lesamisdelescalade.entities.User;
import fr.alexandresarouille.lesamisdelescalade.entities.enums.Difficulty;
import fr.alexandresarouille.lesamisdelescalade.entities.enums.Region;
import fr.alexandresarouille.lesamisdelescalade.entities.enums.Role;
import fr.alexandresarouille.lesamisdelescalade.services.ClimbingSiteService;
import fr.alexandresarouille.lesamisdelescalade.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter  {

    @Autowired
    private UserService userService;
    @Autowired
    private ClimbingSiteService climbingSiteService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth
                .inMemoryAuthentication()
                .passwordEncoder(bCryptPasswordEncoder())
                .and()
                .userDetailsService(userService);

        userService.createUserAccount(new User("admin", "admin", "admin", Role.ADMIN));
        climbingSiteService.createClimbingSite(new ClimbingSite("TEST", "TEST", Region.GRAND_EST, 10, 10, 10, true, Difficulty.HIGH));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()

                .disable()
                .formLogin()
                .defaultSuccessUrl("/")
                .and()
                .authorizeRequests()
                .antMatchers("/**").permitAll()
                .antMatchers("/user/**").hasAuthority(Role.USER.name())
                .antMatchers("/admin/**").hasAuthority(Role.ADMIN.name())
                .anyRequest().authenticated();

    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
