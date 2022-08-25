package com.ds.auth.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class WebSecurityConfig {

    @Configuration
    static class WebSecurityAuthServer extends WebSecurityConfigurerAdapter {

        UserDetailsService userDetailsService;
        AuthenticationProvider authenticationProvider;
        PasswordEncoder passwordEncoder;
        Environment env;

        public WebSecurityAuthServer(
            UserDetailsService userDetailsService,
            AuthenticationProvider authenticationProvider,
            PasswordEncoder passwordEncoder,
            Environment env
        ) {
            this.userDetailsService = userDetailsService;
            this.authenticationProvider = authenticationProvider;
            this.passwordEncoder = passwordEncoder;
            this.env = env;
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .csrf().disable()
                .cors().disable()
                .httpBasic().and()
                .requestMatchers().and()
                .authorizeRequests()
                .antMatchers(
                        "/actuator/info",
                        "/actuator/health"
                ).permitAll()
                .mvcMatchers(
                        "/",
                        "/img/**",
                        "/docs/**",
                        "/error**",
                        "/.well-known/jwks.json"
                ).permitAll()
                .anyRequest().authenticated();
        }

        @Override
        public void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.authenticationProvider(authenticationProvider);
            auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
        }

        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }
    }

    @Order(1)
    @Configuration
    static class WebSecurityConfigResourceServer extends WebSecurityConfigurerAdapter {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            http
                .antMatcher("/api/**")
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .csrf().disable()
                .cors().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .requestMatchers().and()
                .authorizeRequests().anyRequest().authenticated().and()
                .oauth2ResourceServer().jwt();
        }
    }
}
