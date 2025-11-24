package vn.hoangtojava.student_management.dto.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.annotation.Annotation;

public class PhoneValidator  implements ConstraintValidator<PhoneNumber, String> {
    @Override
    public void initialize(PhoneNumber phoneNumberNo) {

    }

    @Override
    public boolean isValid(String phoneNo, ConstraintValidatorContext constraintValidatorContext) {
        if (phoneNo == null) {
            return false;
        }
        // 10 so
        if (phoneNo.matches("\\d{10}")) return true;
            // 090-234-345s
        else if (phoneNo.matches("\\d{3}[-]\\d{3}[-]\\d{4}")) return true;
            // sdt vn: 0969498606
        else  return (phoneNo.matches("^(0[0-9]{9})$")) ;
    }

}
