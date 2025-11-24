package vn.hoangtojava.student_management.repository.specification;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static vn.hoangtojava.student_management.repository.specification.SearchOperation.*;

@Getter
@Setter
@NoArgsConstructor
public class SpecSearchCriteria {
    private String key;
    private SearchOperation searchOperation;
    private Object value;
    private boolean orPredicate;

    public SpecSearchCriteria(String key, SearchOperation operation, Object value){
        super();
        this.key = key;
        this.searchOperation = operation;
        this.value = value;
    }
    public SpecSearchCriteria(String orPredicate, String key, SearchOperation operation, Object value){
        super();
        this.orPredicate = orPredicate != null && orPredicate.contains(OR_PREDICATE_FLAG);
        this.key = key;
        this.searchOperation = operation;
        this.value = value;
    }

    public SpecSearchCriteria(String key, String oper, Object value, String prefix, String suffix){
        SearchOperation operation = SearchOperation.getOperation(oper.charAt(0));
        if(operation != null){
            if(operation == EQUALITY){
                boolean startWithSao = prefix != null && prefix.contains(DAU_SAO);
                boolean endWithSao = suffix != null && suffix.contains(DAU_SAO);
                if(startWithSao && endWithSao){
                    operation = CONTAINS;
                } else if(endWithSao){
                    operation = STARTS_WITH;
                } else if(startWithSao){
                    operation = ENDS_WITH;
                }
            }
            this.key = key;
            this.searchOperation = operation;
            this.value = value;
        }
    }
}
