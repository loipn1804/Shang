package digimatic.shangcommerce.model;

/**
 * Created by USER on 3/12/2016.
 */
public class ChangePass {

    public String password;
    public String new_password;

    public ChangePass(String password, String new_password) {
        this.password = password;
        this.new_password = new_password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNew_password() {
        return new_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }
}
