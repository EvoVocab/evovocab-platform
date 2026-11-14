package com.evovocab.user.application.port.out;

import java.util.UUID;

public record UserResult(UUID id, String fullName, String email, String status, String provider) {}
