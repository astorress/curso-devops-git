/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.paymentchain.billing.common;

import java.time.Duration;
import java.util.Arrays;
import java.util.stream.Stream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;

/**
 *
 * @author sotobotero
 *
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private static final String[] NO_AUTH_LIST = { //
        "/v2/api-docs", //
        "/configuration/ui", //
        "/swagger-resources", //
        "/configuration/security", //
        "/swagger-ui.html", //
        "/webjars/**" //
    };

    @Bean
    public UserDetailsService users() {
        UserDetails user = User.builder()
                .username("api-user")
                .password(passwordEncoder().encode("password"))
                .roles("USER")
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("admin"))
                .roles("USER", "ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user, admin);
    }
 @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults())
                .authorizeHttpRequests((authz) -> {
                    // Use lambda and stream to create AntPathRequestMatcher instances
                    authz.requestMatchers(Stream.of(NO_AUTH_LIST)
                            .map(AntPathRequestMatcher::new)
                            .toArray(AntPathRequestMatcher[]::new))
                            .permitAll();
                    authz.anyRequest().authenticated();
                })
                .csrf(csrf -> csrf.disable())
              .cors(corsCustomizer()); // Use the custom CorsCustomizer
        return http.build();
    }     
   
    @Bean
    public Customizer<CorsConfigurer<HttpSecurity>> corsCustomizer() {
        return (cors) -> {
            CorsConfiguration cc = new CorsConfiguration();
            cc.setAllowedHeaders(Arrays.asList("Origin,Accept", "X-Requested-With", "Content-Type", "Access-Control-Request-Method", "Access-Control-Request-Headers", "Authorization"));
            cc.setExposedHeaders(Arrays.asList("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
            cc.setAllowedOrigins(Arrays.asList("/*"));
            cc.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "PUT", "PATCH","DELETE"));
            cc.addAllowedOriginPattern("*");
            cc.setMaxAge(Duration.ZERO);
            cc.setAllowCredentials(Boolean.TRUE);
            cors.configurationSource(request -> cc);
        };
    }

}
