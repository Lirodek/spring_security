package com.security.demosecurity.config;

import com.security.demosecurity.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig implements WebSecurityCustomizer {
    
    @Autowired AccountService accountService;
    @Value("${member.role.admin}") String ADMIN;
    @Value("${member.role.user}") String USER;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((authRequest)->{
                authRequest
                    .requestMatchers("/", "/info", "/account/**").permitAll()
                    .requestMatchers("/user").hasRole(USER)
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

        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);

        return http.build();
    }


    @Override
    public void customize(WebSecurity web) {
        web.ignoring().requestMatchers("/favicon.ico");
    }
}
