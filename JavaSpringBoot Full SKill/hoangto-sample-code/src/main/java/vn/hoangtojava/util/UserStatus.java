package vn.hoangtojava.util;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum UserStatus {
    @JsonProperty("none")
    NONE,
    @JsonProperty("active")
    ACTIVE,
    @JsonProperty("unactive")
    UNACTIVE
}
