package vn.hoangtojava.student_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.hoangtojava.student_management.model.Student;
import vn.hoangtojava.student_management.model.StudentSubject;
import vn.hoangtojava.student_management.model.StudentSubjectKey;
import vn.hoangtojava.student_management.model.Subject;

import java.util.Optional;

@Repository
public interface StudentSubjectRepository extends JpaRepository<StudentSubject, StudentSubjectKey> {

    boolean existsByStudentAndSubject(Student student, Subject subject);
//    SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END
//    FROM student_subject
//    WHERE student_id = ? AND subject_id = ?;
}
