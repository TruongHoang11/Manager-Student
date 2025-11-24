package XuLyDaNgonNgu;

import java.text.NumberFormat;
import java.util.Locale;

public class Main3 {
    public static void main(String[] args) {
        Locale vnLocale = new Locale("vi", "VN");
        NumberFormat vnFormatter = NumberFormat.getCurrencyInstance(vnLocale);
        NumberFormat cnFormatter = NumberFormat.getCurrencyInstance(Locale.CHINA);
        NumberFormat usFormatter = NumberFormat.getCurrencyInstance(Locale.US);
        NumberFormat jpFormatter = NumberFormat.getCurrencyInstance(Locale.JAPAN);
        NumberFormat grFormatter = NumberFormat.getCurrencyInstance(Locale.GERMAN);

        double currency = 19393939d;
        System.out.println(vnFormatter.format(currency));
        System.out.println(cnFormatter.format(currency));
        System.out.println(usFormatter.format(currency));
        System.out.println(jpFormatter.format(currency));
        System.out.println(grFormatter.format(currency));


    }
}
