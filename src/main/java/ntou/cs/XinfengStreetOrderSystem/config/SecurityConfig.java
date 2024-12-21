package ntou.cs.XinfengStreetOrderSystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // 停用 CSRF
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // 開放所有 API
            );
        System.out.println("SecurityConfig initialized: All APIs are open");
        return http.build();
    }
}
