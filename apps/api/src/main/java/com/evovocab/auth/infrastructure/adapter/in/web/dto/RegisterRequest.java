package com.evovocab.auth.infrastructure.adapter.in.web.dto;

import com.evovocab.auth.infrastructure.adapter.in.web.deserializer.EmailDeserializer;
import com.evovocab.auth.infrastructure.adapter.in.web.deserializer.FullNameDeserializer;
import com.evovocab.auth.infrastructure.adapter.in.web.validation.ValidRegisterRequest;
import com.evovocab.shared.jackson.SkipNfcNormalization;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@ValidRegisterRequest
public record RegisterRequest(
    @JsonDeserialize(using = FullNameDeserializer.class) String fullName,

    @JsonDeserialize(using = EmailDeserializer.class) String email,

    @SkipNfcNormalization String password
) {}
