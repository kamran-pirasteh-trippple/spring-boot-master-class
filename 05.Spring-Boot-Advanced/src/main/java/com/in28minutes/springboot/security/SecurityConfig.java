package com.in28minutes.springboot.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public InMemoryUserDetailsManager userDetailsService() {
		UserDetails user1 = User.withUsername("user1")
				.password("{noop}secret1")
				.roles("USER")
				.build();
		UserDetails admin1 = User.withUsername("admin1")
				.password("{noop}secret1")
				.roles("USER", "ADMIN")
				.build();
		return new InMemoryUserDetailsManager(user1, admin1);
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.httpBasic(httpBasic -> {})
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/surveys/**").hasRole("USER")
				.requestMatchers("/users/**").hasRole("USER")
				.requestMatchers("/**").hasRole("ADMIN")
			)
			.csrf(csrf -> csrf.disable())
			.headers(headers -> headers.frameOptions(frame -> frame.disable()));
		return http.build();
	}
}
