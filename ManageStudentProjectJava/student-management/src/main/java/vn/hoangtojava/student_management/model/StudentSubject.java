package vn.hoangtojava.student_management.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "tbl_student_subject")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentSubject {

    @EmbeddedId
    private StudentSubjectKey id = new StudentSubjectKey(); // KHỞI TẠO NGAY

    @ManyToOne
    @MapsId("studentId") // Map voi studentId trong StudentSubjectKey
    @JoinColumn( name = "student_id")
//    @JsonBackReference("student-ref") // Con trong quan hệ Student ↔ StudentSubject
    @JsonIgnore
    private Student student;



    @ManyToOne
    @MapsId("subjectId") // map voi subjectId trong StudentSubjectKey
    @JoinColumn(name = "subject_id")
//    @JsonBackReference("subject-ref") // Con trong quan hệ Subject ↔ StudentSubject
    @JsonIgnore
    private Subject subject;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name ="enroll_date")
    private Date enrollDate;

    public StudentSubject(Student student, Subject subject) {
        this.id = new StudentSubjectKey(); // đảm bảo không null
        this.student = student;
        this.subject = subject;
        this.enrollDate = new Date();
    }


    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentSubject that = (StudentSubject) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
