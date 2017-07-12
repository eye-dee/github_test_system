package com.epam.testsystem.github.web;

import com.epam.testsystem.github.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.epam.testsystem.github.EnvironmentConstant.SPRING_PROFILE_DEV;
import static com.epam.testsystem.github.EnvironmentConstant.SPRING_PROFILE_PROD;

/**
 * github_test
 * Create on 7/11/2017.
 */

@Configuration
@EnableWebSecurity
@Profile(value = {SPRING_PROFILE_PROD, SPRING_PROFILE_DEV})
public class WebSecurityConfig extends WebSecurityConfigurerAdapter  {

    @Autowired
    private UserService userService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(
                        "/static/**",
                        "/registration/**",
                        "/login.html"
                ).permitAll()
                .anyRequest().authenticated();

        http.formLogin()
                .loginPage("/login.html")
                .usernameParameter("username")
                .passwordParameter("password")
                .permitAll().defaultSuccessUrl("/");

        http.logout()
                .logoutSuccessUrl("/")
                .permitAll();

        http.csrf().disable();
    }

    @Autowired
    public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }
}
