package vn.hoangtojava.student_management.dto.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.FIELD})
@Constraint(validatedBy = EnumPatternValidator.class)
public @interface EnumValue {
    String name();
    String message() default"{name} must be any of enum {enumClass}";
    Class<? extends Enum <?>> enumClass();
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};
}
