package vn.hoangtojava.repository.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.jpa.domain.Specification;
import vn.hoangtojava.model.User;

@Getter
@AllArgsConstructor
public class UserSpecification implements Specification<User> {
    private SpecSearchCriteria criteria;



    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return switch (criteria.getOperation()){
            // equality nhan tham so la 1 object
            case EQUALITY -> criteriaBuilder.equal(root.get(criteria.getKey()),criteria.getValue());
            case NEGATION -> criteriaBuilder.notEqual(root.get(criteria.getKey()), criteria.getValue());
            // greater_than chi nhan tham so Comparable hoac String -> moi ep kieu
            case GREATER_THAN -> criteriaBuilder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString());
            case LESS_THAN -> criteriaBuilder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString());
            case STARTS_WITH -> criteriaBuilder.like(root.get(criteria.getKey()), criteria.getValue() + "%");
            case ENDS_WITH -> criteriaBuilder.like(root.get(criteria.getKey()), "%" + criteria.getValue());
            case LIKE -> criteriaBuilder.like(root.get(criteria.getKey()), "%" + criteria.getValue().toString() + "%");
            case CONTAINS -> criteriaBuilder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
            default -> null;
        };
    }
}

