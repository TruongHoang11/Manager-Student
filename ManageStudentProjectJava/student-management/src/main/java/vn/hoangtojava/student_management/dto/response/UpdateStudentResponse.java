package vn.hoangtojava.student_management.dto.response;

import jakarta.validation.constraints.Pattern;
import lombok.*;
import vn.hoangtojava.student_management.dto.validator.EnumValue;
import vn.hoangtojava.student_management.dto.validator.PhoneNumber;
import vn.hoangtojava.student_management.util.Status;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UpdateStudentResponse implements Serializable {
        private String studentCode;
        private String firstName;
        private String lastName;
        //    hoangtodz1@gmail.com
        @Pattern( regexp = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}", message = "email invalid format")
        private String email;
        @PhoneNumber
        private String phone;
        @EnumValue(name = "status", enumClass = Status.class)
        private String status;
        private Integer age;

        public UpdateStudentResponse(String firstName, String lastName, String email, String phone, String status, Integer age){
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.phone = phone;
            this.status = status;
            this.age = age;

        }

    }


