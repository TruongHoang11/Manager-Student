package vn.hoangtojava.repository.criteria;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.function.Consumer;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchCriteriaQueryConsumer implements Consumer<SearchCriteria> {

    private CriteriaBuilder builder;
    private Predicate predicate;
    private Root root;


    @Override
    public void accept(SearchCriteria param) {
        if(param.getOpration().equals(">")){
            predicate = builder.and(predicate,builder.greaterThanOrEqualTo(root.get(param.getKey()), root.get(param.getValue().toString())));
        } else if(param.getOpration().equals("<")){
            predicate = builder.and(predicate,builder.lessThanOrEqualTo(root.get(param.getKey()), root.get(param.getValue().toString())));
        } else{
            if(root.get(param.getKey()).getJavaType() == String.class){
                predicate = builder.and(predicate,builder.like(root.get(param.getKey()), "%" + param.getValue() + "%"));
            } else{
                predicate = builder.and(predicate,builder.equal(root.get(param.getKey()), root.get(param.getValue().toString())));
            }
        }
    }
}
