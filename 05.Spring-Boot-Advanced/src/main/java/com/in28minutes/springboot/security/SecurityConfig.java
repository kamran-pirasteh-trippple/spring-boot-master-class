package com.in28minutes.springboot.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	public InMemoryUserDetailsManager userDetailsManager() {
		UserDetails user1 = User.builder()
				.username("user1")
				.password("{noop}secret1")
				.roles("USER")
				.build();
		UserDetails admin1 = User.builder()
				.username("admin1")
				.password("{noop}secret1")
				.roles("USER", "ADMIN")
				.build();
		return new InMemoryUserDetailsManager(user1, admin1);
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.httpBasic(Customizer.withDefaults())
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/surveys/**").hasRole("USER")
						.requestMatchers("/users/**").hasRole("USER")
						.requestMatchers("/**").hasRole("ADMIN"))
				.csrf(csrf -> csrf.ignoringRequestMatchers("/surveys/**"))
				.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));
		return http.build();
	}

}
