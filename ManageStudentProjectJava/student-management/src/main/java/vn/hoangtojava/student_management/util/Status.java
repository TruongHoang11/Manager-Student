package vn.hoangtojava.student_management.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum Status {
    @JsonProperty("active")
    ACTIVE,
    @JsonProperty("inactive")
    INACTIVE
}
