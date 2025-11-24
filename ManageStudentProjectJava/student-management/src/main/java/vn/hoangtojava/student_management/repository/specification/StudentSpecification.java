package vn.hoangtojava.student_management.repository.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import vn.hoangtojava.student_management.model.Student;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StudentSpecification implements Specification<Student> {

    private SpecSearchCriteria spec;

    @Override
    public Predicate toPredicate(Root<Student> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        String key = spec.getKey();
        Object value = spec.getValue();

        return switch (spec.getSearchOperation()) {

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
}
