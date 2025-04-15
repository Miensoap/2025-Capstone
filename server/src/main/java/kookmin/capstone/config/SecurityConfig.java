package kookmin.capstone.config;

import kookmin.capstone.auth.DeviceAuthenticationFilter;
import kookmin.capstone.auth.JwtProvider;
import kookmin.capstone.auth.UserAuthenticationFilter;
import kookmin.capstone.repository.DeviceRepository;
import kookmin.capstone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(userAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(deviceAuthenticationFilter(), UserAuthenticationFilter.class)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/device/**").hasAuthority("DEVICE")
                        .requestMatchers("/api/user/**").hasAuthority("USER")
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    @Bean
    public DeviceAuthenticationFilter deviceAuthenticationFilter() {
        return new DeviceAuthenticationFilter(deviceRepository);
    }

    @Bean
    public UserAuthenticationFilter userAuthenticationFilter() {
        return new UserAuthenticationFilter(jwtProvider, userRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
