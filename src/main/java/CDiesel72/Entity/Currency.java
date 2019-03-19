package CDiesel72.Entity;

import CDiesel72.Other.Money;

import javax.persistence.*;

/**
 * Created by Diesel on 15.03.2019.
 */
@Entity
@Table(name = "Currencies")
public class Currency {
    @Id
    private String id;

    @Column(nullable = false)
    private Double rate;

    public Currency() {
    }

    public Currency(String id, Double rate) {
        this.id = id;
        this.rate = rate;
    }

    public String getId() {
        return id;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String info() {
        return id + " = " + Money.toString(Money.doubleToMoney(rate));
    }
}
