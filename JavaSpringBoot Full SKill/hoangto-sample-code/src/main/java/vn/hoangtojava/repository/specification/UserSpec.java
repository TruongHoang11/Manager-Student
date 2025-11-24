package vn.hoangtojava.repository.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import vn.hoangtojava.model.User;
import vn.hoangtojava.util.Gender;

import javax.swing.text.html.HTMLDocument;

public class UserSpec {

    // phuong thuc hasFirstName : VIET BINH THUONG
//    public static Specification<User> hasFirstName(String firstName){
//        return new Specification<User>() {
//            @Override
//            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//                return criteriaBuilder.like(root.get("firstName"), "%" + "t" + "%");
//            }
//        };
//    }

    // phuong thuc hasFirstName: VIET BANG LAMBDA
    public static Specification<User> hasFirstName(String firstName){
        return ((root, query, criteriaBuilder)
                -> criteriaBuilder.like(root.get("firstName"),"%" + firstName + "%"));
    }

    // phuong thuc notEqualGender: VIET BINH THUONG
//    public static Specification<User> notEqualGender(Gender gender){
//        return new Specification<User>() {
//            @Override
//            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//                return criteriaBuilder.equal(root.get("gender"), Gender.MALE);
//            }
//        };
//    }

    // phuong thuc notEqualGender: VIET BANG LAMBDA
    public static Specification<User> notEqualGender(Gender gender){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.notEqual(root.get("gender"),gender));
    }

}
