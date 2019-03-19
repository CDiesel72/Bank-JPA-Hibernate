package CDiesel72.Entity;

import CDiesel72.Other.Money;
import CDiesel72.Other.TypeTransact;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Diesel on 15.03.2019.
 */
@Entity
@Table(name = "Transacts")
public class Transact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Date date;

    @Column(nullable = false)
    private TypeTransact type;

    private Long delta;

    private Long money;

    @Column(name = "exp")
    private String explain;

    @ManyToOne
    @JoinColumn(name = "bankAccount_id")
    private BankAccount bankAccount;

    public Transact() {
    }

    public Transact(TypeTransact type, Long delta, String explain, BankAccount bankAccount) {
        this.date = new Date();
        this.type = type;
        this.delta = delta;
        this.money = bankAccount.getMoney();
        this.explain = explain;
        this.bankAccount = bankAccount;
    }

    public Integer getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public TypeTransact getType() {
        return type;
    }

    public Long getDelta() {
        return delta;
    }

    public Long getMoney() {
        return money;
    }

    public String getExplain() {
        return explain;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public String info() {
        String sdate = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(date);
        StringBuilder str = new StringBuilder(sdate + "; №" + id + "; счет №" + bankAccount.getId() + " владелец "
                + bankAccount.getClient().getName() + "; " + type.getType());

        if (type == TypeTransact.INC || type == TypeTransact.DEC || type == TypeTransact.ERROR) {
            str.append(" " + Money.toString(Math.abs(delta)) + " " + bankAccount.getCurrency());
        }
        str.append("; ");
        if (!explain.isEmpty()) {
            str.append(explain + "; ");
        }
        str.append("Доступные средства " + Money.toString(money) + " " + bankAccount.getCurrency()
                + System.lineSeparator());

        return str.toString();
    }
}
