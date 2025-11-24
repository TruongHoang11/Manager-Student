package vn.hoangtojava.student_management.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;

@Builder
@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class StudentDetailResponse implements Serializable {
    private Long id;
    private String studentCode;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Integer age;
    private String status;
    private Set<String> subjects;

    public StudentDetailResponse(Long id, String studentCode, String lastName, String email){
        this.id = id;
        this.studentCode = studentCode;
        this.lastName = lastName;
        this.email = email;
    }
}
