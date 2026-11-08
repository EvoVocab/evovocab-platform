package com.evovocab.security.adapters;

import com.evovocab.security.properties.JwtProperties;
import com.evovocab.shared.dto.requests.TokenRequestDto;
import com.evovocab.shared.providers.TokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtAdapter implements TokenProvider {

    private final JwtProperties jwtProperties;

    private SecretKey secretKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getSecret()));
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        Instant expiresIn = Instant.now().plus(jwtProperties.getAccessTokenExpiration());
        String jwtId = UUID.randomUUID().toString();

        return Jwts.builder()
            .id(jwtId)
            .subject(userDetails.getUsername())
            .issuedAt(Date.from(Instant.now()))
            .expiration(Date.from(expiresIn))
            .signWith(secretKey())
            .compact();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(secretKey()).build().parseSignedClaims(token);
            return true;
        } catch (SignatureException e) {
            log.warn("Security warning: JWT signature is invalid: {}", e.getMessage());
            throw new JwtException("Xác thực không thành công");
        } catch (UnsupportedJwtException e) {
            log.warn("Security warning: Received unsupported token type: {}", e.getMessage());
            throw new JwtException("Xác thực không thành công");
        } catch (MalformedJwtException | IllegalArgumentException e) {
            log.debug("Invalid JWT structure or empty data: {}", e.getMessage());
            throw new JwtException("Xác thực không thành công");
        } catch (ExpiredJwtException e) {
            throw new JwtException("Xác thực không thành công");
        } catch (JwtException e) {
            log.error("Error validating JWT: {}", e.getMessage());
            throw new JwtException("Xác thực không thành công");
        }
    }

    @Override
    public TokenRequestDto extractTokenRequestDto(String token) {
        Claims claims = Jwts.parser().verifyWith(secretKey()).build().parseSignedClaims(token).getPayload();

        return TokenRequestDto.builder()
            .id(UUID.fromString(claims.getId()))
            .subject(claims.getSubject())
            .issuedAt(claims.getIssuedAt().toInstant())
            .expiredAt(claims.getExpiration().toInstant())
            .build();
    }
}
