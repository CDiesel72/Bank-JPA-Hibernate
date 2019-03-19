package CDiesel72.Implementation;

import CDiesel72.Entity.Client;
import CDiesel72.Other.EnterType;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Diesel on 09.03.2019.
 */
public class ClientDAO extends EntityDAO<Client, Integer> {

    private final String QUERY = "SELECT с FROM Client с";
    private Scanner sc;
    private CurrencyDAO currDAO;

    public ClientDAO(EntityManager em, Scanner sc, CurrencyDAO currDAO) {
        super(em);
        this.sc = sc;
        this.currDAO = currDAO;

        setQueryDAO(QUERY);
    }

    public Client create() {
        EnterType et = new EnterType(sc);
        System.out.println("Введите данные о себе:");
        System.out.print("имя: ");
        String name = et.inString();
        System.out.print("пароль: ");
        String pass = et.inString();
        System.out.print("повторите пароль: ");
        String passRep = et.inString();

        Client cl = null;

        if (pass.equals(passRep)) {
            cl = new Client(name, pass);
            add(cl);
        } else {
            System.out.println("ERROR: Пароли не совпадают");
        }

        return cl;
    }

    public Client checkIn() {
        EnterType et = new EnterType(sc);
        System.out.println("Введите свой ID:");
        int id = et.inInt();

        Client cl = selId(id);

        if (cl != null) {
            System.out.println("Введите пароль:");
            String pass = et.inString();
            if (!pass.equals(cl.getPass())) {
                System.out.println("ERROR: Введен не правильный пароль");
                cl = null;
            }
        } else {
            System.out.println("Клиент с ID " + id + " не найден");
        }

        return cl;
    }

    public void selAllToScreen() {
        List<Client> lcl = selAll();
        System.out.println("Список клиентов банка:");
        if (lcl.size() > 0) {
            lcl.forEach(cl -> System.out.print(cl.info(false, currDAO)));
        } else {
            System.out.println("Нет зарегистрированных клиентов");
        }
    }
}
