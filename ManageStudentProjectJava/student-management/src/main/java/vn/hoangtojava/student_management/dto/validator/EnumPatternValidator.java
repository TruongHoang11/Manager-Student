package vn.hoangtojava.student_management.dto.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class EnumPatternValidator implements ConstraintValidator<EnumValue, CharSequence> {

    private List acceptedValues;
    @Override
    public void initialize(EnumValue enumValue) {
        acceptedValues = acceptedValues = Stream.of(enumValue.enumClass().getEnumConstants())
                .map(Enum::name)
                .toList();
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext constraintValidatorContext) {
        if(value == null)   return true;

        return acceptedValues.contains(value.toString().toUpperCase());
    }
}
