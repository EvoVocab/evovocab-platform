package com.evovocab.shared.utils;

import jakarta.validation.ConstraintValidatorContext;

public final class ConstraintValidatorUtils {

    private ConstraintValidatorUtils() {}

    public static boolean fail(ConstraintValidatorContext ctx, String field, String message) {
        ctx.buildConstraintViolationWithTemplate(message).addPropertyNode(field).addConstraintViolation();

        return false;
    }
}
