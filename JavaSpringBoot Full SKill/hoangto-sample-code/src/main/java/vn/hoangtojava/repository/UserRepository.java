package vn.hoangtojava.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.hoangtojava.dto.response.PageResponse;
import vn.hoangtojava.model.User;

import java.util.Collection;
import java.util.Date;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    // neu nativeQuery thi cau SQL viet Hoa, con JQA query thi viet lower thoi
    // native query tu
    //  @Query(nativeQuery = true, value = "SELECT * FROM TBL_USER") native query tren table

    // jpa query qua entity
    @Query(value = "select u" +
            " from User u inner join u.addresses a  " +
            "where a.city=:city")
    Page<User> getAllUserCity(String city, Pageable pageable);




    // =======Distinct=======
//    @Query(value = "select distinct from User u where u.firstNam=:firstName and u.lastName=:lastName")
    Page<User> findDistinctByFirstNameAndLastName(String firstName, String lastName, Pageable pageable);

    // =======Single field=======
//    @Query(value = "select * from User u where u.email= ?1")
    Page<User> findByEmail(String email, Pageable pageable);

    //=======OR=======
//    @Query(value = "select * from User u where u.firstName=:name or u.lastName=:name")
    Page<User> findByFirstNameOrLastName(String fName,String lName, Pageable pageable);

    //=======Is, Equals=======
//    @Query(value = "select * from User u where u.firstName=:name")
    Page<User> findByFirstName(String name, Pageable pageable);
    Page<User> findByFirstNameIs(String name, Pageable pageable);
    Page<User> findByFirstNameEquals(String name, Pageable pageable);


    //=======Between=======
//    @Query(value = "select * from User u where u.createdAt between ?1 and ?2")
    Page<User> findByCreatedAtBetween(Date startDate, Date endDate, Pageable pageable);

    //=======LessThan,GreaterThan, LessThanEquals, GreaterThanEquals=======
//    @Query(value = "select * from User u where u.age < :age")
    Page<User> findByAgeLessThan(int age, Pageable pageable);
    Page<User> findByAgeLessThanEqual(int age,Pageable pageable);
    Page<User> findByAgeGreaterThan(int age,Pageable pageable);
    Page<User> findByAgeGreaterThanEqual(int age,Pageable pageable);



    //=======Before, After=======
//    @Query (value = "select * from User u where u.createdAt < :date")
    Page<User> findByCreatedAtBefore(Date date, Pageable pageable);
    Page<User> findByCreatedAtAfter(Date date, Pageable pageable);

    //=======Null, IsNull=======
//    @Query(value = "select * from User u where u.age is null")
    Page<User> findByAgeIsNull( Pageable pageable);

    //=======NotNull, IsNotNull=======
//    @Query(value = "select * from User u where u.age is not null")
    Page<User> findByAgeIsNotNull( Pageable pageable);

    //=======Like=======
//    @Query(value = "select * from User u where u.lastName like %:name%")
    Page<User> findByLastNameLike(String name, Pageable pageable);

    //=======NotLike=======
//    @Query(value = "select * from User u where u.lastName not like %:name%")
    Page<User> findByLastNameNotLike(String name, Pageable pageable);

    //=======StartingWith======= bat dau voi
//    @Query(value = "select * from User u where u.lastName like :name%")
    Page<User> findByLastNameStartingWith(String name, Pageable pageable);

    //=======EndingWith======= ket thuc voi
//    @Query(value = "select * from User u where u.lastName like :name%")
    Page<User> findByLastNameEndingWith(String name, Pageable pageable);

    //=======Containing=======
//    @Query(value = "select * from User u where u.firstName like %:firstName%")
    Page<User> findByFirstNameContaining(String firstName, Pageable pageable);

    //=======Not=======
//    @Query(value = "select * from User u where u.lastName <> :name")
    Page<User> findByLastNameNot(String name, Pageable pageable);

    //=======In=======
//    @Query(value = "select * from User u where u.age in (18,25,30)")
    Page<User> findByAgeIn(Collection<Integer> ages, Pageable pageable);

    //=======NotIn=======
//    @Query(value = "select * from User u where u.age not in (18,25,30)")
    Page<User> findByAgeNotIn(Collection<Integer> ages, Pageable pageable);

    //=======True/False=======
//    @Query(value = "select * from User u where u.activated = true")
    Page<User> findByActivatedTrue( Pageable pageable);
    Page<User> findByActivatedFalse( Pageable pageable);


    // IgnoreCase
//    @Query(value = "select * from User u where lower(u.firstName) = lower(:name) ")
    Page<User> findByFirstNameIgnoreCase(String name, Pageable pageable);

    // =======OrderBy=======

    Page<User> findByFirstNameOrderByCreatedAtDesc(String name, Pageable pageable);

    Page<User> findByFirstNameAndLastNameAllIgnoreCase(String firstName, String lastName, Pageable pageable);

    // Them And de ket noi

    // existsByStudentAndSubject



}
