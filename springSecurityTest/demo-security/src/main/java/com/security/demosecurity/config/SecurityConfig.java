package com.security.demosecurity.config;

import com.security.demosecurity.account.AccountService;
import com.security.demosecurity.common.LoggingFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;

import java.io.IOException;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig implements WebSecurityCustomizer {
    
    @Autowired AccountService accountService;
    @Value("${member.role.admin}") String ADMIN;
    @Value("${member.role.user}") String USER;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((authRequest)->{
                authRequest
                    .requestMatchers("/", "/info", "/account/**", "/signup").permitAll()
                    .requestMatchers("/user").hasRole(USER)
                    .requestMatchers("/admin").hasRole(ADMIN)
                    .anyRequest().authenticated();
            })

            .rememberMe(remeberMeCustom->{
                remeberMeCustom
                        .alwaysRemember(true)
                        .userDetailsService(accountService)
                        .useSecureCookie(true)
                        .key("remember-me-sample");
            })

            .formLogin((formLogin)->{})
            .sessionManagement((sessionManager)->{
                sessionManager
                    .sessionFixation().changeSessionId()
                    .maximumSessions(1)
                        .expiredSessionStrategy(null)
                        .maxSessionsPreventsLogin(false)
                        .sessionRegistry(null);
            })

            .logout(logout->logout.logoutSuccessUrl("/"))
            .exceptionHandling(exceptionHandler -> {
                exceptionHandler
                        .accessDeniedPage("/access-denied")
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                            String username = principal.getUsername();
                            log.info("{} is denied to access {}", username, request.getRequestURI());
                            response.sendRedirect("/access-denied");
                        });
            })
            .addFilterBefore(new LoggingFilter(), WebAsyncManagerIntegrationFilter.class)
            //.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable())
            .httpBasic((httpBasic)->{});


        // TODO ExceptionTranslatorFilter -> FilterSecuyrityInterceptor
        // TODO AuthenticationException -> AuthenticationEntryPoint
        // TODO AccessDeniedException -> AccessDeniedHandler



        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);

        return http.build();
    }


    @Override
    public void customize(WebSecurity web) {
        web.ignoring().requestMatchers("/favicon.ico");
    }
}
