package vn.hoangtojava.student_management.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.hoangtojava.student_management.dto.validator.EnumValue;
import vn.hoangtojava.student_management.dto.validator.PhoneNumber;
import vn.hoangtojava.student_management.util.Status;

import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateStudentDTO implements Serializable {
    @NotBlank(message = "firstName must be not blank")
    private String firstName;

    @NotBlank(message = "lastName must be not blank")
    private String lastName;

    //    hoangtodz1@gmail.com
    @Pattern( regexp = "^[\\w.-]+@[\\w.-]+\\.[a-zA-z]{2,6}", message = "email invalid format")
    private String email;

    @PhoneNumber
    private String phone;

    @NotNull(message = "status must be not null")
    @EnumValue(name = "status", enumClass = Status.class)
    private String status;

    @NotNull
    private Integer age;
}
