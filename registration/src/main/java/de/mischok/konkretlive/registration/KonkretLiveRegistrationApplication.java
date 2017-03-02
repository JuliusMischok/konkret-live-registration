package de.mischok.konkretlive.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * Spring Boots main class to start the project
 */
@SpringBootApplication
@EnableWebSecurity
public class KonkretLiveRegistrationApplication extends WebSecurityConfigurerAdapter {

	private static final String ROLE_USER = "USER";
	
	@Value("${serviceuser.name}")
	private String username;

	@Value("${serviceuser.password}")
	private String password;
	
	/**
	 * Entry point to start application
	 * @param args Command line args
	 */
	public static void main(String[] args) {
		SpringApplication.run(KonkretLiveRegistrationApplication.class, args);
	}
	
	/**
	 * Sets up the Basic Authentication
	 * @param auth Builder
	 * @throws Exception Any exceptions are re-thrown
	 */
	@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
			.withUser(this.username)
			.password(this.password)
			.authorities(ROLE_USER);
    }
 
    @Override
    protected void configure(HttpSecurity http) throws Exception {
		http
		.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		.httpBasic()
		.and()
		.authorizeRequests()
			.antMatchers(HttpMethod.GET, "/registration/**").authenticated()
		.and()
		.csrf().disable();
    }
}
