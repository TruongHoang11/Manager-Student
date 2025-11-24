package vn.hoangtojava.student_management.service.impl;

import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import vn.hoangtojava.student_management.dto.exception.ResourceNotFoundException;
import vn.hoangtojava.student_management.dto.request.CreateStudentDTO;
import vn.hoangtojava.student_management.dto.request.StudentRequestDTO;
import vn.hoangtojava.student_management.dto.request.SubjectDTO;
import vn.hoangtojava.student_management.dto.response.PageResponse;
import vn.hoangtojava.student_management.dto.response.UpdateStudentResponse;
import vn.hoangtojava.student_management.dto.response.EnrollSubjectResponse;
import vn.hoangtojava.student_management.dto.response.StudentDetailResponse;
import vn.hoangtojava.student_management.model.Student;
import vn.hoangtojava.student_management.model.StudentSubject;
import vn.hoangtojava.student_management.model.Subject;
import vn.hoangtojava.student_management.repository.SearchRepository;
import vn.hoangtojava.student_management.repository.StudentRepository;
import vn.hoangtojava.student_management.repository.StudentSubjectRepository;
import vn.hoangtojava.student_management.repository.SubjectRepository;
import vn.hoangtojava.student_management.repository.specification.StudentSpecificationBuilder;
import vn.hoangtojava.student_management.service.StudentService;
import vn.hoangtojava.student_management.util.Status;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Builder
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final StudentSubjectRepository studentSubjectRepository;

    private final SearchRepository searchRepository;


    @Override
    public long createStudent(CreateStudentDTO requestDTO) {
        Student student = Student.builder()
                .firstName(requestDTO.getFirstName())
                .lastName(requestDTO.getLastName())
                .email(requestDTO.getEmail())
                .phone(requestDTO.getPhone())
                .age(requestDTO.getAge())
                .status(Status.valueOf(requestDTO.getStatus().toUpperCase()))
                .build();
        studentRepository.save(student);
        return student.getId();
    }

    @Override
    public long createSubject(SubjectDTO subjectDTO) {
        if (subjectRepository.findBySubjectCode(subjectDTO.getSubjectCode()).isPresent()) {
            throw new IllegalArgumentException(String.format("This subject was already created!, subjectCode = %s", subjectDTO.getSubjectCode()));
        }
        Subject subject = Subject.builder()
                .subjectCode(subjectDTO.getSubjectCode())
                .subjectName(subjectDTO.getSubjectName())
                .credit(subjectDTO.getCredit())
                .build();
        subjectRepository.save(subject);
        return subject.getId();
    }

    @Override
    @Transactional
    public EnrollSubjectResponse enrollSubject(StudentRequestDTO studentRequestDTO) {
        Student student = studentRepository.findByStudentCode(studentRequestDTO.getStudentCode())
                .orElseGet(() -> {
                    Student newStudent = Student.builder()
                            .studentCode(studentRequestDTO.getStudentCode())
                            .firstName(studentRequestDTO.getFirstName())
                            .lastName(studentRequestDTO.getLastName())
                            .email(studentRequestDTO.getEmail())
                            .phone(studentRequestDTO.getPhone())
                            .age(studentRequestDTO.getAge())
                            .status(Status.valueOf(studentRequestDTO.getStatus().toUpperCase()))
                            .build();
                    return studentRepository.save(newStudent);
                });

        List<String> addedSubjects = new ArrayList<>();
        List<String> existsSubjects = new ArrayList<>();

        studentRequestDTO.getSubjects().forEach(subjectDTO -> {
            Subject subject = subjectRepository.findBySubjectCode(subjectDTO.getSubjectCode())
                    .orElseGet(() -> {
                        Subject newSubject = Subject.builder()
                                .subjectCode(subjectDTO.getSubjectCode())
                                .subjectName(subjectDTO.getSubjectName())
                                .credit(subjectDTO.getCredit())
                                .build();
                        return subjectRepository.save(newSubject);
                    });


            boolean exists = studentSubjectRepository.existsByStudentAndSubject(student, subject);
            if (!exists) {
                StudentSubject newStudentSubject = new StudentSubject(student, subject);
                studentSubjectRepository.save(newStudentSubject);
                addedSubjects.add(subjectDTO.getSubjectName());
            } else {
                existsSubjects.add(subject.getSubjectName());
            }

        });


        return EnrollSubjectResponse.builder()
                .studentCode(studentRequestDTO.getStudentCode())
                .firstName(studentRequestDTO.getFirstName())
                .lastName(studentRequestDTO.getLastName())
                .addedSubjects(addedSubjects)
                .existSubjects(existsSubjects)
                .build();
    }


    // tao moi cung luc Student va cac mon hoc luon
    @Override
    @Transactional
    public long saveStudent(StudentRequestDTO requestDTO) {
        // 1. Tạo Student entity
        Student student = Student.builder()
                .firstName(requestDTO.getFirstName())
                .lastName(requestDTO.getLastName())
                .email(requestDTO.getEmail())
                .phone(requestDTO.getPhone())
                .age(requestDTO.getAge())
                .status(Status.valueOf(requestDTO.getStatus().toUpperCase()))
                .build();

        // 2. Save Student để có ID
        student = studentRepository.saveAndFlush(student);
        log.info("Đã lưu Student với ID: {}", student.getId());

        // 3. Xử lý Subjects
        final Student savedStudent = student; // Lambda yêu cầu final

        requestDTO.getSubjects().forEach(subjectDTO -> {
            // Tìm Subject có sẵn hoặc tạo mới
            Subject subject = subjectRepository.findBySubjectCode(subjectDTO.getSubjectCode())
                    .orElseGet(() -> {
                        log.info("Tạo Subject mới: {}", subjectDTO.getSubjectCode());
                        Subject newSubject = Subject.builder()
                                .subjectCode(subjectDTO.getSubjectCode())
                                .subjectName(subjectDTO.getSubjectName())
                                .credit(subjectDTO.getCredit())
                                .build();

                        // ✅ QUAN TRỌNG: saveAndFlush để có ID ngay
                        return subjectRepository.saveAndFlush(newSubject);
                    });

            // Đăng ký môn học (giờ cả Student và Subject đều có ID)
            savedStudent.saveSubjects(subject);
        });

        // 4. Save lại để persist relationships
//        studentRepository.save(savedStudent);
        studentRepository.flush();

        log.info("Đã đăng ký {} môn học cho Student ID: {}",
                requestDTO.getSubjects().size(), savedStudent.getId());

        return savedStudent.getId();

    }


    @Override
    public UpdateStudentResponse updateStudent(long studentId, UpdateStudentResponse requestDTO) {
        Student student = getStudentById(studentId);
        student.setStudentCode(requestDTO.getStudentCode());
        student.setFirstName(requestDTO.getFirstName());
        student.setLastName(requestDTO.getLastName());
        student.setEmail(requestDTO.getEmail());
        student.setPhone(requestDTO.getPhone());
        student.setStatus(Status.valueOf(requestDTO.getStatus()));
        student.setAge(requestDTO.getAge());

        studentRepository.save(student);
        return UpdateStudentResponse.builder()
                .studentCode(student.getStudentCode())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .phone(student.getPhone())
                .age(student.getAge())
                .status(student.getStatus().name())
                .build();

    }

    @Override
    public void changeStatus(long studentId, Status studentStatus) {
        Student student = getStudentById(studentId);
        student.setStatus(studentStatus);
        studentRepository.save(student);
        log.info("Status was changed successfully");
    }

    @Override
    public void deleteStudent(long studentId) {
        Student student = getStudentById(studentId);
        studentRepository.deleteById(student.getId());
    }

    @Override
    public StudentDetailResponse getStudent(long studentId) {
        Student student = getStudentById(studentId);
        StudentDetailResponse response = StudentDetailResponse.builder()
                .studentCode(student.getStudentCode())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .phone(student.getPhone())
                .email(student.getEmail())
                .age(student.getAge())
                .status(student.getStatus().name())
                .build();
        return response;
    }

    @Override
    public StudentDetailResponse getStudentAnSubject(long studentId) {
        Student student = getStudentById(studentId);
        studentRepository.findStudentWithSubjects(student.getId());

        Set<String> subjects = student.getStudentSubjects().stream()
                .map(ss -> ss.getSubject().getSubjectName())
                .collect(Collectors.toSet());
        return StudentDetailResponse.builder()
                .studentCode(student.getStudentCode())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .phone(student.getPhone())
                .email(student.getEmail())
                .age(student.getAge())
                .status(student.getStatus().name())
                .subjects(subjects)
                .build();
    }

    @Override
    public PageResponse<?> getAllStudentWithSortBy(int pageNo, int pageSize, String sortBy) {
        int pageno = 0;
        if (pageNo > 1) {
            pageno = pageNo - 1;
        }
        List<Sort.Order> sorts = new ArrayList<>();
        if (StringUtils.hasLength(sortBy)) {
            Pattern pattern = Pattern.compile("^(\\w.+)(:)(.*)");
            Matcher matcher = pattern.matcher(sortBy);
            if (matcher.find()) {
                if (matcher.group(3).equalsIgnoreCase("asc")) {
                    sorts.add(new Sort.Order(Sort.Direction.ASC, matcher.group(1)));
                } else {
                    sorts.add(new Sort.Order(Sort.Direction.DESC, matcher.group(1)));
                }
            }
        }
        Set<String> subjects = new HashSet<>();

        Pageable pageable = PageRequest.of(pageno, pageSize, Sort.by(sorts));

        Page<Student> students = studentRepository.findAll(pageable);

//        List<StudentDetailResponse> responses = students.stream()
//                .map(student -> StudentDetailResponse.builder()
//                        .id(student.getId())
//                        .studentCode(student.getStudentCode())
//                        .firstName(student.getFirstName())
//                        .lastName(student.getLastName())
//                        .phone(student.getPhone())
//                        .email(student.getEmail())
//                        .age(student.getAge())
//                        .status(student.getStatus().name())
//                        .build()).toList();

        return convertToPageResponse(students, pageable);
    }

    @Override
    public PageResponse<?> getAllStudentWithSortByMultipleColumn(int pageNo, int pageSize, String... sorts) {
        int pageno = 0;
        if (pageNo > 1) {
            pageno = pageNo - 1;
        }

        ArrayList<Sort.Order> orders = new ArrayList<>();
        if (sorts != null) {
            Pattern pattern = Pattern.compile("(\\w.+)(:)(.*)");
            for (String sortBy : sorts) {
                Matcher matcher = pattern.matcher(sortBy);
                if (matcher.find()) {
                    if (matcher.group(3).equalsIgnoreCase("asc")) {
                        orders.add(new Sort.Order(Sort.Direction.ASC, matcher.group(1)));
                    } else {
                        orders.add(new Sort.Order(Sort.Direction.DESC, matcher.group(1)));
                    }
                }
            }
        }
        Pageable pageable = PageRequest.of(pageno, pageSize, Sort.by(orders));
        Page<Student> students = studentRepository.findAll(pageable);

//        List<StudentDetailResponse> responses = students.stream()
//                .map(student -> StudentDetailResponse.builder()
//                        .id(student.getId())
//                        .studentCode(student.getStudentCode())
//                        .firstName(student.getFirstName())
//                        .lastName(student.getLastName())
//                        .phone(student.getPhone())
//                        .email(student.getEmail())
//                        .age(student.getAge())
//                        .status(student.getStatus().name())
//                        .build()).toList();


        return convertToPageResponse(students, pageable);
    }

    @Override
    public PageResponse<?> getAllStudentWithSortByMultipleAndSearchColumn(int pageNo, int pageSize, String search, String sortBy) {
        return searchRepository.getAllStudentWithSortByMultipleAndSearchColumn(pageNo, pageSize, search, sortBy);
    }

    @Override
    public PageResponse<?> advancedSearchWithCriteria(int pageNo, int pageSize, String sortBy, String... search) {
        return searchRepository.advancedSearchWithCriteria(pageNo, pageSize, sortBy, search);
    }

    @Override
    public Student getStudentById(long studentId) {
        return studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFoundException("Student not found"));
    }

    @Override
    public PageResponse<?> advancedSearchWithSpecification(Pageable pageable, String[] student, String[] subject) {

        List<Student> list1 = new ArrayList<>();
        if (student != null && subject != null) {
            return searchRepository.getStudentJoinSubject(pageable, student, subject);
        } else if (student != null) {
            StudentSpecificationBuilder studentSpecificationBuilder = new StudentSpecificationBuilder();
            Pattern pattern = Pattern.compile("([\\w\\\\p{L}]+?)([:><!~])(\\*?)(.*?)(\\*?)$");

            for (String s : student) {
                Matcher matcher = pattern.matcher(s);
                if (matcher.find()) {
                    studentSpecificationBuilder.with(matcher.group(1), matcher.group(2), matcher.group(4), matcher.group(3), matcher.group(5));
                }
            }
            Page<Student> list = studentRepository.findAll(studentSpecificationBuilder.build(), pageable);
//            List<StudentDetailResponse> responses = list.stream()
//                    .map(s -> StudentDetailResponse.builder()
//                            .id(s.getId())
//                            .phone(s.getPhone())
//                            .email(s.getEmail())
//                            .firstName(s.getFirstName())
//                            .lastName(s.getLastName())
//                            .studentCode(s.getStudentCode())
//                            .status(s.getStatus().name())
//                            .age(s.getAge())
//                            .build()).toList();
            return convertToPageResponse(list,pageable);
        }

        Page<Student> students = studentRepository.findAll(pageable);
        List<StudentDetailResponse> responses = students.stream()
                .map(s -> StudentDetailResponse.builder()
                        .id(s.getId())
                        .phone(s.getPhone())
                        .email(s.getEmail())
                        .firstName(s.getFirstName())
                        .lastName(s.getLastName())
                        .studentCode(s.getStudentCode())
                        .status(s.getStatus().name())
                        .age(s.getAge())
                        .build()).toList();
        return convertToPageResponse(students,pageable);
    }

    public PageResponse<?> convertToPageResponse(Page<Student> list, Pageable pageable) {

        List<StudentDetailResponse> responses = list.stream()
                .map(student ->
                     StudentDetailResponse.builder()
                            .studentCode(student.getStudentCode())
                            .id(student.getId())
                            .phone(student.getPhone())
                            .email(student.getEmail())
                            .firstName(student.getFirstName())
                            .lastName(student.getLastName())
                            .studentCode(student.getStudentCode())
                            .status(student.getStatus().name())
                            .age(student.getAge())
                            .build()).toList();

        return PageResponse.builder()
                .pageNo(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalPage(list.getTotalPages())
                .item(responses)
                .build();
    }


}
