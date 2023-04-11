package com.example.myhome.home.configuration.security;

import com.example.myhome.home.model.Owner;
import com.example.myhome.home.service.OwnerService;
import com.example.myhome.util.UserRole;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpSession;

@Configuration
@EnableWebSecurity
@Log
public class WebSecurityConfig{

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public PasswordEncoder passwordEncoder1() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProviderOwner(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder1());
        authenticationProvider.setUserDetailsService(userDetailsService());

        return authenticationProvider;
    }

    @Bean
    public SecurityFilterChain cabinetSecurityFilterChain(HttpSecurity http) throws Exception {

        log.info("setting up cabinet filter chain");

        http.authenticationProvider(authenticationProviderOwner());
        http.userDetailsService(userDetailsService());

        http
                .antMatcher("/cabinet/**")
                .authorizeRequests()
                    .antMatchers("/dist/**").permitAll()
                    .antMatchers("/images/**").permitAll()
                    .anyRequest().authenticated()
                .and()
                .formLogin()
                    .loginPage("/cabinet/site/login").permitAll()
                    .defaultSuccessUrl("/cabinet", true)
                    .successHandler(successHandler())
                .and()
                .logout();

        return http.build();
    }


    private AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {
            CustomUserDetails details = (CustomUserDetails) authentication.getPrincipal();
            HttpSession session = request.getSession();
            session.setAttribute("user", details);
            response.sendRedirect("/cabinet");
        };
    }

}
