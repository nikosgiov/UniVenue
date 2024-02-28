package models;
/**
 *
 * @author Nick Giovanopoulos
 */
public class Admin extends Person {

    private int admin_id;

    public Admin(String firstname, String lastname, String username, String password, String email,String gender, String birthDate, String telephone) {
        super(firstname, lastname, username, password, email,gender, birthDate, telephone);
    }

    public int getAdminId() {
        return admin_id;
    }

    public void setAdminId(int admin_id) {
        this.admin_id = admin_id;
    }
}