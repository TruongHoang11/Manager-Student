package vn.hoangtojava.student_management.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.hoangtojava.student_management.dto.request.CreateStudentDTO;
import vn.hoangtojava.student_management.dto.request.StudentRequestDTO;
import vn.hoangtojava.student_management.dto.request.SubjectDTO;
import vn.hoangtojava.student_management.dto.response.StudentDetailResponse;
import vn.hoangtojava.student_management.dto.response.UpdateStudentResponse;
import vn.hoangtojava.student_management.dto.response.ResponseData;
import vn.hoangtojava.student_management.dto.response.ResponseError;
import vn.hoangtojava.student_management.service.StudentService;
import vn.hoangtojava.student_management.util.Status;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Validated
@RestController
@RequestMapping("/student")
@Slf4j
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping(value = "/")
    public ResponseData<?> saveStudent(@Valid @RequestBody  StudentRequestDTO studentRequestDTO){
    log.info("Request add student = {} {}", studentRequestDTO.getFirstName(), studentRequestDTO.getLastName());

        try{
            long studentId = studentService.saveStudent(studentRequestDTO);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Added Student Successfully", studentId);
        } catch(Exception e){
            log.info("Error Message = {}", e.getMessage(),e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Added Student Failed");
        }
    }

    @PostMapping("/create_student")
    public ResponseData<?> createStudent(@Valid @RequestBody CreateStudentDTO createStudentDTO){
        log.info("Request create student = {} {}", createStudentDTO.getFirstName(), createStudentDTO.getLastName());

        try{
            long studentId = studentService.createStudent(createStudentDTO);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Created Student Successfully", studentId);
        } catch(Exception e){
            log.info("Error Message = {}", e.getMessage(),e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Create Student Failed");
        }
    }

    @PostMapping("/create_subject")
    public ResponseData<?> createSubject(@Valid @RequestBody SubjectDTO subjectDTO){
        log.info("Request create subject = {}", subjectDTO.getSubjectName());

        try{
            long subjectId = studentService.createSubject(subjectDTO);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Created Subject Successfully", subjectId);
        } catch(Exception e){
            log.info("Error Message = {}", e.getMessage(),e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Create Subject Failed");
        }
    }


    @PostMapping("/enroll_subject")
    public ResponseData<?> enrollSubject(@Valid @RequestBody StudentRequestDTO studentRequestDTO){
        log.info("Request enroll subject ");

        try{
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Enrolled Subject Successfully", studentService.enrollSubject(studentRequestDTO) );
        } catch(Exception e){
            log.info("Error Message = {}", e.getMessage(),e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Enroll Subject Failed");
        }
    }


    @PutMapping( "/{studentId}")
    public ResponseData<?> updateStudent(@PathVariable  @Min(1) Long studentId, @Valid @RequestBody UpdateStudentResponse studentRequestDTO){
        log.info("Update student information!");
        try{
            UpdateStudentResponse response = studentService.updateStudent(studentId, studentRequestDTO);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Updated student successfully!", response);
        } catch (Exception e){
            log.info("Message error = {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Update student failled!");
        }

    }

    @PatchMapping ("/{studentId}")
    public ResponseData<?> changeStatus(@PathVariable @Min(1) Long studentId, @RequestParam Status status){

        log.info("Request change student status, studentId = {}", studentId);
        try{
            studentService.changeStatus(studentId,status);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Change student status successfully");
        } catch(Exception e){
            log.info("Error message = {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Change student status failed");
        }
    }

    @DeleteMapping ("/{studentId}")
    public ResponseData<?> deleteStudent(@PathVariable @Min(1) Long studentId){

        log.info("Request delete student, studentId = {}", studentId);
        try{
            studentService.deleteStudent(studentId);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Delete student successfully");
        } catch(Exception e){
            log.info("Error message = {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Delete student failed");
        }
    }

//    @GetMapping("/{studentId}")
//    public ResponseData<?> getStudent(@PathVariable Long studentId){
//        log.info("Request get student, studentId = {}", studentId);
//        try{
//            StudentDetailResponse student = studentService.getStudent(studentId);
//            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Get student successfully", student);
//        } catch(Exception e){
//            log.info("Error message = {}", e.getMessage(), e.getCause());
//            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get student failed");
//        }
//    }

    @GetMapping("/{studentId}")
    public ResponseData<?> getStudentAnSubject(@PathVariable Long studentId){
        log.info("Request get student and subject, studentId = {}", studentId);
        try{
            StudentDetailResponse student = studentService.getStudentAnSubject(studentId);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Get student and subject successfully", student);
        } catch(Exception e){
            log.info("Error message = {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get student and subject failed");
        }
    }

    @GetMapping("/list")
    public ResponseData<?> getAllStudentWithSortBy(
           @RequestParam(defaultValue = "0", required = false) int pageNo,
           @RequestParam(defaultValue = "10", required = false) int pageSize,
           @RequestParam(required = false) String sortBy
    ){
        log.info("Request all student with sortBy, sortBy = {}", sortBy);
        try{
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Get all student with softBy successfully", studentService.getAllStudentWithSortBy(pageNo,pageSize,sortBy));
        } catch(Exception e){
            log.info("Error message = {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get all student with softBy failed");
        }
    }

    @GetMapping("/list_multiple_column")
    public ResponseData<?> getAllStudentWithSortByMultipleColumn(
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @RequestParam(defaultValue = "10", required = false) int pageSize,
            @RequestParam(required = false) String... sorts
    ){
        log.info("Request all student with sortBy Multiple Column, sortBy = {}", sorts);
        try{
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Get all student with softBy multiple column successfully", studentService.getAllStudentWithSortByMultipleColumn(pageNo,pageSize,sorts));
        } catch(Exception e){
            log.info("Error message = {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get all student with softBy multiple column failed");
        }
    }


    @GetMapping("/list-sortBy-search-columns")
    public ResponseData<?> getAllStudentWithSortByMultipleAndSearchColumns(
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @RequestParam(defaultValue = "10", required = false) int pageSize,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sortBy
    ){
        log.info("Request all student with sortBy And Search Multiple Column");
        try{
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Get all student with softBy and search multiple column successfully", studentService.getAllStudentWithSortByMultipleAndSearchColumn(pageNo,pageSize,search,sortBy));
        } catch(Exception e){
            log.info("Error message = {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get all student with softBy and search multiple column failed");
        }
    }


    // tim kiem tat ca cac field cua Sudent
    @GetMapping("/list-sort-search-criteria")
    public ResponseData<?> advancedSearchWithCriteria(
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @RequestParam(defaultValue = "10", required = false) int pageSize,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String... search
    ){
        log.info("Request all student with sortBy And Search Multiple Column");
        try{
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Get all student with softBy and search multiple column successfully", studentService.advancedSearchWithCriteria(pageNo,pageSize,sortBy,search));
        } catch(Exception e){
            log.info("Error message = {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get all student with softBy and search multiple column failed");
        }
    }


    @GetMapping("/list-advanced-search-specification")
    public ResponseData<?> advancedSearchWithSpecification(
            Pageable pageable,
            @RequestParam(required = false) String[] student,
            @RequestParam(required = false) String [] subject
    ){
        log.info("Request With Advanced Search Specification");
        try{
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Get all student with specification succeslly", studentService.advancedSearchWithSpecification(pageable, student, subject));
        } catch(Exception e){
            log.info("Error message = {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get all student with with specification failed");
        }
    }




}
