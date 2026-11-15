package com.evovocab.user.application.port.in;

public record CreateUserByEmailCommand(String fullName, String email, String password) {}
