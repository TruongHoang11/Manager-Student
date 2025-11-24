package regex;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KhaiBaoRegex {
    public static void main(String[] args) {
//        // cach 1
//        Pattern pattern = Pattern.compile("Jav.");
//        Matcher matcher = pattern.matcher("Java");
//        boolean result = matcher.matches();
//        System.out.println(result);
//        // cach 2
//        boolean result1 = pattern.compile("Jav.").matcher("Java").matches();
//        System.out.println(result1);
//        // cach 3
//        boolean matches = Pattern.matches("Jav.", "Java");
//        System.out.println(matches);


//        Pattern pattern = Pattern.compile("[abc]");
//        Matcher matcher = pattern.matcher("a");
//        boolean result = matcher.matches();
//        System.out.println(result);

//        Pattern pattern = Pattern.compile("[abc]{2}");
//        Matcher matcher = pattern.matcher("dea");
//        boolean result = matcher.matches();
//        System.out.println(result);

//        Pattern pattern = Pattern.compile("\\w+");
//        Matcher matcher = pattern.matcher("haonfksndkdksn293303");
//        boolean result = matcher.matches();
//        System.out.println(result);
//        String x = "3";

        // cach 1 simple dateformat
//        Date currentDate = new Date();
//        System.out.println("Current Date: " + currentDate);
//        String dateToStr = DateFormat.getInstance().format(currentDate);
//        System.out.println("DateFormat.getInstance(): " + dateToStr);
//        dateToStr = DateFormat.getDateInstance().format(currentDate);
//        System.out.println("DateFormat.getDateInstance(): " + dateToStr);
//        dateToStr = DateFormat.getTimeInstance().format(currentDate);
//        System.out.println("DateFormat.getTimeInstance(): " + dateToStr);
//        dateToStr = DateFormat.getDateTimeInstance().format(currentDate);
//        System.out.println("DateFormat.getDateTimeInstance(): " + dateToStr);
//        dateToStr = DateFormat.getTimeInstance(DateFormat.SHORT). format(currentDate);
//        System.out.println("DateFormat.getTimeInstance(DateFormat.SHORT): " + dateToStr);
//        dateToStr = DateFormat.getTimeInstance(DateFormat.MEDIUM).format(currentDate);
//        System.out.println("DateFormat.getTimeInstance(DateFormat.MEDIUM): " + dateToStr);
//        System.out.println("DateFormat.getTimeInstance(DateFormat.MEDIUM): " + dateToStr);
//        dateToStr = DateFormat.getTimeInstance(DateFormat.LONG). format(currentDate);
//        System.out.println("DateFormat.getTimeInstance(DateFormat.LONG): " + dateToStr);
//        dateToStr = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat. SHORT). format(currentDate);
//        System.out.println("DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat .SHORT): " + dateToStr);

    // cach 2 simple dateformat
//        Date date = new Date();
//
//        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
//        String strDate = formatter.format(date);
//        System.out.println("MM/dd/yyyy = " + strDate);
//
//        formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
//        strDate = formatter.format(date);
//        System.out.println("dd-M-yyyy hh:mm:ss = " + strDate);
//
//        formatter = new SimpleDateFormat("dd MMMM yyyy");
//        strDate = formatter.format(date);
//        System.out.println("dd MMMM yyyy = " + strDate);
//
//        formatter = new SimpleDateFormat("dd MMMM yyyy zzzz");
//        strDate = formatter.format(date);
//        System.out.println("dd MMMM yyyy zzzz = " + strDate);
//
//        formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");
//        strDate = formatter.format(date);
//        System.out.println("E, dd MMM yyyy HH:mm:ss z = " + strDate);

        //  convert local date sang String
        // Date format pattern for convert
        String DD_MM_YY = "ddMMyy";
        String YYYY_MM_DD = "yyyy-MM-dd";
        String DD_MM_YYYY = "dd/MM/yyyy";

        // Chọn một định dạng
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DD_MM_YYYY);

        // Lấy ngày hiện tại
        LocalDate localDate = LocalDate.now();

        // Chuyển ngày sang chuỗi theo định dạng
        String dateFormatted = localDate.format(formatter);

        System.out.println(dateFormatted);  // Ví dụ: 15/04/2025
   }
}
