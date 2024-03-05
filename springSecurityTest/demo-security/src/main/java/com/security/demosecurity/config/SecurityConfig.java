package com.security.demosecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig{
    
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((authRequest)->{
                authRequest
                    .requestMatchers("/", "/info").permitAll()
                    .requestMatchers("/admin").hasRole("ADMIN")
                    .anyRequest().authenticated();
            })
            .formLogin((formLogin)->{
                
            })
            .sessionManagement((sessionManager)->{
                sessionManager
                    .sessionFixation().changeSessionId()
                    .maximumSessions(1)
                    .expiredSessionStrategy(null)
                    .maxSessionsPreventsLogin(false)
                    .sessionRegistry(null);
            })           
            .httpBasic((httpBasic)->{});

        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails users[] = new UserDetails[2];
        users[0] = User.withUsername("keesun").password(PasswordEncoder().encode("123")).roles("USER").build();
        users[1] = User.withUsername("admin").password(PasswordEncoder().encode("!@#")).roles("ADMIN").build();

        return new InMemoryUserDetailsManager(users);
    }

    @Bean
    PasswordEncoder PasswordEncoder () {
    	return new BCryptPasswordEncoder();
    }
}
