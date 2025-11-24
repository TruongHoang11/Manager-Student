package main.java;
import java.util.*;
import java.time.LocalDate;

public interface SampleInterface {
    // constant fied
    String name = "hoangto";
    // declare method that abstract
    void getName();
    void getBirthday();
    // default method
    default void printName(){
        System.out.println(name);
    }
    // static method
    static String getCurrentTime(){
        return String.valueOf(LocalDate.now());
    }
}
