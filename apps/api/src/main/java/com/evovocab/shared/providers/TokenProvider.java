package com.evovocab.shared.providers;

import com.evovocab.shared.dto.requests.TokenRequestDto;
import org.springframework.security.core.userdetails.UserDetails;

public interface TokenProvider {
    String generateToken(UserDetails userDetails);

    boolean validateToken(String token);

    TokenRequestDto extractTokenRequestDto(String token);
}
