package se.biplob.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {

        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(auth -> auth
                        .pathMatchers(
                                "/auth/**",
                                "/actuator/**"
                        ).permitAll()

                        .pathMatchers("/api/v1/bookings/**")
                        .hasAnyRole("PATIENT", "ADMIN")

                        .pathMatchers("/api/v1/departments/**")
                        .hasRole("ADMIN")

                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth ->
                        oauth.jwt(Customizer.withDefaults())
                );

        return http.build();
    }

//    @Bean
//    public ReactiveJwtAuthenticationConverter jwtAuthenticationConverter() {
//        ReactiveJwtAuthenticationConverter converter =
//                new ReactiveJwtAuthenticationConverter();
//
//        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
//            List<String> roles = jwt.getClaimAsStringList("roles");
//            return roles.stream()
//                    .map(role -> "ROLE_" + role)
//                    .map(SimpleGrantedAuthority::new)
//                    .collect(Collectors.toList());
//        });
//
//        return converter;
//    }

}
