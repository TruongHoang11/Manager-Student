package vn.hoangtojava.student_management.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tbl_subject")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Subject extends AbstractEntity {

    @Column(name = "subject_code")
    private String subjectCode;

    @Column(name = "subject_name")
    private String subjectName;
    @Column(name = "credit")
    private int credit;


    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @JsonManagedReference("subject-ref") // cha trong quan he Subject <-> StudentSubject
    @JsonIgnore
    private Set<StudentSubject> studentSubjects = new HashSet<>();

}
