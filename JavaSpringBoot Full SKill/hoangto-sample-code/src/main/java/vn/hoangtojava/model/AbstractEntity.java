package vn.hoangtojava.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;


// Dù không có method trừu tượng, vẫn dể abstract để:
// Không cho phép tạo đối tượng new AbstractEntity() (vì nó không đại diện cho bảng thật trong DB)
// Dùng làm lớp cơ sở để các entity khác kế thừa logic chung như : id, createAt, updateAt.
@Getter
@Setter
@MappedSuperclass
public abstract class AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column( name = "id")
    private Long id;

    @Column(name = "created_at")
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

}
