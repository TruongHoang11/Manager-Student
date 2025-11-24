package vn.hoangtojava.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import vn.hoangtojava.dto.response.PageResponse;
import vn.hoangtojava.model.Address;
import vn.hoangtojava.model.User;
import vn.hoangtojava.repository.criteria.AddressSearchCriteria;
import vn.hoangtojava.repository.criteria.AddressSearchCriteriaQueryConsumer;
import vn.hoangtojava.repository.criteria.SearchCriteria;
import vn.hoangtojava.repository.criteria.UserSearchCriteriaQueryConsumer;
import vn.hoangtojava.repository.specification.SpecSearchCriteria;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Repository
public class SearchRepository {

    private final UserRepository userRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public PageResponse<?> getAllUserWithSortByMultipleAndSearchColunms(int pageNo, int pageSize, String search, String sortBy){
        int pageno = 0;
        if(pageNo > 0){
            pageno = pageNo - 1;
        }

        // cach lay list user cu theo stream, map, bai nay lay list user theo query nha
//        Pageable pageable = PageRequest.of(pageNo,pageSize,Sort.by(orders));
//        Page<User> users = userRepository.findAll(pageable);
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
//                        .type(user.getType().toString())
//                        .build()).toList();

        // query ra list user new vn.hoangtojava.dto.response.UserDetailResponse( u.firstName,  u.lastName,  u.email)
        StringBuilder sqlQuery = new StringBuilder("SELECT new vn.hoangtojava.dto.response.UserDetailResponse(u.id, u.firstName, u.lastName, u.email) FROM User u WHERE 1=1");

        if(StringUtils.hasLength(search)){
            sqlQuery.append(" AND LOWER(u.firstName) LIKE LOWER(:firstName)");
            sqlQuery.append(" OR LOWER(u.lastName) LIKE LOWER(:lastName)");
            sqlQuery.append(" OR LOWER(u.email) LIKE LOWER(:email)");
        }
        if(sortBy != null){
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sortBy);
            if(matcher.find()){
                sqlQuery.append(String.format(" ORDER BY u.%s %s", matcher.group(1),matcher.group(3)));

            }

        }

        // get list of User
        Query selectQuery = entityManager.createQuery(sqlQuery.toString());
        selectQuery.setFirstResult(pageno);
        selectQuery.setMaxResults(pageSize);
        if(StringUtils.hasLength(search)){

            selectQuery.setParameter("firstName", String.format("%%%s%%", search));
            selectQuery.setParameter("lastName", String.format("%%%s%%", search));
            selectQuery.setParameter("email", String.format("%%%s%%", search));
        }
        List<?> users = selectQuery.getResultList();


        System.out.println(users);

        // query ra so record theo dk tim kiem
        StringBuilder sqlCountQuery = new StringBuilder("SELECT COUNT(*) from User u WHERE 1=1");

        if(StringUtils.hasLength(search)){
            sqlCountQuery.append(" AND ( LOWER(u.firstName) LIKE LOWER(?1)");
            sqlCountQuery.append(" OR LOWER(u.lastName) LIKE LOWER(?2)");
            sqlCountQuery.append(" OR LOWER(u.email) LIKE LOWER(?3) )");

        }


        Query selectCountQuery = entityManager.createQuery(sqlCountQuery.toString());
        if(StringUtils.hasLength(search)){
            selectCountQuery.setParameter(1, String.format("%%%s%%", search));
            selectCountQuery.setParameter(2, String.format("%%%s%%", search));
            selectCountQuery.setParameter(3, String.format("%%%s%%", search));
            selectCountQuery.getSingleResult();
        }
        // DB tra ve dung 1 gia tri duy nhat - so luong ban ghi thoa man dieu kien
        // Hibernate/Jpa nhan ve mot Object -> ep ve kieu Long
        // ket qua cuoi cung: totalElements se chua tong so ban ghi tim duoc
        Long totalElements = (Long) selectCountQuery.getSingleResult();
        System.out.println(totalElements);

        Pageable pageable = PageRequest.of(pageno,pageSize);
        Page<?> page = new PageImpl<>(users,pageable , totalElements);



        return PageResponse.builder()
                .pageNo(pageno)
                .pageSize(pageSize)
                .totalPage(page.getTotalPages())
                .item(users)
                .build();
    }



    // search tat ca field cua  address criteriaSearch

    public PageResponse<?> avancedSearchWithCriteriaAdress(int pageNo, int pageSize, String sortBy,String[] address, String... search){
        // strong mang search se la cac phan tu nhu: firstName: t, lastName: t, email: t
        // bay gio can kiem tra key, operation, value co match voi pattern regex hay khong, fill no vao cac doi tuong
        int pageno = 0;
        if(pageNo > 0){
            pageno = pageNo - 1;
        }
        // 1. Lay danh sach user
        List<SearchCriteria> criteriaList = new ArrayList<>();
        if(search != null){
            Pattern pattern = Pattern.compile("(\\w+?)(:|>|<)(.*)");
            for(String srch: search){
                // firstName:value
                Matcher matcher = pattern.matcher(srch);
                if(matcher.find()){
                    criteriaList.add(new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3)));
                }
            }
        }
        List<AddressSearchCriteria> addressSearchCriteria = new ArrayList<>();
        if(address != null){
            Pattern pattern = Pattern.compile("(\\w+?)(:|>|<)(.*)");
            for(String adres: address){
                // firstName:value
                Matcher matcher = pattern.matcher(adres);
                if(matcher.find()){
                    addressSearchCriteria.add(new AddressSearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3)));
                }
            }
        }


        List<User> users = getUserAdresses(pageno,pageSize,sortBy,criteriaList, addressSearchCriteria);

        // 2. Lay ra tong so ban ghi, tong so element
        Long totalElement = getTotalElementAndAddresses(criteriaList,addressSearchCriteria);

        Pageable pageable = PageRequest.of(pageno, pageSize);
        Page page = new PageImpl(users,pageable,totalElement);


        return PageResponse.builder()
                .pageNo(pageno) // offset: lay tu vi tri offset
                .pageSize(pageSize) // lay tu vi tri offset + them pageSize = so luong ban ghi can lay
                .totalPage(page.getTotalPages())
                .item(users)
                .build();
    }
    private Long getTotalElementAndAddresses(List<SearchCriteria> criteriaList,List<AddressSearchCriteria> addressSearchCriteria) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<User> root = criteriaQuery.from(User.class);

        Predicate userPredicate = criteriaBuilder.conjunction();

        UserSearchCriteriaQueryConsumer queryConsumer = new UserSearchCriteriaQueryConsumer(criteriaBuilder, userPredicate,root);
        for(SearchCriteria c: criteriaList){
            queryConsumer.accept(c);
        }
        userPredicate = queryConsumer.getPredicate();
        Predicate addressPredicate = criteriaBuilder.conjunction();
        Join<User,Address> userAddressJoin = root.join("addresses");
        AddressSearchCriteriaQueryConsumer queryAddress = new AddressSearchCriteriaQueryConsumer(criteriaBuilder, userPredicate,userAddressJoin);
        for(AddressSearchCriteria a : addressSearchCriteria){
            queryAddress.accept(a);
        }
        addressPredicate = queryAddress.getPredicate();

        criteriaQuery.select(criteriaBuilder.count(root));
        criteriaQuery.where(userPredicate,addressPredicate);

        return entityManager.createQuery(criteriaQuery).getSingleResult();

    }

    // end


    // search 1 field cua address
    public PageResponse<?> avancedSearchWithCriteria(int pageNo, int pageSize, String sortBy,String address, String... search){
        // strong mang search se la cac phan tu nhu: firstName: t, lastName: t, email: t
        // bay gio can kiem tra key, operation, value co match voi pattern regex hay khong, fill no vao cac doi tuong
        int pageno = 0;
        if(pageNo > 0){
            pageno = pageNo - 1;
        }
        // 1. Lay danh sach user
        List<SearchCriteria> criteriaList = new ArrayList<>();
        if(search != null){
            Pattern pattern = Pattern.compile("(\\w+?)(:|>|<)(.*)");
            for(String srch: search){
                // firstName:value
                Matcher matcher = pattern.matcher(srch);
                if(matcher.find()){
                    criteriaList.add(new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3)));
                }
            }
        }
        List<User> users = getUsers(pageno,pageSize,sortBy,criteriaList, address);

        // 2. Lay ra tong so ban ghi, tong so element
        Long totalElement = getTotalElement(criteriaList);

        Pageable pageable = PageRequest.of(pageno, pageSize);
        Page page = new PageImpl(users,pageable,totalElement);


        return PageResponse.builder()
                .pageNo(pageno) // offset: lay tu vi tri offset
                .pageSize(pageSize) // lay tu vi tri offset + them pageSize = so luong ban ghi can lay
                .totalPage(page.getTotalPages())
                .item(users)
                .build();
    }

    // search nhieu field cua address phien ban tu custom dcap chua lam get totalElement
    private List<User> getUserAdresses(int pageno, int pageSize, String sortBy, List<SearchCriteria> criteriaList, List<AddressSearchCriteria> addressSearchCriteria) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);



        Predicate predicate = criteriaBuilder.conjunction();
        UserSearchCriteriaQueryConsumer queryConsumer = new UserSearchCriteriaQueryConsumer(criteriaBuilder,predicate,root);


        criteriaList.forEach(queryConsumer);

        predicate = queryConsumer.getPredicate();

        Predicate finalPreadicate = predicate;
        if(addressSearchCriteria != null && !addressSearchCriteria.isEmpty()){
            // tu doi tong address sang doi tuong user, thong qua field addresses
            Join<User,Address> addressUserJoin = root.join("addresses",JoinType.LEFT);
            Predicate addressPredicate = criteriaBuilder.conjunction();
            AddressSearchCriteriaQueryConsumer queryConsumerAddress = new AddressSearchCriteriaQueryConsumer(criteriaBuilder,addressPredicate,addressUserJoin);
            addressSearchCriteria.forEach(queryConsumerAddress);
            addressPredicate = queryConsumerAddress.getPredicate();
            finalPreadicate = criteriaBuilder.and(predicate,addressPredicate);
        }

        criteriaQuery.select(root).distinct(true).where(finalPreadicate);



        // sort
        if(StringUtils.hasLength(sortBy)){
            // firstName: asc|desc
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sortBy);
            if(matcher.find()){
                String sortName = matcher.group(1);
                if(matcher.group(3).equalsIgnoreCase("desc")){
                    criteriaQuery.orderBy(criteriaBuilder.desc(root.get(sortName)));
                }  else{
                    criteriaQuery.orderBy(criteriaBuilder.asc(root.get(sortName)));
                }

            }
        }


        return entityManager.createQuery(criteriaQuery)
                .setFirstResult(pageno)
                .setMaxResults(pageSize)
                .getResultList();


    }

    // end



    private Long getTotalElement(List<SearchCriteria> criteriaList) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<User> root = criteriaQuery.from(User.class);

        Predicate predicate = criteriaBuilder.conjunction();
        UserSearchCriteriaQueryConsumer queryConsumer = new UserSearchCriteriaQueryConsumer(criteriaBuilder,predicate,root);
        for(SearchCriteria c: criteriaList){
            queryConsumer.accept(c);
        }
        predicate = queryConsumer.getPredicate();
        criteriaQuery.select(criteriaBuilder.count(root));
        criteriaQuery.where(predicate);

        return entityManager.createQuery(criteriaQuery).getSingleResult();

    }



    private List<User> getUsers(int pageno, int pageSize, String sortBy, List<SearchCriteria> criteriaList, String address) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);

        // xu ly cac dieu kien tim kiem

        //criteriaBuilder.conjunction() tạo ra một Predicate rỗng nhưng được hiểu như điều kiện TRUE.
        // neu them dieu kien nao -> no cung khong lam thay doi ket qua
        // no giong nhu where 1=1 trong SQL( luon dung )
        Predicate predicate = criteriaBuilder.conjunction();
        UserSearchCriteriaQueryConsumer queryConsumer = new UserSearchCriteriaQueryConsumer(criteriaBuilder,predicate,root);


        // for (SearchCriteria c : criteriaList) {
        //    queryConsumer.accept(c);
        //}
        //duyệt từng điều kiện trong criteriaList và truyền cho queryConsumer.accept().
        //Chuyển đổi từng SearchCriteria thành Predicate.
        //
        //Nối các Predicate này vào predicate chung (dùng AND hoặc OR).
        criteriaList.forEach(queryConsumer);
        predicate = queryConsumer.getPredicate();

        criteriaQuery.where(predicate);
        if(StringUtils.hasLength(address)){
            // tu doi tong address sang doi tuong user, thong qua field addresses
            Join<User,Address> addressUserJoin = root.join("addresses");
            Predicate addressPredicate = criteriaBuilder.like(addressUserJoin.get("city"), "%" + address + "%");
            criteriaQuery.where(predicate,addressPredicate);
        }


        // sort
        if(StringUtils.hasLength(sortBy)){
            // firstName: asc|desc
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sortBy);
            if(matcher.find()){
                String sortName = matcher.group(1);
                if(matcher.group(3).equalsIgnoreCase("desc")){
                    criteriaQuery.orderBy(criteriaBuilder.desc(root.get(sortName)));
                }  else{
                    criteriaQuery.orderBy(criteriaBuilder.asc(root.get(sortName)));
                }

            }
        }


        return entityManager.createQuery(criteriaQuery)
                .setFirstResult(pageno)
                .setMaxResults(pageSize)
                .getResultList();


    }



    public PageResponse getUserJoinedAddress(Pageable pageable, String[] user, String[] address) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> userRoot = criteriaQuery.from(User.class);
        Join<User,Address> addressUserJoin = userRoot.join("addresses");

        List<Predicate> userPre = new ArrayList<>();


        Pattern pattern = Pattern.compile("(\\w+?)([<:>~!])(.*)(\\p{Punct}?)(\\p{Punct}?)");
        for(String s : user){
            Matcher matcher = pattern.matcher(s);
            if(matcher.find()){
                SpecSearchCriteria criteria = new SpecSearchCriteria(matcher.group(1),matcher.group(2),matcher.group(3),matcher.group(4),matcher.group(5));
                Predicate predicate = toUserPredicate(userRoot,criteriaBuilder,criteria);
                userPre.add(predicate);
            }
        }
        List<Predicate> addressPre = new ArrayList<>();
        for(String a : address){
            Matcher matcher = pattern.matcher(a);
            if(matcher.find()){
                SpecSearchCriteria criteria = new SpecSearchCriteria(matcher.group(1),matcher.group(2),matcher.group(3),matcher.group(4),matcher.group(5));
                Predicate predicate1 = toAddressPredicate(addressUserJoin,criteriaBuilder,criteria);
                addressPre.add(predicate1);
            }
        }

        Predicate userPredicateArr = criteriaBuilder.or(userPre.toArray(new Predicate[0]));
        Predicate addressPredicateArr = criteriaBuilder.or(addressPre.toArray(new Predicate[0]));

        Predicate finalPredicate = criteriaBuilder.and(userPredicateArr,addressPredicateArr);

        criteriaQuery.where(finalPredicate);

        List<User> users = entityManager.createQuery(criteriaQuery)
                .setFirstResult(pageable.getPageNumber())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
        long totalElement = getTotalElementJoinedAddresses(user, address);


        return PageResponse.builder()
                .pageNo(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalPage((int)totalElement)
                .item(users)
                .build();


    }

    private long getTotalElementJoinedAddresses(String[] user, String[] address) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<User> userRoot = criteriaQuery.from(User.class);
        Join<User,Address> addressRoot = userRoot.join("addresses");

        List<Predicate> userPre = new ArrayList<>();
        List<Predicate> addressPre = new ArrayList<>();

        Pattern pattern = Pattern.compile("(\\w+?)([:><!~])(.*)(\\p{Punct}?)(\\p{Punct}?)");
        for(String s : user){
            Matcher matcher = pattern.matcher(s);
            if(matcher.find()){
                SpecSearchCriteria criteria = new SpecSearchCriteria(matcher.group(1),matcher.group(2),matcher.group(3),matcher.group(4),matcher.group(5));
                Predicate predicate = toUserPredicate(userRoot,criteriaBuilder,criteria);
                userPre.add(predicate);
            }
        }

        for(String a : address){
            Matcher matcher = pattern.matcher(a);
            if(matcher.find()){
                SpecSearchCriteria criteria = new SpecSearchCriteria(matcher.group(1),matcher.group(2),matcher.group(3),matcher.group(4),matcher.group(5));
                Predicate predicate = toAddressPredicate(addressRoot,criteriaBuilder,criteria);
                addressPre.add(predicate);
            }
        }

        Predicate userPredicateArr = criteriaBuilder.or(userPre.toArray(new Predicate[0]));
        Predicate addressPredicateArr = criteriaBuilder.or(addressPre.toArray(new Predicate[0]));

        Predicate finalPredicate = criteriaBuilder.and(userPredicateArr,addressPredicateArr);

        criteriaQuery.select(criteriaBuilder.count(userRoot));
        criteriaQuery.where(finalPredicate);

        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    private Predicate toUserPredicate(Root<User> root, CriteriaBuilder criteriaBuilder, SpecSearchCriteria criteria) {
        return switch (criteria.getOperation()){
            // equality nhan tham so la 1 object
            case EQUALITY -> criteriaBuilder.equal(root.get(criteria.getKey()),criteria.getValue());
            case NEGATION -> criteriaBuilder.notEqual(root.get(criteria.getKey()), criteria.getValue());
            // greater_than chi nhan tham so Comparable hoac String -> moi ep kieu
            case GREATER_THAN -> criteriaBuilder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString());
            case LESS_THAN -> criteriaBuilder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString());
            case STARTS_WITH -> criteriaBuilder.like(root.get(criteria.getKey()), criteria.getValue() + "%");
            case ENDS_WITH -> criteriaBuilder.like(root.get(criteria.getKey()), "%" + criteria.getValue());
            case LIKE -> criteriaBuilder.like(root.get(criteria.getKey()), "%" + criteria.getValue().toString() + "%");
            case CONTAINS -> criteriaBuilder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
            default -> null;
        };
    }
    private Predicate toAddressPredicate(Join<User,Address> root, CriteriaBuilder criteriaBuilder, SpecSearchCriteria criteria) {
        return switch (criteria.getOperation()){
            // equality nhan tham so la 1 object
            case EQUALITY -> criteriaBuilder.equal(root.get(criteria.getKey()),criteria.getValue());
            case NEGATION -> criteriaBuilder.notEqual(root.get(criteria.getKey()), criteria.getValue());
            // greater_than chi nhan tham so Comparable hoac String -> moi ep kieu
            case GREATER_THAN -> criteriaBuilder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString());
            case LESS_THAN -> criteriaBuilder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString());
            case STARTS_WITH -> criteriaBuilder.like(root.get(criteria.getKey()), criteria.getValue() + "%");
            case ENDS_WITH -> criteriaBuilder.like(root.get(criteria.getKey()), "%" + criteria.getValue());
            case LIKE -> criteriaBuilder.like(root.get(criteria.getKey()), "%" + criteria.getValue().toString() + "%");
            case CONTAINS -> criteriaBuilder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
            default -> null;
        };
    }


}
