package vn.hoangtojava.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import vn.hoangtojava.util.Gender;
import vn.hoangtojava.util.UserStatus;
import vn.hoangtojava.util.UserType;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_user")
public class User extends AbstractEntity {

// Column(name = "") map voi cac field trong entity trong DB
    // con ten may field o day call len postMan

    @Column( name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status")
    private UserStatus status;

    @Enumerated(EnumType.STRING)
    // Chỉ định cách lưu Enum (dạng text thay vì số)  'ADMIN' thay vì 0       .ORDINAL thi se luu kieu so
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
//    Nói rằng đây là PostgreSQL enum thực sự, không phải varchar       Kết nối Java Enum ↔ PostgreSQL Enum
    @Column(name = "type")
    //   Gán field type của entity vào cột type trong DB   cột type trong tbl_user
    private UserType type;

    @Column(name= "age")
    private Integer age;
    @Column(name = "activated")
    private Boolean activated;

    //mappedBy = "user": trỏ tới filed user trong class Address, để hibernate biết
    // ai là chủ sở hữu mối quan hệ(ở đây là class Address)
    // Nếu không thêm mappedBy thì thì Hibernate sẽ nghĩ đây là 2 mối quan hệ riêng
    // biệt, nó sẽ tạo một bảng trung gian để quản lý mối quan hệ giữa User và Address

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "user")
        private Set<Address> addresses = new HashSet<>();

    public void saveAddress(Address address){
//        if(addresses != null){
            if(addresses == null) {// nếu Set addresses bằng null thì khởi tạo một HashSet<>()
        addresses = new HashSet<>();
    }
    // nếu không null thì add address vào Set
            addresses.add(address);
    // cập nhập tức là gán thís, this chính là User hiện tại vào Address
    // mỗi address cần biết tôi thuộc về User nào, đúng với @ManyToOne
    // gán User vào trường user trong Adress

            address.setUser(this);
//        }

}


}
