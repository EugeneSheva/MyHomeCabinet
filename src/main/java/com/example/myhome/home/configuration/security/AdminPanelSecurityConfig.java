package com.example.myhome.home.configuration.security;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@Order(-1)
@Log
public class AdminPanelSecurityConfig {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PersistentTokenRepository repository;

    @Bean
    public UserDetailsService adminDetailsService() {
        return new CustomAdminDetailsService();
    }

    @Bean
    public PasswordEncoder passwordEncoder2() {return new BCryptPasswordEncoder(12);}

    @Bean
    public DaoAuthenticationProvider authenticationProviderAdmin(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(adminDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder2());

        return authenticationProvider;
    }

    /*
    Использование AuthenticationManager в этой версии спринга уничтожает возможность делать тесты,
    выкидывает StackOverflowError , надо или удалить ссылку на имеющийся, или вставить новый @MockBean
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

//    @Bean
//    public PersistentTokenRepository persistentTokenRepository() {
//        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
//        tokenRepository.setDataSource(dataSource);
//        return tokenRepository;
//    }

    @Bean
    public PersistentTokenBasedRememberMeServices rememberMeServicesAdmin() {
        return new PersistentTokenBasedRememberMeServices("secretKey", adminDetailsService(), repository);
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
                    .failureUrl("/admin/site/login?error=true")
                .and()
                    .logout()
                        .logoutSuccessUrl("/admin/site/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                .and()
                .rememberMe()
                    .rememberMeServices(rememberMeServicesAdmin())
                    .tokenValiditySeconds(600);

        return http.build();
    }

}
