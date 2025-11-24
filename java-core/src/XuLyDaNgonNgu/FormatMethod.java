package XuLyDaNgonNgu;



import java.util.Date;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class FormatMethod {
    public static String formatCurrency(double currency, Locale locale){
        NumberFormat localeFormat = NumberFormat.getCurrencyInstance(locale);
        String localeCurrency = localeFormat.format(currency);
        return localeCurrency;
    }
    public static String formatTime(Date time , Locale locale){
            DateFormat dateFormat = DateFormat.getTimeInstance(DateFormat.FULL, locale);
            return dateFormat.format(time);
    }
    public static String formatDate(Date date , Locale locale){
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL, locale);
        String datenow = dateFormat.format(date);
        return datenow;
    }




}