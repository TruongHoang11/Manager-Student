package vn.hoangtojava.student_management.repository.specification;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import vn.hoangtojava.student_management.model.Student;

import java.util.ArrayList;
import java.util.List;

import static vn.hoangtojava.student_management.repository.specification.SearchOperation.*;

@Getter
public class StudentSpecificationBuilder {

    public final List<SpecSearchCriteria> params;
    public StudentSpecificationBuilder() {
        params = new ArrayList<>();
    }


    public StudentSpecificationBuilder with (String key, String operation, Object value, String prefix, String suffix){
        return with(null, key,operation, value,prefix, suffix);
    }

   public StudentSpecificationBuilder with (String orPredicate, String key, String operation, Object value, String prefix, String suffix){
       SearchOperation searchOperation = SearchOperation.getOperation(operation.charAt(0));
       if(searchOperation != null){
           if(searchOperation == EQUALITY){
               boolean startWithSao = prefix != null && prefix.contains(DAU_SAO);
               boolean endWithSao = suffix != null && suffix.contains(DAU_SAO);
               if(startWithSao && endWithSao){
                   searchOperation = CONTAINS;
               } else if(endWithSao){
                   searchOperation = STARTS_WITH;
               } else if(startWithSao ){
                   searchOperation = ENDS_WITH;
               }
           }
       }
        params.add(new SpecSearchCriteria(orPredicate, key, searchOperation, value));
       return this;
   }


   public Specification<Student> build(){
        if(params.isEmpty()) {
            return null;
        }
        Specification<Student> specification = new StudentSpecification(params.get(0));

        for(int i = 1; i < params.size(); i++){
            specification = params.get(i).isOrPredicate()
                    ? Specification.where(specification).or(new StudentSpecification(params.get(i)))
                    : Specification.where(specification).and(new StudentSpecification(params.get(i)));
        }
        return specification;
   }

}
