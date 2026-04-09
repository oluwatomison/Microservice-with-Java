package config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;


//@Configuration
//@EnableWebSecurity
public class SecurityConfig {

    // These values are set in application.yml
    @Value("${spring.security.user.name}")
    private String eurekaUsername;

    // These values are set in application.yml
    @Value("${spring.security.user.password}")
    private String eurekaPassword;

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(auth -> auth
//                        // Permit ALL requests - completely open for development
//                        .anyRequest().permitAll()
//                )
//                .httpBasic(Customizer.withDefaults());
//
//        return http.build();
//    }

//    @Bean
//    public InMemoryUserDetailsManager userDetailsService() {
//        // Use the values from application.yml
//        UserDetails user = User.builder()
//                .username(eurekaUsername)
//                .password("{noop}" + eurekaPassword)
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(user);
//    }
}

