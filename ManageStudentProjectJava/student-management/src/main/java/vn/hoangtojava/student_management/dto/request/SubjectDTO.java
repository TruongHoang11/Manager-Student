package vn.hoangtojava.student_management.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubjectDTO implements Serializable {
    @NotBlank(message = "subjectCode must be not blank")
    private String subjectCode;
    @NotBlank(message = "subjectCode must be not blank")
    private String subjectName;
    @NotNull(message = "subjectCode must be not null")
    private Integer credit;

}
