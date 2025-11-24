package vn.hoangtojava.service;

import jakarta.mail.MessagingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.hoangtojava.dto.request.UserRequestDTO;
import vn.hoangtojava.dto.response.PageResponse;
import vn.hoangtojava.dto.response.UserDetailResponse;
import vn.hoangtojava.model.User;
import vn.hoangtojava.util.UserStatus;

import java.io.UnsupportedEncodingException;

public interface UserService {

    long saveUser(UserRequestDTO request) throws MessagingException, UnsupportedEncodingException;

    void updateUser(long userId, UserRequestDTO request);

    void changeStatus(long userId, UserStatus status);

    void deleteUser(long userId);

    UserDetailResponse getUser(long userId);


    // Để lấy danh sách user theo từng trang, chứ không phải lấy toàn bôj một lúc
    // Gọi là phân trang (Pagination) - giúp tăng tốc độ, tiết kiệm tài nguyên cho cả back và front end
    //pageNO là chỉ số trang bạn muốn lấy, bắt đầu từ số 0, pageSize: số lượng bản ghi trong mỗi trang
    // ví dụ: pageNo: 1, pageSize:10 -> lấy danh sách 10 user trang thứ hai
    // pageNo: 0, pageSize: 20 -> lấy danh sách 20 user trang đầu tiên
    // Binh thuong viet: criteriaList.forEach(c -> queryConsumer.accept(c));
    //Nhưng ở đây queryConsumer đã là một Consumer → không cần viết lambda nữa, chỉ cần truyền thẳng vào forEach.

    PageResponse<?> getAllUsersWithSortBy(int pageNo, int pageSize, String sortBy);
    PageResponse<?> getAllUserWithSortByMultipleColumns(int pageNo, int pageSize, String ... sorts);

    PageResponse<?> getAllUserWithSortByMultipleAndSearchColunms(int pageNo, int pageSize,String search, String sortBy);

    PageResponse<?> avancedSearchWithCriteria(int pageNo, int pageSize, String sortBy,String address, String... search);

    PageResponse<?> avancedSearchWithCriteriaAdress(int pageNo, int pageSize, String sortBy,String []address, String... search);
    User getUserById(long userid);


    PageResponse<?> avancedSearchWithSpecification(Pageable pageable, String[] user, String[] address);

    PageResponse<?> findByFirstName(String name, Pageable pageable);
    PageResponse<?> getAllUserCity(String city, Pageable pageable);

    void confirmUser(long userId, String secretCode);
}
