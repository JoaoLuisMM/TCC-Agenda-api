package br.com.joaoluis.tcc.config.security;

import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class ResourceServerConfig {

    @Autowired
    private ApiProperty weblicApiProperty;

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf((c) -> c.disable()) // Desabilita as configurações default do Spring Security
            .authorizeHttpRequests((authorize) -> authorize
                    .requestMatchers(AntPathRequestMatcher.antMatcher("/empresas/novo-cadastro/**")).permitAll()
                    .requestMatchers(AntPathRequestMatcher.antMatcher("/usuarios/*/nova-senha")).permitAll()
                    .requestMatchers("/styles/*", "/webfonts/*", "/images/*").permitAll()
                    .anyRequest().authenticated()
            )
            // Esta configuração se repete no método authServerFilterChain da classe AuthServerConfig
            .formLogin((form) -> form.loginPage("/login").permitAll().isCustomLoginPage())
            .logout(logoutConfig -> {
                // logoutSuccessHandler -> permite acesso a alguns objetos
                logoutConfig.logoutSuccessHandler((httpServletRequest, httpServletResponse, authentication) -> {
                    String redirectTo = httpServletRequest.getParameter("redirectTo");
                    if (!StringUtils.hasText(redirectTo)) {
                        redirectTo = weblicApiProperty.getSeguranca().getAuthServerUrl();
                    }
                    httpServletResponse.setStatus(302);
                    httpServletResponse.sendRedirect(redirectTo);
                });
            })
            .oauth2ResourceServer(configure -> configure.jwt(j -> j.jwtAuthenticationConverter(jwtAuthenticationConverter())));
        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            List<String> authorities = jwt.getClaimAsStringList("authorities");
            if (authorities == null) {
                authorities = Collections.emptyList();
            }
            JwtGrantedAuthoritiesConverter scopesAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
            Collection<GrantedAuthority> grantedAuthorities = scopesAuthoritiesConverter.convert(jwt);
            grantedAuthorities.addAll(authorities.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList()));
            return grantedAuthorities;
        });
        return jwtAuthenticationConverter;
    }
}
