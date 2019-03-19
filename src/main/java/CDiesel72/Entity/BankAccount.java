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
@Table(name = "BankAccounts")
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private Long money;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bankAccount", cascade = CascadeType.ALL)
    private List<Transact> transacts = new ArrayList<>();

    public BankAccount() {
    }


    public BankAccount(Client client, String currency) {
        this.client = client;
        this.currency = currency;
        this.money = 0L;
    }

    public Integer getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public String getCurrency() {
        return currency;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    public List<Transact> getTransacts() {
        return transacts;
    }

    public String info(boolean bTr, CurrencyDAO currDAO) {
        StringBuilder str = new StringBuilder();

        long moneyUAH = currDAO.convert(money, currency, "UAH");

        str.append("Номер счета: " + id + " владелец " + client.getName() + "; Доступные средства: " + Money.toString(money) + " "
                + currency + " = " + Money.toString(moneyUAH) + " UAH" + System.lineSeparator());

        if (bTr) {
            if (transacts.size() > 0) {
                str.append("Транзакции:" + System.lineSeparator());
                transacts.forEach(tr -> str.append("    " + tr.info()));
            } else {
                str.append("Нет транзакций" + System.lineSeparator());
            }
        }

        return str.toString();
    }
}
