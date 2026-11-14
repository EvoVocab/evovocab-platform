package com.evovocab.auth.application.port.in;

public record RegisterByEmailCommand(String fullName, String email, String password) {}
