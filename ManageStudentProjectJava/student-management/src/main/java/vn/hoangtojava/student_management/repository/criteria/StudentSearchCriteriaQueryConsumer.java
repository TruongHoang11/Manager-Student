package vn.hoangtojava.student_management.repository.criteria;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.hoangtojava.student_management.model.Student;

import java.util.function.Consumer;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentSearchCriteriaQueryConsumer implements Consumer<SearchCriteria> {
    private CriteriaBuilder builder;

    private Predicate predicate;

    private Root<Student> root;

    @Override
    public void accept(SearchCriteria params) {
        Class<?> fieldType = root.get(params.getKey()).getJavaType();
        if (params.getOperations().equals(">")) {
            if (root.get(params.getKey()).getJavaType() == Integer.class) {
                predicate = builder.and(predicate,
                        builder.greaterThanOrEqualTo(root.get(params.getKey()), Integer.valueOf(params.getValue().toString())));
            }
        } else if (params.getOperations().equals("<")) {
            if (root.get(params.getKey()).getJavaType() == Integer.class) {
                predicate = builder.and(predicate,
                        builder.lessThanOrEqualTo(root.get(params.getKey()), Integer.valueOf(params.getValue().toString())));
            }
        } else if (params.getOperations().equals(":")) {
            if (fieldType == String.class)
                predicate = builder.and(predicate, builder.like(builder.lower(root.get(params.getKey())), "%" + params.getValue().toString().toLowerCase() + "%"));
            else if (fieldType == Integer.class) {
                predicate = builder.and(predicate,
                        builder.equal(root.get(params.getKey()), Integer.valueOf(params.getValue().toString())));
            } else if (fieldType.isEnum()) {
                Object enumValue = Enum.valueOf((Class<Enum>) fieldType, params.getValue().toString().toUpperCase());

                predicate = builder.and(predicate, builder.equal(root.get(params.getKey()), enumValue));
            }

        }
    }
}
