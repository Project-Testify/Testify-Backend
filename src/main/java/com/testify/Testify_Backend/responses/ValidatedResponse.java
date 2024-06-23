package com.testify.Testify_Backend.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class ValidatedResponse<entity> {
    @JsonProperty("validation_violations")
    public List<ValidationViolation> validationViolations =
            new java.util.ArrayList<>();

    public void setValidationViolationsFromConstraintViolations(
            Set<ConstraintViolation<entity>> constraintViolations) {
        if (constraintViolations != null && !constraintViolations.isEmpty()) {
            for (ConstraintViolation<?> violation : constraintViolations) {
                addError(violation);
            }
        }
    }

    private void addError(ConstraintViolation<?> violation) {
        validationViolations.add(
                new ValidationViolation(violation.getPropertyPath().toString(),
                        violation.getMessage()
                ));
    }

    public void addError(String field, String message) {
        validationViolations.add(new ValidationViolation(field, message));
    }

    public boolean hasErrors() {
        return !validationViolations.isEmpty();
    }

    public boolean checkValidity(entity entity) {
        var factory    = Validation.buildDefaultValidatorFactory();
        var validator  = factory.getValidator();
        var violations = validator.validate(entity);
        if (!violations.isEmpty()) {
            setValidationViolationsFromConstraintViolations(violations);
        }
        return !hasErrors();
    }
}
