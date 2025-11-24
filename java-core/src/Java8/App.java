package Java8;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class App {
    public static void main(String[] args) {
        User user1 = new User();
        user1.setId(1);
        user1.setName("John");
        User user2 = new User();
        user2.setId(2);
        user1.setName("Anna");
        List<User> arr1 = List.of(user1, user2);
        List<User> arr2 = null;
//        if(arr2.isEmpty()){
//            System.out.println("List empty");
//        }
        Optional<List<User>> optionalUsers = Optional.of(arr1);
        optionalUsers.ifPresent(System.out::println);
        Optional<User> user = Optional.of(user1);
        user.ifPresent(System.out::println);


                LocalDate today = LocalDate.now();
                LocalDate sunday = LocalDate.of(2024, Month.OCTOBER, 6);

                System.out.println(today);
                System.out.println(sunday);
                if(today.isAfter(sunday))
                    System.out.println("to day is after sunday");
                else System.out.println("Sai Bet");



    }
}
