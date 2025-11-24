package vn.hoangtojava.student_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.hoangtojava.student_management.model.Student;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {
    Optional<Student> findByStudentCode(String studentCode);

    @Query("""
            SELECT s 
            FROM Student s
            LEFT JOIN FETCH s.studentSubjects ss
            LEFT JOIN FETCH ss.subject
            WHERE s.id = :studentId
            """)
    Optional<Student> findStudentWithSubjects(@Param("studentId") Long studentId);
}
