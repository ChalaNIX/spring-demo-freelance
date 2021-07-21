package ua.in.lsrv.freelance.validation;

import ua.in.lsrv.freelance.annotation.PasswordMatches;
import ua.in.lsrv.freelance.request.SignUpRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatcherValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        SignUpRequest signUpRequest = (SignUpRequest) object;
        return signUpRequest.getPassword().equals(signUpRequest.getConfirmPassword());
    }
}
