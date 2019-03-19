package CDiesel72.Other;

/**
 * Created by Diesel on 17.03.2019.
 */
public class Money {
    public static long doubleToMoney(double d) {
        return Math.round(d * 100);
    }

    public static String toString(long l) {
        return (l / 100) + "." + String.format("%02d", Math.abs(l % 100));
    }
}
