package XuLyDaNgonNgu;

import java.util.Locale;

import static java.util.Locale.*;

public class Main {
    public static void main(String[] args) {
        Locale default1 = getDefault();
        System.out.println("getCountry: " + default1.getCountry());
        System.out.println("getDisplayCountry: " + default1.getDisplayCountry());
        System.out.println("getDisplayCountry: " + default1.getDisplayCountry( new Locale("vi", "vn")));
        System.out.println("getDisplayLanguage: " + default1.getDisplayLanguage());
        System.out.println("getDisplayLanguage: " + default1.getDisplayLanguage(new Locale("vi", "vn")));
        System.out.println("getDisplayVariant: " + default1.getDisplayVariant());
        System.out.println("getISO3Country: " + default1.getISO3Country());
        System.out.println("getISO3Language: " + default1.getISO3Language());

    }
}
