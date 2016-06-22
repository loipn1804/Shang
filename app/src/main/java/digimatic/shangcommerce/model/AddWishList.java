package digimatic.shangcommerce.model;

/**
 * Created by USER on 3/14/2016.
 */
public class AddWishList {

    public long customer_id;
    public long product_id;

    public AddWishList(long customer_id, long product_id) {
        this.customer_id = customer_id;
        this.product_id = product_id;
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
}
