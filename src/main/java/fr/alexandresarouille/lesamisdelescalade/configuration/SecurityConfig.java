package fr.alexandresarouille.lesamisdelescalade.configuration;

import fr.alexandresarouille.lesamisdelescalade.entities.enums.Role;
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


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth
                .inMemoryAuthentication()
                .passwordEncoder(bCryptPasswordEncoder())
                .withUser("admin")
                .password("admin")
                .authorities(Role.ADMIN.name());

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()

                .disable()
                .formLogin()
                .defaultSuccessUrl("/index")
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
