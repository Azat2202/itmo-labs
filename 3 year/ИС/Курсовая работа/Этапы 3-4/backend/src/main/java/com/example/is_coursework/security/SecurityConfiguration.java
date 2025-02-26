package com.example.is_coursework.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    @Value("${spring.security.oauth2.issuerUrl}")
    private String issuerUrl;

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfiguration = new CorsConfiguration();
                    corsConfiguration.setAllowedOriginPatterns(List.of("*"));
                    corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH"));
                    corsConfiguration.setAllowedHeaders(List.of("*"));
                    corsConfiguration.setAllowCredentials(true);
                    return corsConfiguration;
                }))
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(configurer -> configurer
                        .authenticationEntryPoint(
                                (request, response, exception) ->
                                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED))
                        .accessDeniedHandler(
                                (request, response, exception) -> response.setStatus(HttpServletResponse.SC_FORBIDDEN)))
                .authorizeHttpRequests(configurer -> configurer
                        .requestMatchers("/api/admin/**")
                        .hasRole("ADMIN")
                        .requestMatchers("/api/**")
                        .authenticated()
                        .anyRequest()
                        .permitAll())
                .oauth2ResourceServer((oauth2) -> oauth2
                        .jwt()
                        .jwtAuthenticationConverter(new CustomJwtAuthenticationConverter()))
                .anonymous(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return JwtDecoders.fromIssuerLocation(issuerUrl);
    }

    public class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

        private JwtAuthenticationConverter jwtAuthenticationConverter;
        private JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter;


        public CustomJwtAuthenticationConverter() {
            this.jwtAuthenticationConverter = new JwtAuthenticationConverter();
            this.jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        }
        @Override
        public AbstractAuthenticationToken convert(Jwt jwt) {
            Collection<GrantedAuthority> authorities = extractAuthorities(jwt);

            return new JwtAuthenticationToken(jwt, authorities);
        }

        private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
            if(jwt.getClaim("realm_access") != null) {
                Map<String, Object> realmAccess = jwt.getClaim("resource_access");
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> bunkerRoles = mapper.convertValue(realmAccess.get("bunker"), new TypeReference<Map<String, Object>>(){});
                if (bunkerRoles == null || bunkerRoles.isEmpty()) return List.of();
                List<String> roles = mapper.convertValue(bunkerRoles.get("roles"), new TypeReference<List<String>>(){});
                if (roles == null || roles.isEmpty()) return List.of();
                List<GrantedAuthority> authorities = new ArrayList<>();

                for (String role : roles) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
                }

                return authorities;
            }
            return new ArrayList<>();
        }
    }

}
