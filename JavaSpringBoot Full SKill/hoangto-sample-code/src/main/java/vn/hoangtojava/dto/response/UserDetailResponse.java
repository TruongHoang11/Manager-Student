package vn.hoangtojava.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import vn.hoangtojava.dto.validator.PhoneNumber;
import vn.hoangtojava.util.Gender;
import vn.hoangtojava.util.UserStatus;

import java.io.Serializable;
import java.util.Date;

//cho phép object java -> byte stream(ghi file, gửi mạng) , Lưu redis, lưu session, dùng socket
//  DTO nên implement Serializable để an toàn mở rộng sau này

@Getter
@Builder
@AllArgsConstructor
public class UserDetailResponse implements Serializable {
    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private Date dateOfBirth;
    private Gender gender;
    private String username;
    private String password;
    private String type;
    private UserStatus status;

    public UserDetailResponse(Long id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}

