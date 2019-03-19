package CDiesel72.Implementation;

import CDiesel72.Entity.Transact;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by Diesel on 09.03.2019.
 */
public class TransactDAO extends EntityDAO<Transact, Integer> {

    private final String QUERY = "SELECT с FROM Transact с";

    public TransactDAO(EntityManager em) {
        super(em);

        setQueryDAO(QUERY);
    }

    public void create(Transact tr) {
        tr.getBankAccount().getTransacts().add(tr);
        add(tr);
    }

    public void selAllToScreen() {
        List<Transact> ltr = selAll();
        System.out.println("Список транзакций:");
        if (ltr.size() > 0) {
            ltr.forEach(tr -> System.out.print(tr.info()));
        } else {
            System.out.println("Нет транзакций");
        }
    }
}