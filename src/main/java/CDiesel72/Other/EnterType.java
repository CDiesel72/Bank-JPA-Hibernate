package CDiesel72.Other;

import java.util.Scanner;

/**
 * Created by Diesel on 17.03.2019.
 */
public class EnterType {
    private Scanner sc;

    public EnterType(Scanner sc) {
        this.sc = sc;
    }

    public String inString() {
        String st = null;
        while ((st == null) || (st.isEmpty())) {
            st = sc.nextLine();
        }
        return st;
    }

    public int inInt() {
        int i = 0;
        boolean flag = true;
        while (flag) {
            try {
                String st = inString();
                i = Integer.valueOf(st);
                flag = false;
            } catch (Exception ex) {
            }
        }

        return i;
    }

    public long inLong() {
        long l = 0;
        boolean flag = true;
        while (flag) {
            try {
                String st = inString();
                l = Long.valueOf(st);
                flag = false;
            } catch (Exception ex) {
            }
        }

        return l;
    }

    public double inDouble() {
        double d = 0;
        boolean flag = true;
        while (flag) {
            try {
                String st = inString();
                d = Double.valueOf(st);
                flag = false;
            } catch (Exception ex) {
            }
        }

        return d;
    }

    public boolean inBool() {
        boolean bl = false;
        boolean flag = true;
        while (flag) {
            String st = inString();
            if ("y".equalsIgnoreCase(st)) {
                bl = true;
                flag = false;
            } else if ("n".equalsIgnoreCase(st)) {
                bl = false;
                flag = false;
            }
        }

        return bl;
    }
}
