package models;
/**
 *
 * @author Nick Giovanopoulos
 */
public class Costumer extends Person {
    private int costumerId;
    private int money;

    public Costumer(String firstname, String lastname, String username, String password, String email, String gender, String birthDate, String telephone, int money) {
        super(firstname, lastname, username, password, email,gender, birthDate, telephone);
        this.money = money;
    }

    public int getCostumerId() {
        return costumerId;
    }

    public void setCostumerId(int costumerId) {
        this.costumerId = costumerId;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}