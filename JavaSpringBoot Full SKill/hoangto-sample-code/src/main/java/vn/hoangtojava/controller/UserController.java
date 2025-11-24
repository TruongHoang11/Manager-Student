package vn.hoangtojava.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.hoangtojava.configuration.Translator;
import vn.hoangtojava.dto.request.UserRequestDTO;
import vn.hoangtojava.dto.response.ResponseData;
import vn.hoangtojava.dto.response.ResponseError;
import vn.hoangtojava.exception.ResourceNotFoundException;
import vn.hoangtojava.service.UserService;
import vn.hoangtojava.util.UserStatus;

import java.io.IOException;

// them @RestController , Neu khong co thi Spring se khong scan va khong goi duoc cac API ben trong class
//Giup tu dong tra ve JSON, khong can viet @ResponseBody cho tung method
@RestController
@RequestMapping("/user")
@Validated
@Slf4j
@Tag(name = "User Controller")
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;
//    public UserController(){
//        userService = new UserServiceImpl();
//    }

    // api add user
    @Operation(summary = "Add user", description = "Api created new user")
    @PostMapping(value="/")
    public ResponseData<Long> addUser(@Valid  @RequestBody UserRequestDTO userDTO) {
        log.info("Request add user = {} {}",userDTO.getFirstName(), userDTO.getLastName());
        try{
            long userId = userService.saveUser(userDTO);
            return new ResponseData<>(HttpStatus.CREATED.value(), Translator.toLocale("user.add.success"), userId);
        }
        catch(Exception e){
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Adda user fail");
        }



//        SampleDTO dto = SampleDTO.builder()
//                .id(1)
//                .name("Hoang")
//                .build();

//        try {
//            userService.addUser(userDTO);
//            return new ResponseData<>(HttpStatus.CREATED.value(), Translator.toLocale("user.add.success"), 1);
//        }
//        catch(Exception e){
//            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Save user failed");
//        }
    }

    // end api add user

    // api update user
    @Operation(summary = "Update user", description = "Api updated user")
    @PutMapping("/{userId}")
    public ResponseData<?> updateUser(@PathVariable @Min(1) long userId, @Valid @RequestBody UserRequestDTO user) {
        log.info("Request update userId= {}",userId);
        try{
            userService.updateUser(userId, user);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), Translator.toLocale("user.update.success"));
        } catch(Exception e){
            log.error("Erorr Message= {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Updated User Failed");
        }

    }
    // end api update user

    // api change user status
    @Operation(summary = "Change status of user", description = "Api changed user")
    @PatchMapping("/{userId}")
    public ResponseData<?> changeUserStatus(@PathVariable long userId, @RequestParam(required = false) UserStatus status){
        log.info("Request change user status, userId={}", userId);
        try{
            userService.changeStatus(userId, status);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), Translator.toLocale("user.change.success"));
        } catch(Exception e){
            log.error("Error Message= {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Change Status Of User Failed");
        }

    }

    // api change user status

    @Operation(summary = "Change status of user", description = "Api changed user")
    @GetMapping("/confirm/{userId}")
    public ResponseData<?> confirmUser(@PathVariable long userId, @RequestParam(required = false) String secretCode, HttpServletResponse response) throws IOException {
        log.info("Request confirm user, userId={}, secretCode={}", userId,secretCode);
        try{
            userService.confirmUser(userId, secretCode);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "User confirmed");
        } catch(Exception e){
            log.error("Error Message= {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Confirmation was failure");
        } finally {
            response.sendRedirect("https://www.facebook.com/share/1DfPbvXsTi/");
        }

    }

    // api delete user
    @Operation(summary = "Delete user", description = "Api deleted user")
    @DeleteMapping("/{userId}")
    public ResponseData<?> deleteUser( @Min(1)  @PathVariable long userId){
        log.info("Request delete userId={}", userId);
        try{
            userService.deleteUser(userId);
            return new ResponseData(HttpStatus.NO_CONTENT.value(), Translator.toLocale("user.delete.success"));
        } catch (Exception e) {
            log.error("Erorr Message= {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Delete user failled");
        }

    }

    // end api delete user

    // api get user by user id
    @Operation(summary = "Get user by userId", description = "Api got userDetail")
    @GetMapping("/{userId}")
    public ResponseData<?> getUser(@Min(1) @Valid @PathVariable long userId) {
        log.info("Request get user detail, userId{}" , userId);
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "user", userService.getUserById(userId));
        } catch (ResourceNotFoundException e) {
            log.error("Error Message= {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "User not found");
        }

    }

    // end api get user by

    // api get list user
    @Operation(summary = "Get list users", description = "Api got listUsers")
    @GetMapping("/list")
    public ResponseData<?> getAllUsers(
             @RequestParam(defaultValue = "0", required = false) int pageNo,
            @Min(10) @RequestParam(defaultValue = "10", required = false) int pageSize,
             @RequestParam(required = false) String sortBy
    )
    {
        log.info("Request get all users");
        try{
            return  new ResponseData<>(HttpStatus.ACCEPTED.value(),"users",userService.getAllUsersWithSortBy(pageNo,pageSize, sortBy));
        }
        catch (Exception e){
            log.error("Message Error= {}", e.getMessage(),e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get all user failed");
        }
    }

    // end api get list user

    // api get list user with sort by multiple columns
    @Operation(summary = "Get list users with sort by multiple columns", description = "Api got listUsers")
    @GetMapping("/list-with-sort-by-multiple-columns")
    public ResponseData<?> getAllUserWithSortByMultipleColumns(
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @RequestParam(defaultValue = "10", required = false) int pageSize,
            @RequestParam(required = false) String... sorts
    )
    {
        log.info("Request get all users with sort by multiple columns");
        try{
            return  new ResponseData<>(HttpStatus.ACCEPTED.value(),"users",userService.getAllUserWithSortByMultipleColumns(pageNo,pageSize, sorts));
        }
        catch (Exception e){
            log.error("Message Error= {}", e.getMessage(),e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get all user failed");
        }
    }
    // end api get list user with sort by multiple column


    //api get list user with sort by multiple and search column
    @Operation(summary = "Get list user with sort by multiple and search column", description = "Api get list user with sort and search column")
    @GetMapping("/list-with-sort-and-search-column")
    public ResponseData<?> getAllUserWithSortByMultipleAndSearchColunms(
            @RequestParam(defaultValue = "0",required = false) int pageNo,
            @RequestParam(defaultValue = "20", required = false) int pageSize,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sortBy
            )
    {
        log.info("Request get all user with sort and search columns");
        try{
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "users", userService.getAllUserWithSortByMultipleAndSearchColunms(pageNo,pageSize,search,sortBy));
        } catch (Exception e){
            log.error("Message Error= {}", e.getMessage(),e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get all user failed");
        }

    }


    // end api get list user with sort by multiple and search


    // api get list user with avanced-search-with-criteria-and-sort-by-column
    @Operation(summary = "Get list user with sort by and advanced search with criteria", description = "Api get list user with sort and avanced search with criteria")
    @GetMapping("/avanced-search-with-criteria")
    public ResponseData<?> avanceSearchWithCriteria(
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @RequestParam(defaultValue = "20", required = false) int pageSize,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String address,
            @RequestParam(defaultValue = "", required = false) String... search
    )
    {
        log.info("Request get all user with Avanced Search With Criteria");
        try{
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "users", userService.avancedSearchWithCriteria(pageNo,pageSize,sortBy,address,search));
        } catch(Exception e){
            log.error("Erorr Message= {}", e.getMessage(),e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get all users by Avance Criteria Failled");
        }
    }


    // end api get list user with avanced-search-with-criteria-and-sort-by-column


    // api get list user with avanced-search-with-criteria-and-sort-by-column and address
    @Operation(summary = "Get list user with sort by and avanced search with criteria,address", description = "Api get list user with sort and avanced search with criteria,address")
    @GetMapping("/avanced-search-with-criteria-address")
    public ResponseData<?> avancedSearchWithCriteria(
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @RequestParam(defaultValue = "20", required = false) int pageSize,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String []address,
            @RequestParam(defaultValue = "", required = false) String... search
    )
    {
        log.info("Request get all user with Avanced Search With Criteria Address");
        try{
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "users", userService.avancedSearchWithCriteriaAdress(pageNo,pageSize,sortBy,address,search));
        } catch(Exception e){
            log.error("Erorr Message= {}", e.getMessage(),e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get all users by Avanced Criteria Search Failled");
        }
    }
    // end api

    //
    @Operation(summary = "Get list user with sort by and avanced search with specification", description = "Api get list user with sort and avanced search with specification")
    @GetMapping("/avanced-search-with-specification")
    public ResponseData<?> avancedSearchWithSpecification(
            Pageable pageable,
            @RequestParam(required = false) String[] user,
            @RequestParam(required = false) String[] address

    )
    {
        log.info("Request get all user with Avanced Search With Specification");
        try{
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "users", userService.avancedSearchWithSpecification(pageable,user,address));
        } catch(Exception e){
            log.error("Erorr Message= {}", e.getMessage(),e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get all users by Avanced Specification Search Failled");
        }
    }

    @Operation(summary = "Get  user by last name and pageable", description = "Api get  user by last name with pageable")
    @GetMapping("/get-user-by-lastName")
    public ResponseData<?> findUserByLastName(
            Pageable pageable,
            @RequestParam String name

    )
    {
        log.info("Request get user find by last name");
        try{

            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "users", userService.findByFirstName(name,pageable));
        } catch(Exception e){
            log.error("Erorr Message= {}", e.getMessage(),e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Find user by last name Failled");
        }
    }



    @Operation(summary = "Get  user by city", description = "Api get  user by city with pageable")
    @GetMapping("/get-user-by-city")
    public ResponseData<?> findUserByCity(
            Pageable pageable,
            @RequestParam String city

    )
    {
        log.info("Request get user find by city");
        try{

            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "users", userService.getAllUserCity(city,pageable));
        } catch(Exception e){
            log.error("Erorr Message= {}", e.getMessage(),e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Find user by last city Failled");
        }
    }
}
