package vn.hoangtojava.student_management.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Builder
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EnrollSubjectResponse implements Serializable {

    private String studentCode;
    private String firstName;
    private String lastName;

    private List<String> addedSubjects;
    private List<String> existSubjects;
}
