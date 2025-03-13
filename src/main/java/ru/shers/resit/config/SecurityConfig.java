package ru.shers.resit.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.shers.resit.service.UserService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private UserService userService;
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    public void setUserService(@Lazy UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setJwtRequestFilter(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE,"/api/students/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/students/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/students/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/teachers/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/teachers/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/teachers/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/subjects/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/subjects/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/subjects/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/retakes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/retakes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/retakes/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .sessionManagement((context)->context.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
