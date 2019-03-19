package CDiesel72.Entity;

import CDiesel72.Implementation.CurrencyDAO;
import CDiesel72.Other.Money;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diesel on 15.03.2019.
 */
@Entity
@Table(name = "Clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    private String pass;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "client", cascade = CascadeType.ALL)
    private List<BankAccount> bankAccounts = new ArrayList<>();

    public Client() {
    }

    public Client(String name, String pass) {
        this.name = name;
        this.pass = pass;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public List<BankAccount> getBankAccounts() {
        return bankAccounts;
    }

    public String info(boolean bBA, CurrencyDAO currDAO) {
        StringBuilder str = new StringBuilder();
        str.append(name + "; id: " + id + System.lineSeparator());

        if (bBA) {
            if (bankAccounts.size() > 0) {
                str.append("Счета:" + System.lineSeparator());
                long summ = 0;
                for (BankAccount ba : bankAccounts) {
                    str.append("    " + ba.info(false, currDAO));
                    summ += currDAO.convert(ba.getMoney(), ba.getCurrency(), "UAH");
                }
                str.append("    ------" + System.lineSeparator());
                str.append("    Доступные средства суммарно по всем счетам " + Money.toString(summ) + " UAH"
                        + System.lineSeparator());
            } else {
                str.append("Нет счетов" + System.lineSeparator());
            }
        }

        return str.toString();
    }
}
