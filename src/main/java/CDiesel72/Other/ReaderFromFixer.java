package CDiesel72.Other;

import CDiesel72.Entity.Currency;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diesel on 17.03.2019.
 */
public class ReaderFromFixer {

    public List<Currency> read() {
        String sCurr = "UAH,USD,EUR";
        StringBuilder sURL = new StringBuilder(
                "http://data.fixer.io/api/latest?access_key=d745a695fcaff57a6757166177073ac4&symbols=");
        sURL.append(sCurr);
        String[] aCurr = sCurr.split("[,]");

        List<Currency> lCurr = new ArrayList<>();

        try {
            URL url = new URL(sURL.toString());
            HttpURLConnection http = (HttpURLConnection) url.openConnection();

            InputStream is = http.getInputStream();
            try {
                byte[] buf = requestBodyToArray(is);
                String strBuf = new String(buf, StandardCharsets.UTF_8);

                JsonObject fixer = (JsonObject) new JsonParser().parse(strBuf);
                boolean success = fixer.get("success").getAsBoolean();
                if (!success) {
                    System.out.println("ERROR: Ошибка чтения курса валют с FIXER");
                    return null;
                }
                JsonObject rates = fixer.get("rates").getAsJsonObject();
                for (String s : aCurr) {
                    double rate = rates.get(s).getAsDouble();
                    lCurr.add(new Currency(s, rate));
                }

            } finally {
                is.close();
            }
        } catch (Exception ex) {
            System.out.println("ERROR: Ошибка IO при чтения курса валют с FIXER");
            return null;
        }

        return lCurr;
    }

    private byte[] requestBodyToArray(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[10240];
        int r;

        do {
            r = is.read(buf);
            if (r > 0) bos.write(buf, 0, r);
        } while (r != -1);

        return bos.toByteArray();
    }
}
