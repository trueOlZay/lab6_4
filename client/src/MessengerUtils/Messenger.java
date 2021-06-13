package MessengerUtils;

import java.util.Locale;
import java.util.ResourceBundle;

public class Messenger {
    public static Locale rus_l = new Locale("ru", "RU");

    public static void printMessage(String key) {
        System.out.println(ResourceBundle.getBundle("Messages", rus_l).getString(key));
    }

    public static void printMessage(String key, Object o) {
        System.out.println(ResourceBundle.getBundle("Messages", rus_l).getString(key) + o);
    }

    public static void printMessage(Object o) {
        System.out.println(o);
    }

    public static String getMessage(String key) {
        return ResourceBundle.getBundle("Messages", rus_l).getString(key);
    }
}
