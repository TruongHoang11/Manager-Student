package vn.hoangtojava.repository.specification;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static vn.hoangtojava.repository.specification.SearchOperation.*;

@Getter
@Setter
@NoArgsConstructor
public class SpecSearchCriteria {


    private String key;
    private SearchOperation operation;
    private Object value;
    private boolean orPredicate;

    public SpecSearchCriteria(final String key, final SearchOperation operation, final Object value){
        super();
        this.key = key;
        this.operation = operation;
        this.value = value;
    }


    public SpecSearchCriteria(final String orPredicate, final String key, final SearchOperation operation, final Object value){
        super();
        this.orPredicate = orPredicate != null && orPredicate.equals(OR_PREDICATE_FLAG);
        this.key = key;
        this.operation = operation;
        this.value = value;
    }

    public SpecSearchCriteria(String key, String operation,Object value, String prefix, String suffix){
        SearchOperation searchOperation = SearchOperation.getSimpleOperation(operation.charAt(0));
        if(searchOperation != null){
            if(searchOperation == EQUALITY){
                final boolean startWithAsterisks = prefix != null && prefix.contains(ZERO_OR_MORE_REGEX);
                final boolean endWithAsterisks = suffix != null && suffix.contains(ZERO_OR_MORE_REGEX);
                if(startWithAsterisks && endWithAsterisks){
                    searchOperation = CONTAINS;
                } else if(startWithAsterisks){
                    searchOperation = ENDS_WITH;
                } else if(endWithAsterisks) {
                    searchOperation = STARTS_WITH;
                }
            }
        }
        this.key = key;
        this.operation = searchOperation;
        this.value = value;
    }

}

