package cardapp.auth;

import cardapp.common.TestConstants;
import cardapp.common.Uri;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static cardapp.auth.RoleEnum.ADMIN;
import static cardapp.auth.RoleEnum.MEMBER;
import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableJpaAuditing
public class SecurityConfig {

    public static final String JWT_DECODER_PREFIX = "SCOPE_";

    public static String toPrefixedAuthority(RoleEnum roleEnum) {
        return JWT_DECODER_PREFIX + roleEnum.getAuthority();
    }

    @Bean
    public AuditorAware<UserProfileEntity> auditorProvider() {
        return () -> Optional.of(UserContext.getCurrentUser());
    }

    @Bean
    public KeyPair generateRsaKeys() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    @Bean
    JwtDecoder jwtDecoder(KeyPair keyPair) {
        return NimbusJwtDecoder
                .withPublicKey((RSAPublicKey) keyPair.getPublic())
                .build();
    }

    @Bean
    JWKSource<SecurityContext> jwkSource(KeyPair keyPair) {
        RSAKey rsaKey = new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                .privateKey(keyPair.getPrivate())
                .keyID(UUID.randomUUID().toString())
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    @Bean
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic(withDefaults())
                .oauth2ResourceServer(oauth -> oauth.jwt(withDefaults())) // enabling jwt authentication
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(Uri.ERROR).authenticated()
                        .requestMatchers(Uri.CARDS + "/**").hasAnyAuthority(
                                toPrefixedAuthority(MEMBER), toPrefixedAuthority(ADMIN))
                        .requestMatchers(Uri.TOKEN).hasAnyAuthority(MEMBER.getAuthority(), ADMIN.getAuthority())
                        .requestMatchers("/swagger-ui/**", "/api-docs/**").permitAll() // for testing, disable for production
                        .requestMatchers(toH2Console()).permitAll() // for testing, disable for production
                        .anyRequest().denyAll()
                )
                // For testing, disable for production
                .headers(headers -> headers.frameOptions(Customizer.withDefaults()).disable())
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

    @Bean
    UserDetailsService userDetailsService(UserProfileDao userProfileDao) {
        // TODO: don't use email as username
        var userMember1 = newUser(TestConstants.MEMBER1_USERNAME, TestConstants.MEMBER1_PASSWORD, MEMBER);
        var userMember2 = newUser(TestConstants.MEMBER2_USERNAME, TestConstants.MEMBER2_PASSWORD, MEMBER);
        var userAdmin1 = newUser(TestConstants.ADMIN1_USERNAME, TestConstants.ADMIN1_PASSWORD, ADMIN);
        userProfileDao.saveAll(List.of(userMember1, userMember2, userAdmin1));
        return userProfileDao::findByUsername;
    }

    private UserProfileEntity newUser(String username, String password, RoleEnum role) {
        return new UserProfileEntity(username, passwordEncoder().encode(password), role);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}