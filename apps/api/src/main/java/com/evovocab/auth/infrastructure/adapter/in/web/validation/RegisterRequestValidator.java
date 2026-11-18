package com.evovocab.auth.infrastructure.adapter.in.web.validation;

import com.evovocab.auth.infrastructure.adapter.in.web.dto.RegisterRequest;
import com.evovocab.shared.utils.ConstraintValidatorUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class RegisterRequestValidator implements ConstraintValidator<ValidRegisterRequest, RegisterRequest> {

    private static final Pattern FULL_NAME_PATTERN = Pattern.compile("^[\\p{L}\\s]+$");

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^(?=.{1,64}@)[A-Za-z0-9_+-]+(\\.[A-Za-z0-9_+-]+)*@[A-Za-z0-9]([A-Za-z0-9-]*[A-Za-z0-9])?(\\.[A-Za-z0-9]([A-Za-z0-9-]*[A-Za-z0-9])?)*\\.[A-Za-z]{2,}$"
    );

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
        "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])" + "(?=.*[\\p{P}\\p{S}])(?=\\p{ASCII}+$)" + "(?!\\s|.*\\s$).*$"
    );

    @Override
    public boolean isValid(RegisterRequest request, ConstraintValidatorContext ctx) {
        if (request == null) {
            return true;
        }

        boolean fullNameValid = validateFullName(request.fullName(), ctx);
        boolean emailValid = validateEmail(request.email(), ctx);
        boolean passwordValid = validatePassword(request.password(), ctx);

        return fullNameValid && emailValid && passwordValid;
    }

    private boolean validateFullName(String fullName, ConstraintValidatorContext ctx) {
        if (fullName == null || fullName.isBlank()) {
            return ConstraintValidatorUtils.fail(ctx, "fullName", "Họ và tên không được để trống");
        }

        if (fullName.length() < 2 || fullName.length() > 100 || !FULL_NAME_PATTERN.matcher(fullName).matches()) {
            return ConstraintValidatorUtils.fail(ctx, "fullName", "Họ và tên không hợp lệ");
        }

        return true;
    }

    private boolean validateEmail(String email, ConstraintValidatorContext ctx) {
        if (email == null || email.isBlank()) {
            return ConstraintValidatorUtils.fail(ctx, "email", "Email không được để trống");
        }

        if (email.length() > 100 || !EMAIL_PATTERN.matcher(email).matches()) {
            return ConstraintValidatorUtils.fail(ctx, "email", "Email không hợp lệ");
        }

        return true;
    }

    private boolean validatePassword(String password, ConstraintValidatorContext ctx) {
        if (password == null || password.isBlank()) {
            return ConstraintValidatorUtils.fail(ctx, "password", "Mật khẩu không được để trống");
        }

        if (password.length() < 8 || password.length() > 72 || !PASSWORD_PATTERN.matcher(password).matches()) {
            return ConstraintValidatorUtils.fail(ctx, "password", "Mật khẩu không hợp lệ");
        }

        return true;
    }
}
