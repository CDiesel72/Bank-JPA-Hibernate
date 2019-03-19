package CDiesel72.Other;

/**
 * Created by Diesel on 15.03.2019.
 */
public enum TypeTransact {

    CREATE("Открыт"), CLOSE("Закрыт"), INC("Пополнен на"), DEC("Снято"),
    ERROR("Недостаточно стредств на счету для снятия");

    private String type;

    private TypeTransact(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
