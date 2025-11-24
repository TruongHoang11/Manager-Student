package vn.hoangtojava.repository.criteria;

import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.hoangtojava.model.Address;
import vn.hoangtojava.model.User;

import java.util.function.Consumer;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressSearchCriteriaQueryConsumer implements Consumer<AddressSearchCriteria> {

    private CriteriaBuilder builder ;
    private Predicate predicate ;
    private  Join<User, Address> join;


    @Override
    public void accept(AddressSearchCriteria param) {
        if(param.getOpration().equals(">")){
            predicate = builder.and(predicate,builder.greaterThanOrEqualTo(join.get(param.getKey()),join.get(param.getValue().toString())));
        } else if(param.getOpration().equals("<")){
            predicate = builder.and(predicate,builder.lessThanOrEqualTo(join.get(param.getKey()),join.get(param.getValue().toString())));
        } else{
            if(join.get(param.getKey()).getJavaType() == String.class){
                predicate = builder.and(predicate,builder.like(join.get(param.getKey()),"%"+param.getValue() + "%"));
            }else{
                predicate = builder.and(predicate,builder.equal(join.get(param.getKey()),join.get(param.getValue().toString())));
            }
        }
    }
}
