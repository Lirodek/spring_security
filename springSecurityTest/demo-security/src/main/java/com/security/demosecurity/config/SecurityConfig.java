package com.security.demosecurity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.security.demosecurity.account.AccountService;

@Configuration
@EnableWebSecurity
public class SecurityConfig{
    
    @Autowired AccountService accountService;
    @Value("${member.role.admin}") String ADMIN;
    @Value("${member.role.user}") String USER;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((authRequest)->{
                authRequest
                    .requestMatchers("/", "/info", "/account/**").permitAll()
                    .requestMatchers("/admin").hasRole(ADMIN)
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

    
}
