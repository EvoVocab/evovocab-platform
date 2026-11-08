package com.evovocab.shared.dto.requests;

import java.time.Instant;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenRequestDto {

    private UUID id;
    private String subject;
    private Instant issuedAt;
    private Instant expiredAt;
}
