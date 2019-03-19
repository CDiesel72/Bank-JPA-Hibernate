package CDiesel72.Implementation;

import CDiesel72.Entity.Currency;
import CDiesel72.Other.Money;
import CDiesel72.Other.ReaderFromFixer;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Diesel on 17.03.2019.
 */
public class CurrencyDAO extends EntityDAO<Currency, String> {

    private final String QUERY = "SELECT с FROM Currency с";
    private Map<String, Double> currMap;

    public CurrencyDAO(EntityManager em) {
        super(em);
        currMap = null;

        setQueryDAO(QUERY);
    }

    public Map<String, Double> getCurrMap() {
        return currMap;
    }

    public void updateCurr() {
        ReaderFromFixer rff = new ReaderFromFixer();
        List<Currency> listCurr = rff.read();
        if (listCurr != null) {
            delAll();
            addList(listCurr);
        } else {
            System.out.println("Применяю последние сохраненные курсы валют");
        }

        currMap = new HashMap<String, Double>();
        for (Currency c : listCurr) {
            currMap.put(c.getId(), c.getRate());
        }
    }

    public boolean checkCurr(String curr) {
        return currMap.containsKey(curr);
    }

    public long convert(long v, String sCurrFrom, String sCurrTo) {
        double rForm = currMap.get(sCurrFrom);
        double rTo = currMap.get(sCurrTo);
        long r = Money.doubleToMoney(rTo / rForm);
        return (v * r) / 100;
    }

    public void selAllToScreen() {
        Set<String> currSet = currMap.keySet();

        System.out.println("Текущие курсы валют:");
        if (currSet.size() > 0) {
            for (String s : currSet) {
                System.out.println(new Currency(s, currMap.get(s)).info());
            }
        } else {
            System.out.println("Нет валют для отображения");
        }
    }

}
