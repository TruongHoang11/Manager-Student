package vn.hoangtojava.student_management.model;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import org.hibernate.annotations.Cache;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
public class StudentSubjectKey implements Serializable {

    @Column(name ="student_id")
    private Long studentId;
    @Column(name = "subject_id")
    private Long subjectId;
    public StudentSubjectKey() {}

    public StudentSubjectKey(Long studentId, Long subjectId) {
        this.studentId = studentId;
        this.subjectId = subjectId;
    }


    public Long getStudentId() {
        return studentId;
    }

    public Long getSubjectId() {
        return subjectId;
    }


    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudentSubjectKey that)) return false;
        return Objects.equals(studentId, that.studentId)
                && Objects.equals(subjectId, that.subjectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, subjectId);
    }

}

