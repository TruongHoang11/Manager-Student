package XuLyDaNgonNgu;

import java.util.Locale;

public class Main2 {
    public static void main(String[] args) {

        Locale defaultLocale =  Locale.getDefault();
        Locale chinaLocale = Locale.CHINA;
        Locale japanLocale = Locale.JAPAN;
        Locale vnLocale = new Locale("vi", "VN");
        System.out.println("defaultLocale.getDisplayLanguage(): " + defaultLocale.getDisplayLanguage());
        System.out.println("vnLocale.getDisplayLanguage(): " + vnLocale.getDisplayLanguage()); // print VietNamese
        // chuyển từ tiêng anh sang tiếng việt (chuyển từ ngông ngữ này sang ngôn ngữ khác)
        System.out.println("vnLocale.getDisplayLanguage(): " + vnLocale.getDisplayLanguage(vnLocale)); // print Tiếng Việt
        System.out.println("chinaLocale.getDisplayLanguage() " + chinaLocale.getDisplayLanguage());
        System.out.println("chinaLocale.getDisplayLanguage() " + chinaLocale.getDisplayLanguage(vnLocale));

        // hieenr thị nhiều ngôn ngữ
        Locale[] locales = { new Locale("en","GB"), new Locale("fr", "FR"),
                new Locale("es", "ES")};
        for(int i = 0 ; i < locales.length; i++){
            String displayLanguage = locales[i].getDisplayLanguage(locales[i]);
            System.out.println(locales[i] + ": " + displayLanguage );
        }

    }
}
