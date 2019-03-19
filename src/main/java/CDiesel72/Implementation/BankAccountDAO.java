package CDiesel72.Implementation;

import CDiesel72.Entity.BankAccount;
import CDiesel72.Entity.Client;
import CDiesel72.Entity.Transact;
import CDiesel72.Other.EnterType;
import CDiesel72.Other.Money;
import CDiesel72.Other.TypeTransact;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by Diesel on 09.03.2019.
 */
public class BankAccountDAO extends EntityDAO<BankAccount, Integer> {

    private final String QUERY = "SELECT с FROM BankAccount с";
    private Scanner sc;
    private CurrencyDAO currDAO;

    public BankAccountDAO(EntityManager em, Scanner sc, CurrencyDAO currDAO) {
        super(em);
        this.sc = sc;
        this.currDAO = currDAO;

        setQueryDAO(QUERY);
    }

    public void create(Client cl) {
        EnterType et = new EnterType(sc);

        System.out.println("Доступные валюты, в которых можно открыть счет:");
        Set<String> currSet = currDAO.getCurrMap().keySet();
        currSet.forEach(s -> System.out.print(s + "   "));
        System.out.println();

        System.out.println("Введите название валюты:");
        String curr = et.inString();

        if (currDAO.checkCurr(curr)) {
            BankAccount ba = new BankAccount(cl, curr);
            cl.getBankAccounts().add(ba);
            add(ba);

            Transact tr = new Transact(TypeTransact.CREATE, 0L, "", ba);
            new TransactDAO(getEm()).create(tr);

            System.out.println("Создан счет");
            System.out.println(ba.info(false, currDAO));
        } else {
            System.out.println("ERROR: Нет валюты " + curr + ". Счет не создан.");
        }
    }

    private BankAccount checkEnter(Client cl) {
        EnterType et = new EnterType(sc);
        System.out.println("Введите номер счета:");
        int id = et.inInt();

        BankAccount ba = selId(id);
        if (ba == null) {
            System.out.println("Нет счета с номером " + id);
            return null;
        }

        if (ba.getClient().getId() != cl.getId()) {
            System.out.println("Счет с номером " + ba.getId() + " не Ваш. Вы не можете иметь к нему доступ");
            return null;
        }

        return ba;
    }

    public void inc(Client cl) {
        BankAccount ba = checkEnter(cl);
        if (ba != null) {
            EnterType et = new EnterType(sc);
            System.out.println("Введите сумму в " + ba.getCurrency() + ", на которую Вы хотите пополнить счет:");
            double delta = Math.abs(et.inDouble());
            long deltaL = Money.doubleToMoney(delta);
            ba.setMoney(ba.getMoney() + deltaL);

            Transact tr = new Transact(TypeTransact.INC, deltaL, "", ba);
            new TransactDAO(getEm()).create(tr);

            System.out.println("Счет №" + ba.getId() + " пополнен на " + Money.toString(deltaL) + " " + ba.getCurrency());
            System.out.println(ba.info(false, currDAO));
        }
    }

    public void dec(Client cl) {
        BankAccount ba = checkEnter(cl);
        if (ba != null) {
            EnterType et = new EnterType(sc);
            System.out.println("Введите сумму в " + ba.getCurrency() + ", которую Вы хотите снять со счета:");
            double delta = Math.abs(et.inDouble());
            long deltaL = Money.doubleToMoney(delta);
            if (ba.getMoney() - deltaL >= 0) {
                ba.setMoney(ba.getMoney() - deltaL);

                Transact tr = new Transact(TypeTransact.DEC, deltaL, "", ba);
                new TransactDAO(getEm()).create(tr);

                System.out.println("Со счета №" + ba.getId() + " снято " + Money.toString(deltaL) + " " + ba.getCurrency());
                System.out.println(ba.info(false, currDAO));
            } else {
                Transact tr = new Transact(TypeTransact.ERROR, deltaL, "", ba);
                new TransactDAO(getEm()).create(tr);

                System.out.println("Не возможно снять со счета №" + ba.getId() + " " + Money.toString(deltaL)
                        + " " + ba.getCurrency() + ". Не достаточно средств на счете");
            }
        }
    }

    public void transfer(Client cl) {
        BankAccount baOut = checkEnter(cl);
        if (baOut != null) {
            EnterType et = new EnterType(sc);
            System.out.println("Введите номер счета, на который Вы хотите перевести средства:");
            int id = et.inInt();

            BankAccount baIn = selId(id);
            if (baIn == null) {
                System.out.println("Нет счета с номером " + id);
                return;
            }

            System.out.println("Введите сумму в " + baOut.getCurrency() + ", которую Вы хотите перевести:");
            double delta = Math.abs(et.inDouble());
            long deltaLOut = Money.doubleToMoney(delta);
            if (baOut.getMoney() - deltaLOut < 0) {
                Transact tr = new Transact(TypeTransact.ERROR, deltaLOut, "", baOut);
                new TransactDAO(getEm()).create(tr);

                System.out.println("Не возможно перевести со счета №" + baOut.getId() + " " + Money.toString(deltaLOut)
                        + " " + baOut.getCurrency() + " на другой счет. Не достаточно средств на счете");
                return;
            }

            baOut.setMoney(baOut.getMoney() - deltaLOut);

            Transact trOut = new Transact(TypeTransact.DEC, deltaLOut, "перевод на счет №" + baIn.getId()
                    + " владелец " + baIn.getClient().getName(), baOut);
            new TransactDAO(getEm()).create(trOut);

            long deltaLIn = currDAO.convert(deltaLOut, baOut.getCurrency(), baIn.getCurrency());
            baIn.setMoney(baIn.getMoney() + deltaLIn);

            Transact trIn = new Transact(TypeTransact.INC, deltaLIn, "перевод со счета №" + baOut.getId()
                    + " владелец " + baOut.getClient().getName(), baIn);
            new TransactDAO(getEm()).create(trIn);

            System.out.println("Перевод со счета №" + baOut.getId() + " " + Money.toString(deltaLOut) + " " + baOut.getCurrency()
                    + " на счет №" + baIn.getId() + " владелец " + baIn.getClient().getName());
            System.out.println(baOut.info(false, currDAO));
        }
    }

    public void infoToScreen(Client cl) {
        BankAccount ba = checkEnter(cl);
        if (ba != null) {
            System.out.println(ba.info(true, currDAO));
        }
    }

    public void selAllToScreen() {
        List<BankAccount> lba = selAll();
        System.out.println("Список банковских счетов:");
        if (lba.size() > 0) {
            lba.forEach(ba -> System.out.print(ba.info(false, currDAO)));
        } else {
            System.out.println("Нет зарегистрированных счетов");
        }
    }
}
