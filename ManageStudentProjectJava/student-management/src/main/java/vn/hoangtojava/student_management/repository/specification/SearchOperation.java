package vn.hoangtojava.student_management.repository.specification;


import lombok.Getter;
import vn.hoangtojava.student_management.dto.response.PageResponse;

@Getter
public enum SearchOperation {
    EQUALITY, NEGATION, GREATER_THAN, LESS_THAN, LIKE, STARTS_WITH, ENDS_WITH, CONTAINS;

    public static final String[] SIMPLE_OPERATION_SET = {":", ">", "<", "~", "!"};

    public static final String OR_PREDICATE_FLAG = "'";

    public static final String DAU_SAO = "*";

    public static final String OR_PREDICATE = "OR";

    public static final String AND_PREDICATE = "AND";

    public static final String LEFT_PARENTHESIS = "(";

    public static final String  RIGHT_PARENTHESIS = ")";

    public static SearchOperation getOperation(final char input){
        return switch (input){
            case ':' -> EQUALITY;
            case '>' -> GREATER_THAN;
            case '<' -> LESS_THAN;
            case '~' -> LIKE;
            case '!' -> NEGATION;
            default -> null;
        };


    }

}
