package vn.hoangtojava.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.*;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;
import vn.hoangtojava.dto.validator.EnumPattern;
import vn.hoangtojava.dto.validator.EnumValue;
import vn.hoangtojava.dto.validator.GenderSubset;
import vn.hoangtojava.dto.validator.PhoneNumber;
import vn.hoangtojava.util.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static vn.hoangtojava.util.Gender.*;

// implments Serializable vi no co qua trinh chuyen doi tu JSON sang nhi phan va nguoc lai. (qua trinh request va reponse)
@Getter
public class UserRequestDTO implements Serializable {

    @NotBlank(message = "{error.firstName.notBlank}")
    private String firstName;

    @NotNull(message= "lastName must be not null")
    private String lastName;

//    @Pattern(regexp = "\\d{10}$", message = "phone invalid format")
    @PhoneNumber
    private String phone;

    @Pattern(regexp = "^[a-zA-Z0-9.+-_.]+@gmail\\.com$", message = "email invalid format")
    private String email;

//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "dateOfBirth must be not null")
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dateOfBirth;

    @NotEmpty(message = "address must be not empty")
    private Set<AddressDTO> addresses;

//    @Pattern(regexp = "^ACTIVE|INACTIVE|NONE$")
//    private String userStatus;

    @EnumPattern(name = "userStatus", regexp = "ACTIVE|UNACTIVE|NONE")
    private UserStatus userStatus;

    @GenderSubset(anyOf = {MALE, FEMALE, OTHER})
    private Gender gender;

    private String username;
    private String password;

    @NotNull(message = "type must be not null")
    @EnumValue(name = "type", enumClass = UserType.class)
    private String type;


    public UserRequestDTO(String firstName, String lastName, String phone, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
    }




}
