package fr.alexandresarouille.lesamisdelescalade.configuration;

import fr.alexandresarouille.lesamisdelescalade.entities.ClimbingSite;
import fr.alexandresarouille.lesamisdelescalade.entities.User;
import fr.alexandresarouille.lesamisdelescalade.entities.enums.Difficulty;
import fr.alexandresarouille.lesamisdelescalade.entities.enums.Region;
import fr.alexandresarouille.lesamisdelescalade.entities.enums.Role;
import fr.alexandresarouille.lesamisdelescalade.services.ClimbingSiteService;
import fr.alexandresarouille.lesamisdelescalade.services.UserService;
import org.aspectj.apache.bcel.classfile.BootstrapMethods;
import org.hibernate.jpa.boot.spi.Bootstrap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.config.BootstrapMode;
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

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth
                .inMemoryAuthentication()
                .passwordEncoder(bCryptPasswordEncoder())
                .and()
                .userDetailsService(userService);

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
