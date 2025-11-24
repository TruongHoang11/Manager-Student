package XuLyDaNgonNgu;

import java.sql.Time;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class Main4 {
    public static void main(String[] args) {
        Locale vnLocale = new Locale("vi", "VN");
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, vnLocale);
        System.out.println(dateFormat.format(new Date()));
        DateFormat vnFormatTime = DateFormat.getTimeInstance(DateFormat.FULL, vnLocale);

        // Internationalizing Time
        Date timeformat = new Date();
        String datetime = vnFormatTime.format(timeformat);
        System.out.println(datetime);
        DateFormat caFormatTime = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.CANADA);
        DateFormat geFormatTime = DateFormat.getTimeInstance(DateFormat.MEDIUM, Locale.GERMAN);
        DateFormat usFormatTime = DateFormat.getTimeInstance(DateFormat.FULL, Locale.US);
        System.out.println(caFormatTime.format(new Date()));
        System.out.println(geFormatTime.format(new Date()));
        System.out.println(usFormatTime.format(new Date()));
    }
}
