package com.example.myhome.home.configuration.security;

import lombok.extern.java.Log;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpSession;

@Configuration
@EnableWebSecurity
@Order(-1)
@Log
public class AdminPanelSecurityConfig {

    @Bean
    public UserDetailsService adminDetailsService() {
        return new CustomAdminDetailsService();
    }

    @Bean
    public PasswordEncoder passwordEncoder2() {return new BCryptPasswordEncoder();}

    @Bean
    public DaoAuthenticationProvider authenticationProviderAdmin(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(adminDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder2());

        return authenticationProvider;
    }

    @Bean
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {

        log.info("setting up admin filter chain");

        http.authenticationProvider(authenticationProviderAdmin());
        http.userDetailsService(adminDetailsService());


        http.antMatcher("/admin/**")
                .authorizeRequests()
                .antMatchers("/dist/**").permitAll()
                .antMatchers("/images/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                    .loginPage("/admin/site/login").permitAll()
                    .loginProcessingUrl("/admin/site/login")
                    .defaultSuccessUrl("/admin")
                    .successHandler(successHandler())
                .and()
                    .logout()
                        .logoutSuccessUrl("/admin/site/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                .and()
                .rememberMe()
                .userDetailsService(adminDetailsService())
                .tokenValiditySeconds(600)
                .key("secretKey");

        return http.build();
    }

    private AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {
            CustomAdminDetails details = (CustomAdminDetails) authentication.getPrincipal();
            HttpSession session = request.getSession();
            session.setAttribute("admin", details);
            response.sendRedirect("/admin");
        };
    }

}
