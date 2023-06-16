package com.example.myhome.home.configuration.security;
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
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
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
        return new CustomUserDetailsService();
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
    public PersistentTokenBasedRememberMeServices rememberMeServices() {
        return new PersistentTokenBasedRememberMeServices("secretKey", userDetailsService(), persistentTokenRepository());
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
                    .antMatchers("/cabinet/site/register/**").permitAll()
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
                    .rememberMeServices(rememberMeServices())
                    .tokenValiditySeconds(600);

        return http.build();
    }


}
