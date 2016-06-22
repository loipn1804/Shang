package digimatic.shangcommerce.model;

import java.util.List;

/**
 * Created by USER on 3/21/2016.
 */
public class ProductAttributeItem {

    private long id;
    private String label;
    private float price;
    private List<Long> products;
    private boolean isChosen;

    public ProductAttributeItem(long id, String label, float price, List<Long> products, boolean isChosen) {
        this.id = id;
        this.label = label;
        this.price = price;
        this.products = products;
        this.isChosen = isChosen;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public List<Long> getProducts() {
        return products;
    }

    public void setProducts(List<Long> products) {
        this.products = products;
    }

    public boolean isChosen() {
        return isChosen;
    }

    public void setIsChosen(boolean isChosen) {
        this.isChosen = isChosen;
    }
}
