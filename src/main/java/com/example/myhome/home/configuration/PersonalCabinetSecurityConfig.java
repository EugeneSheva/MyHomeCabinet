package com.example.myhome.home.configuration;
import com.example.myhome.home.service.impl.OwnerServiceImpl;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import javax.sql.DataSource;


@Configuration
@EnableWebSecurity
@Log
public class PersonalCabinetSecurityConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    public UserDetailsService userDetailsService() {
        return new OwnerServiceImpl();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProviderOwner(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(userDetailsService());
        return authenticationProvider;
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }

    @Bean
    public SecurityFilterChain cabinetSecurityFilterChain(HttpSecurity http) throws Exception {

        log.info("setting up cabinet filter chain");

        http
                .antMatcher("/cabinet/**")
                .authorizeRequests()
                .antMatchers("/dist/**").permitAll()
                .antMatchers("/images/**").permitAll()
                .antMatchers("/cabinet/site/register/**").permitAll()
                .antMatchers("/reset-password/**", "/reset-password", "reset-password", "/**/reset-password").permitAll()
                .antMatchers("/forgot-password/**", "/forgot-password", "forgot-password", "/**/forgot-password").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/cabinet/site/login").permitAll()
                .defaultSuccessUrl("/cabinet", true)
//                    .failureUrl("/cabinet/site/login?error=true")
                .and()
                .logout()
                .logoutSuccessUrl("/cabinet/site/login")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID", "remember-me")
                .and()
                .rememberMe()
                .userDetailsService(userDetailsService())
                .key("secretKey")
                .tokenValiditySeconds(6000);

        return http.build();
    }
}