package digimatic.shangcommerce.model;

import org.json.JSONObject;

/**
 * Created by USER on 3/14/2016.
 */
public class AddCart {

    public long customer_id;
    public long product_id;
    public int qty;
    public JSONObject options;
    public float price;

    public AddCart(long customer_id, long product_id, int qty, JSONObject options, float price) {
        this.customer_id = customer_id;
        this.product_id = product_id;
        this.qty = qty;
        this.options = options;
        this.price = price;
    }

    public long getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(long customer_id) {
        this.customer_id = customer_id;
    }

    public long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(long product_id) {
        this.product_id = product_id;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public JSONObject getOptions() {
        return options;
    }

    public void setOptions(JSONObject options) {
        this.options = options;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
