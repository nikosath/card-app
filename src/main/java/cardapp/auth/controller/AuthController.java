package cardapp.auth.controller;

import cardapp.auth.model.dto.TokenDto;
import cardapp.common.constant.Uri;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthController {
    public static final int JWT_EXPIRY_TIME_MINUTES = 60;
    private final JwtEncoder jwtEncoder;

    @PostMapping(Uri.TOKEN)
    public TokenDto token(Authentication authentication) {
        List<String> authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .subject(authentication.getName())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(JWT_EXPIRY_TIME_MINUTES, ChronoUnit.MINUTES))
                .claim("scope", authorities)
                .build();

        String token = jwtEncoder.encode(JwtEncoderParameters.from(claimsSet))
                .getTokenValue();
        return new TokenDto(token);
    }

}