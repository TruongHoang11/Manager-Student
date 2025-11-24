package XuLyDaNgonNgu;

import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

import static XuLyDaNgonNgu.FormatMethod.*;

public class MainTestHam {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double money = 1000000;
        Locale vn = new Locale("vi", "VN");
        String vnCurrency = formatCurrency(money, vn);
        System.out.println(vnCurrency);
        System.out.printf("%s",formatCurrency(112345, Locale.US));
        System.out.println();
        Date time = new Date();
        System.out.println(formatTime((java.util.Date) time, vn));
        System.out.println(formatDate((java.util.Date) time, vn));
    }
}
