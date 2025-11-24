package vn.hoangtojava.service.impl;


import jakarta.mail.MessagingException;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import vn.hoangtojava.dto.request.UserRequestDTO;
import vn.hoangtojava.dto.response.PageResponse;
import vn.hoangtojava.dto.response.UserDetailResponse;
import vn.hoangtojava.exception.ResourceNotFoundException;
import vn.hoangtojava.model.Address;
import vn.hoangtojava.model.User;
import vn.hoangtojava.repository.SearchRepository;
import vn.hoangtojava.repository.UserRepository;
import vn.hoangtojava.repository.specification.UserSpec;
import vn.hoangtojava.repository.specification.UserSpecificationBuilder;
import vn.hoangtojava.service.MailService;
import vn.hoangtojava.service.UserService;
import vn.hoangtojava.util.Gender;
import vn.hoangtojava.util.UserStatus;
import vn.hoangtojava.util.UserType;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service // cho application biet class nay thuoc tang service
@Slf4j
@RequiredArgsConstructor
@Builder
public class UserServiceImpl implements UserService {


    // @RequiredArgConstructor chỉ nhận các biến final.
    // lombok sẽ tự động tạo ra cấu trúc :
    // public UserReposiroty (UserRepository userRepository){
    // this.userRepository = userRepository}
    private final UserRepository userRepository;
    private final SearchRepository searchRepository;
    private final MailService mailService;

    @Override
    public long saveUser(UserRequestDTO request) throws MessagingException, UnsupportedEncodingException {

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .username(request.getUsername())
                .password(request.getPassword())
                .status(request.getUserStatus())
                // valueOf chuyển một chuỗi String thành giá trị enum tương ứng
                //UserType type = UserType.valueOf("USER")
                // sout(type) => in ra USER
                .type(UserType.valueOf(request.getType().toUpperCase()))
                .build();

//        for (AddressRequest a : request.getAddresses()) {
//            Address address = Address.builder()
//                    .apartmentNumber(a.getApartmentNumber())
//                    .floor(a.getFloor())
//                    .building(a.getBuilding())
//                    .streetNumber(a.getStreetNumber())
//                    .street(a.getStreet())
//                    .city(a.getCity())
//                    .country(a.getCountry())
//                    .addressType(a.getAddressType())
//                    .build();
//
//            user.saveAddress(address);
//        }
        request.getAddresses().forEach(a ->
                user.saveAddress(Address.builder()
                        .apartmentNumber(a.getApartmentNumber())
                        .floor(a.getFloor())
                        .building(a.getBuilding())
                        .streetNumber(a.getStreetNumber())
                        .street(a.getStreet())
                        .city(a.getCity())
                        .country(a.getCountry())
                        .addressType(a.getAddressType())
                        .build()));

        userRepository.save(user);
        if(user.getId() != null){
            // send email confirm tại đây
            mailService.confirmEmailLink(user.getEmail(), user.getId(), "secretcode");

        }

        // ghi ra console/log file, {} là placeholder, sẽ được thay thế bởi user.getId()
        log.info("User has added successfully, userId={}", user.getId());


        // trả về id user vừa mới lưu
        return user.getId();

    }

    @Override
    public void updateUser(long userId, UserRequestDTO request) {
        User user = getUserById(userId);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setGender(request.getGender());
        user.setPhone(request.getPhone());
        if(!request.getEmail().equals(user.getEmail())){
            // check email tu DB neu chua co ton tai thi update, otherwise throw exception tu ham getUserId.
            user.setEmail(request.getEmail());
        }
//        if(StringUtils.hasLength(request.getUsername())){ // hasLength : check null hoac rong, neu rong or null moi chay
//            user.setUsername(request.getUsername());
//        }
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setStatus(request.getUserStatus());
        user.setType(UserType.valueOf(request.getType().toUpperCase()));

        userRepository.save(user);
        log.info("Updated user successfully");
    }

    @Override
    public void changeStatus(long userId, UserStatus status) {
        User user = getUserById(userId);
        user.setStatus(status);
        userRepository.save(user);
        log.info("Status was changed successfull");
    }

    @Override
    public void deleteUser(long userId) {
        User user = getUserById(userId);
        userRepository.deleteById(user.getId());
        log.info("User deleted, usedId= {}", user.getId());
    }

    @Override
    public UserDetailResponse getUser(long userId) {
        User user = getUserById(userId);

        return UserDetailResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .email(user.getEmail())
                .dateOfBirth(user.getDateOfBirth())
                .gender(user.getGender())
                .username(user.getUsername())
                .password(user.getPassword())
                .type(user.getType().name())
                .status(user.getStatus())
                .build();
    }

    @Override
    public PageResponse<?> getAllUsersWithSortBy(int pageNo, int pageSize, String sortBy) {
        int page = 0;
        if(pageNo > 0){
            page = pageNo - 1;
        }

        List<Sort.Order> sorts = new ArrayList<>();

        if(StringUtils.hasLength(sortBy)){
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sortBy);
            if(matcher.find()){
                if(matcher.group(3).equalsIgnoreCase("asc")){
                    sorts.add(new Sort.Order(Sort.Direction.ASC,matcher.group(1)));
                }   else{
                    sorts.add(new Sort.Order(Sort.Direction.DESC,matcher.group(1)));
                }
            }
        }


        Pageable pageable = PageRequest.of(page,pageSize, Sort.by(sorts));

        Page<User> users = userRepository.findAll(pageable);

        return convertToPageResponse(users,pageable);
//        List<UserDetailResponse> responses = users.stream()
//                .map(user -> UserDetailResponse.builder()
//                        .id(user.getId())
//                        .firstName(user.getFirstName())
//                        .lastName(user.getLastName())
//                        .phone(user.getPhone())
//                        .email(user.getEmail())
//                        .dateOfBirth(user.getDateOfBirth())
//                        .gender(user.getGender())
//                        .username(user.getUsername())
//                        .password(user.getPassword())
//                        .status(user.getStatus())
//                        .type(user.getType().name())
//                        .build()).toList();
//
//        return PageResponse.builder()
//                .pageNo(pageNo)
//                .pageSize(pageSize)
//                .totalPage(users.getTotalPages())
//                .item(responses)
//                .build();



    }

    @Override
    public PageResponse<?> getAllUserWithSortByMultipleColumns(int pageNo, int pageSize, String... sorts) {
        int page = 0;
        if(pageNo > 0){
            page = pageNo - 1;
        }

        List<Sort.Order> orders = new ArrayList<>();
        if(sorts != null){
            for(String sortBy: sorts){
                Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
                Matcher matcher = pattern.matcher(sortBy);
                if(matcher.find()){
                    if(matcher.group(3).equalsIgnoreCase("asc")){
                        orders.add(new Sort.Order(Sort.Direction.ASC,matcher.group(1)));
                    }   else{
                        orders.add(new Sort.Order(Sort.Direction.DESC,matcher.group(1)));
                    }
                }

            }
        }

        Pageable pageable = PageRequest.of(page,pageSize, Sort.by(orders));



        Page<User> users = userRepository.findAll(pageable);

        return convertToPageResponse(users,pageable);



//        return users.stream()
//                .map(user -> UserDetailResponse.builder()
//                        .id(user.getId())
//                        .firstName(user.getFirstName())
//                        .lastName(user.getLastName())
//                        .phone(user.getPhone())
//                        .email(user.getEmail())
//                        .dateOfBirth(user.getDateOfBirth())
//                        .gender(user.getGender())
//                        .username(user.getUsername())
//                        .password(user.getPassword())
//                        .status(user.getStatus())
//                        .type(user.getType().name())
//                        .build()
//                ).toList();
    }

    @Override
    public PageResponse<?> getAllUserWithSortByMultipleAndSearchColunms(int pageNo, int pageSize, String search, String sortBy) {
//        return searchRepository.getAllUserWithSortByMultipleAndSearchColunms(pageNo,pageSize,search,sortBy);
        return searchRepository.getAllUserWithSortByMultipleAndSearchColunms(pageNo,pageSize,search,sortBy);
    }

    @Override
    public PageResponse<?> avancedSearchWithCriteria(int pageNo, int pageSize, String sortBy, String address, String... search) {
        return searchRepository.avancedSearchWithCriteria( pageNo,  pageSize,  sortBy,address,  search);
    }

    @Override
    public PageResponse<?> avancedSearchWithCriteriaAdress(int pageNo, int pageSize, String sortBy, String[] address, String... search) {
        return searchRepository.avancedSearchWithCriteriaAdress( pageNo,  pageSize,  sortBy,  address,  search);
    }


    @Override
     public User getUserById(long userId){
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public PageResponse<?> avancedSearchWithSpecification(Pageable pageable, String[] user, String[] address) {


        if(user != null && address != null){
            // tim kiem tren user va address --> join table
        return searchRepository.getUserJoinedAddress(pageable,user,address);
        } else if(user != null ){
            // tim kiem tren user --> khong can join sang bang address

//            Specification<User> firstNameSpec = UserSpec.hasFirstName("B");
//            Specification<User> genderSpec = UserSpec.notEqualGender(Gender.FEMALE);
//            Specification<User> finalSpec = firstNameSpec.and(genderSpec);

            UserSpecificationBuilder builder = new UserSpecificationBuilder();
            for(String s : user){
                Pattern pattern = Pattern.compile("(\\w+?)([<:>~!])(.*)(\\p{Punct}?)(\\p{Punct}?)");
                Matcher matcher = pattern.matcher(s);
                if(matcher.find()){
                    builder.with(matcher.group(1),matcher.group(2),matcher.group(3),matcher.group(4),matcher.group(5));
                }
            }
            Page<User> list = userRepository.findAll(builder.build(), pageable);
            return convertToPageResponse(list,pageable);


        }
        Page<User> users = userRepository.findAll(pageable);

        return convertToPageResponse(users,pageable);

    }



    private PageResponse<?> convertToPageResponse(Page<User> users, Pageable pageable){
        //chuyen doi danh sach User lay tu DB thanh danh sach DTO UserDetailResponse de tra ve cho client, theo dung
        // nguyen tac tach biet Entity <-> DT).
        List<UserDetailResponse> response = users.stream()
                .map(user -> UserDetailResponse.builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .phone(user.getPhone())
                        .email(user.getEmail())
                        .dateOfBirth(user.getDateOfBirth())
                        .gender(user.getGender())
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .status(user.getStatus())
                        .type(user.getType().name())
                        .build()).toList();

        return PageResponse.builder()
                .pageNo(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalPage(users.getTotalPages())
                .item(response)
                .build();
    }



    // JPA Query Method
    @Override
    public PageResponse<?> findByFirstName(String name, Pageable pageable) {
        Page<User> users =  userRepository.findByFirstName(name,pageable);
        return PageResponse.builder()
                .pageNo(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalPage(users.getTotalPages())
                .item(users.stream().toList())
                .build();
    }

    @Override
    public PageResponse<?> getAllUserCity(String city, Pageable pageable) {
        Page<User> users = userRepository.getAllUserCity(city, pageable);

        return convertToPageResponse(users,pageable);
    }

    @Override
    public void confirmUser(long userId, String secretCode) {

    }


}
