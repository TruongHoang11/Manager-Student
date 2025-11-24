package vn.hoangtojava.student_management.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import vn.hoangtojava.student_management.dto.response.PageResponse;
import vn.hoangtojava.student_management.dto.response.StudentDetailResponse;
import vn.hoangtojava.student_management.model.Student;
import vn.hoangtojava.student_management.model.StudentSubject;
import vn.hoangtojava.student_management.model.Subject;
import vn.hoangtojava.student_management.repository.criteria.SearchCriteria;
import vn.hoangtojava.student_management.repository.criteria.StudentSearchCriteriaQueryConsumer;
import vn.hoangtojava.student_management.repository.specification.SpecSearchCriteria;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Repository
public class SearchRepository {

    private final StudentRepository studentRepository;

    @PersistenceContext
    private EntityManager entityManager;


    public  PageResponse<?> getAllStudentWithSortByMultipleAndSearchColumn(int pageNo, int pageSize, String search, String sortBy) {
        int pageno = 0;
        if(pageNo > 1){
            pageno = pageNo - 1;
        }

        StringBuilder sqlQuery = new StringBuilder("SELECT new vn.hoangtojava.student_management.dto.response.StudentDetailResponse" +
                "(s.id, s.studentCode, s.lastName, s.email) FROM Student s WHERE 1=1");

        if(StringUtils.hasLength(search)){
            sqlQuery.append(" AND (LOWER(s.studentCode) LIKE LOWER(:studentCode)");
            sqlQuery.append(" OR LOWER(s.lastName) LIKE LOWER(:lastName) ");
            sqlQuery.append(" OR LOWER(s.email) LIKE LOWER(:email))");
        }

        if(StringUtils.hasLength(sortBy)){
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sortBy);
            if(matcher.find()){
                    sqlQuery.append(String.format(" ORDER BY s.%s %s", matcher.group(1), matcher.group(3)));

            }
        }

        Query selectQuery = entityManager.createQuery(sqlQuery.toString());
        selectQuery.setFirstResult(pageno);
        selectQuery.setMaxResults(pageSize);
        if(StringUtils.hasLength(search)){
            selectQuery.setParameter("studentCode", String.format("%%%s%%", search));
            selectQuery.setParameter("lastName", String.format("%%%s%%", search));
            selectQuery.setParameter("email", String.format("%%%s%%", search));
        }


        List<?> students = selectQuery.getResultList();


        StringBuilder sqlQueryCount = new StringBuilder("SELECT COUNT(*) FROM Student s WHERE 1=1");

        if(StringUtils.hasLength(search)){
            sqlQueryCount.append(" AND (LOWER(s.studentCode) LIKE LOWER(:studentCode)");
            sqlQueryCount.append(" OR LOWER(s.lastName) LIKE LOWER(:lastName) ");
            sqlQueryCount.append(" OR LOWER(s.email) LIKE LOWER(:email))");
        }
        Query selectQueryCount = entityManager.createQuery(sqlQueryCount.toString());
        if(StringUtils.hasLength(search)){
            selectQueryCount.setParameter("studentCode", String.format("%%%s%%", search));
            selectQueryCount.setParameter("lastName", String.format("%%%s%%", search));
            selectQueryCount.setParameter("email", String.format("%%%s%%", search));
            selectQueryCount.getSingleResult();
        }
        Long totalElement = (Long)selectQueryCount.getSingleResult();

        Pageable pageable = PageRequest.of(pageno,pageSize);

        Page<?> page = new PageImpl<>(students, pageable, totalElement);

        return PageResponse.builder()
                .pageNo(pageno)
                .pageSize(pageSize)
                .totalPage(page.getTotalPages())
                .item(students)
                .build();


    }


    public PageResponse<?> advancedSearchWithCriteria(int pageNo, int pageSize, String sortBy, String[] search) {
        int pageno= 0;
        if(pageNo > 1){
            pageno = pageNo - 1;
        }
        // seach o day se la 1 list cac field dieu kien tim kie
        // vi du: firstName = hoang, lastName = anh, age > 19 ...etc.

        List<SearchCriteria> criteriaList = new ArrayList<>();
        if(search != null){

            // firstName:hoang, lastName:anh, age>18
            Pattern pattern = Pattern.compile("(\\w+?)(>|<|:)(.*)");
            for(String s : search){
            Matcher matcher = pattern.matcher(s);
            if (matcher.find()){
                criteriaList.add(new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3)));
            }

            }
        }

        List<Student> students = getStudents( pageno, pageSize, sortBy,criteriaList);
        Long totalElement = getTotalElement( criteriaList);

        Pageable pageable = PageRequest.of(pageno, pageSize);
        Page page = new PageImpl(students,pageable,totalElement);

        return PageResponse.builder()
                .pageNo(pageno)
                .pageSize(pageSize)
                .totalPage(page.getTotalPages())
                .item(students)
                .build();
    }

    private Long getTotalElement(List<SearchCriteria> criteriaList) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<Student> root = query.from(Student.class);
        Predicate predicate = criteriaBuilder.conjunction();

        StudentSearchCriteriaQueryConsumer queryConsumer = new StudentSearchCriteriaQueryConsumer(criteriaBuilder, predicate, root);
        criteriaList.forEach(queryConsumer);
        predicate = queryConsumer.getPredicate();

        query.select(criteriaBuilder.count(root));
        query.where(predicate);

        return entityManager.createQuery(query)
                .getSingleResult();



    }

    private List<Student> getStudents(int pageno, int pageSize, String sortBy, List<SearchCriteria> criteriaList) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Student> query = criteriaBuilder.createQuery(Student.class);
        Root<Student> root = query.from(Student.class);

        Predicate finalPredicate = criteriaBuilder.conjunction();

        StudentSearchCriteriaQueryConsumer queryConsumer = new StudentSearchCriteriaQueryConsumer(criteriaBuilder,finalPredicate,root);
        criteriaList.forEach(queryConsumer);

        finalPredicate = queryConsumer.getPredicate();

        query.where(finalPredicate);



        if(StringUtils.hasLength(sortBy)){
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sortBy);
            if(matcher.find()){
                if (matcher.group(3).equalsIgnoreCase("asc")){
                    query.orderBy(criteriaBuilder.asc(root.get(matcher.group(1))));
                } else{
                    query.orderBy(criteriaBuilder.desc(root.get(matcher.group(1))));
                }
            }
        }

        return entityManager.createQuery(query)
                .setFirstResult(pageno)
                .setMaxResults(pageSize)
                .getResultList();
    }

    public PageResponse<?> getStudentJoinSubject(Pageable pageable, String[] student, String[] subject) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Student> criteriaQuery = criteriaBuilder.createQuery(Student.class);
        Root<Student> root = criteriaQuery.from(Student.class);
        Join<Student, StudentSubject> joinSS = root.join("studentSubjects");
        Join<StudentSubject,Subject> joinSubject = joinSS.join("subject");

        List<Predicate> studentPre = new ArrayList<>();
        Pattern pattern = Pattern.compile("([\\w\\p{L}]+?)([:><!~])(\\*?)(.*?)(\\*?)$");

        for(String a : student){
        Matcher matcher = pattern.matcher(a);
            if (matcher.find()){
                SpecSearchCriteria searchCriteria = new SpecSearchCriteria(matcher.group(1), matcher.group(2), matcher.group(4), matcher.group(3), matcher.group(5));
                Predicate predicate = toStudentPredicate(root, criteriaBuilder, searchCriteria);
                studentPre.add(predicate);
            }
        }

        List<Predicate> subjectPre = new ArrayList<>();

        for(String a : subject){
            Matcher matcher = pattern.matcher(a);
            if(matcher.find()){
                SpecSearchCriteria criteria = new SpecSearchCriteria(matcher.group(1),matcher.group(2),matcher.group(4),matcher.group(3),matcher.group(5));
                Predicate predicate1 = toSubjectPredicate(joinSubject,criteriaBuilder,criteria);
                subjectPre.add(predicate1);
            }
        }

    Predicate stuPredicate =criteriaBuilder.or(studentPre.toArray(new Predicate[0]));
    Predicate subPredicate =criteriaBuilder.or(subjectPre.toArray(new Predicate[0]));
    Predicate finalPredicate = criteriaBuilder.and(stuPredicate, subPredicate);

    criteriaQuery.where(finalPredicate);

        List<Student> students = entityManager.createQuery(criteriaQuery)
                .setFirstResult(pageable.getPageNumber())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
        long totalElement = getTotalElementJoinedAddresses(student, subject);


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

        return PageResponse.builder()
                .pageNo(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalPage((int)totalElement)
                .item(responses)
                .build();
    }

    private long getTotalElementJoinedAddresses(String[] student, String[] subject) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<Student> root = criteriaQuery.from(Student.class);
        Join<Student, StudentSubject> joinSS = root.join("studentSubjects");
        Join<StudentSubject, Subject> joinSubject = joinSS.join("subject");

        List<Predicate> studentPre = new ArrayList<>();
        Pattern pattern = Pattern.compile("([\\w\\p{L}]+?)([:><!~])(\\*?)(.*?)(\\*?)$");

        for(String a : student){
            Matcher matcher = pattern.matcher(a);
            if (matcher.find()){
                SpecSearchCriteria searchCriteria = new SpecSearchCriteria(matcher.group(1), matcher.group(2), matcher.group(4), matcher.group(3), matcher.group(5));
                Predicate predicate = toStudentPredicate(root, criteriaBuilder, searchCriteria);
                studentPre.add(predicate);
            }
        }

        List<Predicate> subjectPre = new ArrayList<>();

        for(String a : subject){
            Matcher matcher = pattern.matcher(a);
            if(matcher.find()){
                SpecSearchCriteria criteria = new SpecSearchCriteria(matcher.group(1),matcher.group(2),matcher.group(4),matcher.group(3),matcher.group(5));
                Predicate predicate1 = toSubjectPredicate(joinSubject,criteriaBuilder,criteria);
                subjectPre.add(predicate1);
            }
        }

        Predicate stuPredicate =criteriaBuilder.or(studentPre.toArray(new Predicate[0]));
        Predicate subPredicate =criteriaBuilder.or(subjectPre.toArray(new Predicate[0]));
        Predicate finalPredicate = criteriaBuilder.and(stuPredicate, subPredicate);


        criteriaQuery.select(criteriaBuilder.count(root));
        criteriaQuery.where(finalPredicate);
        return entityManager.createQuery(criteriaQuery).getSingleResult();

    }

    private Predicate toStudentPredicate(Root<Student> root, CriteriaBuilder cb, SpecSearchCriteria criteria){
        String key = criteria.getKey();
        Object value = criteria.getValue();
        return switch (criteria.getSearchOperation()){
            case EQUALITY ->
                    cb.equal(root.get(key), value);

            case NEGATION ->
                    cb.notEqual(root.get(key), value);

            case GREATER_THAN ->
                    cb.greaterThan(root.get(key), value.toString());

            case LESS_THAN ->
                    cb.lessThan(root.get(key), value.toString());

            case LIKE, CONTAINS ->
                    cb.like(root.get(key).as(String.class), "%" + value + "%");

            case STARTS_WITH ->
                    cb.like(root.get(key).as(String.class), value + "%");

            case ENDS_WITH ->
                    cb.like(root.get(key).as(String.class), "%" + value);

            default -> null;

        };
    }

    private Predicate toSubjectPredicate(Join<StudentSubject, Subject> root, CriteriaBuilder cb, SpecSearchCriteria criteria) {
        String key = criteria.getKey();
        Object value = criteria.getValue();
        return switch (criteria.getSearchOperation()) {
            case EQUALITY -> cb.equal(root.get(key), value);

            case NEGATION -> cb.notEqual(root.get(key), value);

            case GREATER_THAN -> cb.greaterThan(root.get(key), value.toString());

            case LESS_THAN -> cb.lessThan(root.get(key), value.toString());

            case LIKE, CONTAINS -> cb.like(root.get(key).as(String.class), "%" + value + "%");

            case STARTS_WITH -> cb.like(root.get(key).as(String.class), value + "%");

            case ENDS_WITH -> cb.like(root.get(key).as(String.class), "%" + value);

            default -> null;

        };
    }
}
