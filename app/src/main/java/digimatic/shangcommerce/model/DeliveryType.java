package digimatic.shangcommerce.model;

/**
 * Created by user on 3/16/2016.
 */
public class DeliveryType {

    public String title;
    public String code;
    public float price;
    public String logo;
    public boolean isChosen;

    public DeliveryType(String title, String code, float price, String logo, boolean isChosen) {
        this.title = title;
        this.code = code;
        this.price = price;
        this.logo = logo;
        this.isChosen = isChosen;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public boolean isChosen() {
        return isChosen;
    }

    public void setIsChosen(boolean isChosen) {
        this.isChosen = isChosen;
    }
}
