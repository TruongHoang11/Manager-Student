package vn.hoangtojava.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

// tự dộng tạo getter cho các field
@Getter
// tự động tạo setter cho các field
@Setter
@Builder
// tự động tạo constructor không có tham số
@NoArgsConstructor
// tự dộng tạo constructor đầy đủ tham số
@AllArgsConstructor
@Entity
@Table(name="tbl_address")
public class Address extends AbstractEntity {


    @Column(name = "apartment_number")
    private String apartmentNumber;

    @Column(name = "floor")
    private String floor;

    @Column(name = "building")
    private String building;

    @Column(name = "street_number")
    private  String streetNumber;

    @Column(name = "street")
    private  String street;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "address_type")
    private Integer addressType;












}
