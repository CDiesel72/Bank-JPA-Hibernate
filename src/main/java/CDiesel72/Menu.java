package CDiesel72;

import CDiesel72.Entity.Client;
import CDiesel72.Implementation.BankAccountDAO;
import CDiesel72.Implementation.ClientDAO;
import CDiesel72.Implementation.CurrencyDAO;
import CDiesel72.Implementation.TransactDAO;
import CDiesel72.Other.EnterType;

import javax.persistence.EntityManager;
import java.util.Scanner;

/**
 * Created by Diesel on 17.03.2019.
 */
public class Menu {

    private Scanner sc;
    private EntityManager em;
    private Client client;
    private CurrencyDAO currDAO;

    public Menu(EntityManager em, Scanner sc) {
        this.em = em;
        this.sc = sc;

        currDAO = new CurrencyDAO(em);
        currDAO.updateCurr();
    }

    public boolean menuClient() {
        EnterType et = new EnterType(sc);
        boolean eBl = true;
        ClientDAO clDAO = new ClientDAO(em, sc, currDAO);

        System.out.println("======");
        clDAO.selAllToScreen();

        while (eBl) {
            System.out.println("======");
            System.out.println("1: Войти");
            System.out.println("2: Зарегистрироваться");
            System.out.println("3: Список клиентов");
            System.out.println("9: Курсы валют");
            System.out.println("0: Выход из программы");
            System.out.print("-> ");

            int m = et.inInt();
            System.out.println("======");
            switch (m) {
                case 0:
                    client = null;
                    eBl = false;
                    break;
                case 1:
                    client = clDAO.checkIn();
                    eBl = client == null;
                    break;
                case 2:
                    client = clDAO.create();
                    eBl = client == null;
                    break;
                case 3:
                    clDAO.selAllToScreen();
                    System.out.println("======");
                    break;
                case 9:
                    currDAO.selAllToScreen();
                    break;
            }
        }

        return client != null;
    }

    public void menuAccount() {
        EnterType et = new EnterType(sc);
        boolean eBl = true;
        BankAccountDAO baDAO = new BankAccountDAO(em, sc, currDAO);
        TransactDAO trDAO = new TransactDAO(em);

        System.out.println("======");
        System.out.print("Вы вошли как " + client.info(true, currDAO));

        while (eBl) {
            System.out.println("======");
            System.out.println("1: Добавить новый счет");
            System.out.println("2: Пополнить счет");
            System.out.println("3: Снять деньги со счета");
            System.out.println("4: Перевести деньги со своего счета на другой счет");
            System.out.println("5: Информация о Вашем аккаунте");
            System.out.println("6: Информация о транзакциях по счету");
            System.out.println("7: Информация о транзакциях по всем счетам");
            System.out.println("9: Курсы валют");
            System.out.println("0: Выход");
            System.out.print("-> ");

            int m = et.inInt();
            System.out.println("======");
            switch (m) {
                case 0:
                    client = null;
                    eBl = false;
                    break;
                case 1:
                    baDAO.create(client);
                    break;
                case 2:
                    baDAO.inc(client);
                    break;
                case 3:
                    baDAO.dec(client);
                    break;
                case 4:
                    baDAO.transfer(client);
                    break;
                case 5:
                    System.out.print("Вы вошли как " + client.info(true, currDAO));
                    break;
                case 6:
                    baDAO.infoToScreen(client);
                    break;
                case 7:
                    trDAO.selAllToScreen();
                    break;
                case 9:
                    currDAO.selAllToScreen();
                    break;
            }
        }
    }
}
