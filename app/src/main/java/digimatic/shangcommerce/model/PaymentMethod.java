package digimatic.shangcommerce.model;

/**
 * Created by user on 3/16/2016.
 */
public class PaymentMethod {

    public String code;
    public String label;
    public boolean isChosen;

    public PaymentMethod(String code, String label, boolean isChosen) {
        this.code = code;
        this.label = label;
        this.isChosen = isChosen;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isChosen() {
        return isChosen;
    }

    public void setIsChosen(boolean isChosen) {
        this.isChosen = isChosen;
    }
}
