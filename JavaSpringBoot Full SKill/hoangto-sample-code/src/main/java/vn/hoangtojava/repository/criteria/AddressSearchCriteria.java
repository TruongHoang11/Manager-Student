package vn.hoangtojava.repository.criteria;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddressSearchCriteria {
    private String key; // fisrtName, lastName, email
    private String opration; // toan tu: =, >, <, like de so sanh
    private Object value; // gia tri cua key, vi no co the la %d, %s, Object => nen de no kieu Object
}
