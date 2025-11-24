package vn.hoangtojava.repository.specification;

public enum SearchOperation {

    // Bang voi, Phu dinh, Lon hon, Be hon, Giong, Bat dau voi, Ket thuc voi, Chua
    EQUALITY, NEGATION, GREATER_THAN, LESS_THAN, LIKE, STARTS_WITH, ENDS_WITH, CONTAINS;

    public static final String[] SIMPLE_OPERATION_SET = {":", "!", ">", "<", "~"};

    public static final String OR_PREDICATE_FLAG = "'";

    public static final String ZERO_OR_MORE_REGEX  = "*";

    public static final String OR_PREDICATE = "OR";

    public static final String AND_PREDICATE = "AND";

    // DAU NGOAC TRAI
    public static final String LEFT_PARENTHESIS = "(";

    // DAU NGOAC PHAI
    public static final String RIGHT_PARENTHESIS = ")";


    public static SearchOperation getSimpleOperation(final char input){
        return switch (input){
            case ':' -> EQUALITY;
            case '!' -> NEGATION;
            case '>' -> GREATER_THAN;
            case '<' -> LESS_THAN;
            case '~' -> LIKE;
            default -> null;
        };
    }

}

