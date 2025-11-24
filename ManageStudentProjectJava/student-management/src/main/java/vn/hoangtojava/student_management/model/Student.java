package vn.hoangtojava.student_management.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import vn.hoangtojava.student_management.dto.request.SubjectDTO;
import vn.hoangtojava.student_management.util.Status;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Setter
@Table(name= "tbl_student")
public class Student extends AbstractEntity {

    @Column(name = "student_code")
    private String studentCode;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "email")
    private String email;
    @Column(name = "phone")
    private String phone;
    @Column(name ="age")
    private int age;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column (name = "status")
    private Status status;


    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @JsonManagedReference("student-ref") //cha trong quan he Student <-> StudentSubject
    @JsonIgnore
    private Set<StudentSubject> studentSubjects = new HashSet<>();

//    public void saveSubjects(Subject subject){
//        if(studentSubjects == null){
//            studentSubjects = new HashSet<>();
//        }
//        StudentSubject ss = new StudentSubject(this, subject);
//        studentSubjects.add(ss);
//        subject.setStudentSubjects(studentSubjects);
//
//    }
public void saveSubjects(Subject subject) {
    if (studentSubjects == null) studentSubjects = new HashSet<>();

    // đảm bảo subject.studentSubjects không null
    if (subject.getStudentSubjects() == null) {
        subject.setStudentSubjects(new HashSet<>());
    }

    StudentSubject ss = new StudentSubject(this, subject);
    studentSubjects.add(ss);
    subject.getStudentSubjects().add(ss);
}

}
