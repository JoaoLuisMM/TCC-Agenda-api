package br.com.joaoluis.tcc.config.security;

import br.com.joaoluis.tcc.seguranca.model.UsuarioLogado;
import br.com.joaoluis.tcc.seguranca.model.UsuarioSistema;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.io.InputStream;
import java.security.KeyStore;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Configuration
public class AuthServerConfig {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ApiProperty apiProperty;

    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        RegisteredClient registeredClient = RegisteredClient
                .withId(UUID.randomUUID().toString())
                .clientId("tccJoaoLuis")
                .clientSecret(passwordEncoder.encode("Jo@oLu15"))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
//                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .redirectUris(uris -> uris.addAll(apiProperty.getSeguranca().getRedirectsPermitidos()))
                .scope("read")
                .scope("write")
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofMinutes(30))
                        .refreshTokenTimeToLive(Duration.ofDays(24))
                        .build())
                .clientSettings(ClientSettings.builder()
                        // Ativa ou desativa a tela de consentimento do OAUTH2.
                        .requireAuthorizationConsent(false)
                        .build())
            .build();

        return new InMemoryRegisteredClientRepository(registeredClient);
    }

    //Onde você define os Endpoints do Protocolo OAuth2
    @Bean
    @Order(1)
    public SecurityFilterChain authServerFilterChain(HttpSecurity httpSecurity) throws Exception{
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(httpSecurity);
        httpSecurity.formLogin(form -> form.loginPage("/login").isCustomLoginPage());
        httpSecurity.exceptionHandling((exceptions) -> exceptions
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")))
//                .oauth2ResourceServer()
//                .getConfigurer(OAuth2AuthorizationServerConfigurer.class)
//                .oidc(Customizer.withDefaults())
        ;
        return httpSecurity.build();
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtBuildCustomizer() {
        return (context) -> {
            Authentication authenticationToken = context.getPrincipal();
            UsuarioSistema usuarioSistema = (UsuarioSistema) authenticationToken.getPrincipal();
            Set<String> authorities = new HashSet<>();
            for (GrantedAuthority grantedAuthority : usuarioSistema.getAuthorities()) {
                authorities.add(grantedAuthority.getAuthority());
            }
            context.getClaims().claim("usuario", new UsuarioLogado(usuarioSistema));
            context.getClaims().claim("authorities", authorities);
        };
    }

    @Bean
    public JWKSet jwkSet() throws Exception {
        final InputStream inputStream = new ClassPathResource("keystore/chaves-weblic.jks").getInputStream();
        final KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(inputStream, "um@senh@qu@lquer".toCharArray());
        RSAKey rsaKey = RSAKey.load(keyStore, "weblic-servicos", "um@senh@qu@lquer".toCharArray());
        return new JWKSet(rsaKey);
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource(JWKSet jwkSet) {
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    @Bean
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

    // quem criou o token
    @Bean
    public AuthorizationServerSettings providerSettings() {
        return AuthorizationServerSettings.builder()
                .issuer(apiProperty.getSeguranca().getAuthServerUrl())
                .build();
    }
}
