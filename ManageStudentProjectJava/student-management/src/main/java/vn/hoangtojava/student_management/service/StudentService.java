package vn.hoangtojava.student_management.service;

import org.springframework.data.domain.Pageable;
import vn.hoangtojava.student_management.dto.request.CreateStudentDTO;
import vn.hoangtojava.student_management.dto.response.PageResponse;
import vn.hoangtojava.student_management.dto.response.UpdateStudentResponse;
import vn.hoangtojava.student_management.dto.response.EnrollSubjectResponse;
import vn.hoangtojava.student_management.dto.request.StudentRequestDTO;
import vn.hoangtojava.student_management.dto.request.SubjectDTO;
import vn.hoangtojava.student_management.dto.response.StudentDetailResponse;
import vn.hoangtojava.student_management.model.Student;
import vn.hoangtojava.student_management.util.Status;

import java.io.UnsupportedEncodingException;

public interface StudentService {

    long createStudent(CreateStudentDTO requestDTO);
    long createSubject(SubjectDTO subjectDTO);

    EnrollSubjectResponse enrollSubject(StudentRequestDTO studentRequestDTO);

    long saveStudent(StudentRequestDTO requestDTO) throws UnsupportedEncodingException;

    UpdateStudentResponse updateStudent(long studentId, UpdateStudentResponse requestDTO);

    void changeStatus(long studentId, Status studentStatus);

    void deleteStudent(long studentId);

    StudentDetailResponse getStudent(long studentId);
    StudentDetailResponse getStudentAnSubject(long studentId);

    PageResponse<?> getAllStudentWithSortBy(int pageNo, int pageSize, String sortBy );

    PageResponse<?> getAllStudentWithSortByMultipleColumn(int pageNo, int pageSize, String ...sorts);

    PageResponse<?> getAllStudentWithSortByMultipleAndSearchColumn(int pageNo, int pageSize, String search, String sortBy);

    PageResponse<?> advancedSearchWithCriteria(int pageNo, int pageSize, String sortBy, String... search);


    Student getStudentById(long studentId);


    PageResponse<?> advancedSearchWithSpecification(Pageable pageable, String[] student, String[] subject);
}
