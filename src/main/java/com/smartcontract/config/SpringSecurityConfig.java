package com.smartcontract.config;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import static org.slf4j.LoggerFactory.getLogger;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger LOGGER = getLogger(SpringSecurityConfig.class);

    @Value("${admin.login}")
    private String adminLogin;
    @Value("${admin.password}")
    private String adminPassword;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(
                        "/",
                        "/bytecode/**",
                        "/api/bytecode/**",
                        "/api/solidityFiles/sourceCode",
                        "/login",
                        "/swagger-ui.html",
                        "/webjars/**", "/css/**", "/js/**", "/highlight/**").permitAll()
                .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .permitAll()
                    .and()
                .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/");
    }


    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        LOGGER.info("Admin account login: {} password: {}", adminLogin, adminPassword);
        UserDetails userDetails = User
                .builder()
                .passwordEncoder(passwordEncoder::encode)
                .username(adminLogin)
                .password(adminPassword)
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(userDetails);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
