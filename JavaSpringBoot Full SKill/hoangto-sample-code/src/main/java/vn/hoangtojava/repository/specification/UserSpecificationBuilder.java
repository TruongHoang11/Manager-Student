package vn.hoangtojava.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import vn.hoangtojava.model.User;

import java.util.ArrayList;
import java.util.List;

import static vn.hoangtojava.repository.specification.SearchOperation.*;
import static vn.hoangtojava.repository.specification.SearchOperation.STARTS_WITH;

public class UserSpecificationBuilder {

    public final List<SpecSearchCriteria> params;

    public UserSpecificationBuilder() {
        params = new ArrayList<>();
    }

    public UserSpecificationBuilder with (String key, String operation, Object value, String prefix, String suffix){
        return with( null, key,  operation,  value,  prefix,  suffix);
    }
    public UserSpecificationBuilder with(String orPredicate,String key, String operation, Object value, String prefix, String suffix){
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
            params.add(new SpecSearchCriteria(orPredicate,key,searchOperation,value));
        }

        return this; // return lai chinh no: UserSpecificationBuilder
    }

    public Specification<User> build(){
        if(params.isEmpty()) return null;

        Specification<User> specification = new UserSpecification(params.get(0));
        for (int i = 1; i < params.size() ; i++) {
            specification = params.get(i).isOrPredicate()
                    ? Specification.where(specification).or(new UserSpecification(params.get(i)))
                    : Specification.where(specification).and(new UserSpecification(params.get(i)));

        }
        return specification;
    }




}


