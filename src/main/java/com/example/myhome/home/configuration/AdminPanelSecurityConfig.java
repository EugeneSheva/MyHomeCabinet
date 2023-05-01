package com.example.myhome.home.configuration;

import com.example.myhome.home.service.AdminService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
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
    private AdminService adminDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder2() {return new BCryptPasswordEncoder(12);}

    /*
    Использование AuthenticationManager в этой версии спринга уничтожает возможность делать тесты,
    выкидывает StackOverflowError , надо или удалить ссылку на имеющийся, или вставить новый @MockBean
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }

    @Bean
    public PersistentTokenBasedRememberMeServices rememberMeServicesAdmin() {
        return new PersistentTokenBasedRememberMeServices("secretKey", adminDetailsService, persistentTokenRepository());
    }

    @Bean
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {

        log.info("setting up admin filter chain");

        http.antMatcher("/admin/**")
                .authorizeRequests()
                .antMatchers("/dist/**").permitAll()
                .antMatchers("/images/**").permitAll()
                .antMatchers("/statistics").hasAuthority("statistics.read")
                .antMatchers(HttpMethod.GET,"/cashbox/**").hasAuthority("cashbox.read")
                .antMatchers(HttpMethod.POST, "/cashbox/**").hasAuthority("cashbox.write")
                .antMatchers(HttpMethod.GET,"/invoices/**").hasAuthority("invoices.read")
                .antMatchers(HttpMethod.POST, "/invoices/**").hasAuthority("invoices.write")
                .antMatchers(HttpMethod.GET,"/accounts/**").hasAuthority("accounts.read")
                .antMatchers(HttpMethod.POST, "/accounts/**").hasAuthority("accounts.write")
                .antMatchers(HttpMethod.GET,"/apartments/**").hasAuthority("apartments.read")
                .antMatchers(HttpMethod.POST, "/apartments/**").hasAuthority("apartments.write")
                .antMatchers(HttpMethod.GET,"/owners/**").hasAuthority("owners.read")
                .antMatchers(HttpMethod.POST, "/owners/**").hasAuthority("owners.write")
                .antMatchers(HttpMethod.GET,"/buildings/**").hasAuthority("buildings.read")
                .antMatchers(HttpMethod.POST, "/buildings/**").hasAuthority("buildings.write")
                .antMatchers(HttpMethod.GET,"/messages/**").hasAuthority("messages.read")
                .antMatchers(HttpMethod.POST, "/messages/**").hasAuthority("messages.write")
                .antMatchers(HttpMethod.GET,"/meters/**").hasAuthority("meters.read")
                .antMatchers(HttpMethod.POST, "/meters/**").hasAuthority("meters.write")
                .antMatchers(HttpMethod.GET,"/requests/**").hasAuthority("requests.read")
                .antMatchers(HttpMethod.POST, "/requests/**").hasAuthority("requests.write")
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
