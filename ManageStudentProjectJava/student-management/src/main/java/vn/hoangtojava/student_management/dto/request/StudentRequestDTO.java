package vn.hoangtojava.student_management.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.hoangtojava.student_management.dto.validator.EnumValue;
import vn.hoangtojava.student_management.dto.validator.PhoneNumber;
import vn.hoangtojava.student_management.util.Status;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class StudentRequestDTO implements Serializable {

    @NotNull(message =  "studentCode must be not null")
    private String studentCode;

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

    @NotEmpty(message =  "Subjects mut be not empty")
    private Set<SubjectDTO> subjects;

//    public String getFirstName(){
//        return firstName;
//    }
//    public void setFirstName(String firstName){
//        this.firstName = firstName;
//    }
//    public String getLastName(){
//        return lastName;
//    }
//    public void setLastName(String lastName){
//        this.lastName = lastName;
//    }
//    public String getEmail(){
//        return email;
//    }
//    public void setEmail(String email){
//        this.email = email;
//    }
//    public String getPhone(){
//        return phone;
//    }
//    public void setPhone(String phone){
//        this.phone = phone;
//    }
//    public Long getAge(){
//        return age;
//    }
//    public void setAge(Long age){
//        this.age = age;
//    }
//    public Set<SubjectDTO> getSubject(){
//        return subjects;
//    }
//    public void setSubject(Set<SubjectDTO> subjects){
//        this.subjects = subjects;
//    }
//    public String getStatus(){
//        return status;
//    }
//    public void setStatus(String status){
//        this.status = status;
//    }



    public StudentRequestDTO(String firstName, String lastName, String email, String phone, String status, Integer age, Set<SubjectDTO> subject){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.status = status;
        this.age = age;
        this.subjects = subject;
    }

}
