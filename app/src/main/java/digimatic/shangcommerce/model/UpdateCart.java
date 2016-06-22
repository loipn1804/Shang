package digimatic.shangcommerce.model;

/**
 * Created by USER on 3/15/2016.
 */
public class UpdateCart {

    public long item_id;
    public long customer_id;
    public long product_id;
    public int qty;

    public UpdateCart(long item_id, long customer_id, long product_id, int qty) {
        this.item_id = item_id;
        this.customer_id = customer_id;
        this.product_id = product_id;
        this.qty = qty;
    }

    public long getItem_id() {
        return item_id;
    }

    public void setItem_id(long item_id) {
        this.item_id = item_id;
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
}
