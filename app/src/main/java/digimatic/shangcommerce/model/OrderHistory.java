package digimatic.shangcommerce.model;

/**
 * Created by USER on 3/15/2016.
 */
public class OrderHistory {
    private long entity_id;
    private String increment_id;
    private float subtotal;
    private float shipping_amount;
    private float discount_amount;
    private float tax_amount;
    private float grand_total;
    private String created_at;
    private String updated_at;
    private String status;
    private int email_sent;

    public OrderHistory(long entity_id, String increment_id, float subtotal, float shipping_amount, float discount_amount, float tax_amount, float grand_total, String created_at, String updated_at, String status, int email_sent) {
        this.entity_id = entity_id;
        this.increment_id = increment_id;
        this.subtotal = subtotal;
        this.shipping_amount = shipping_amount;
        this.discount_amount = discount_amount;
        this.tax_amount = tax_amount;
        this.grand_total = grand_total;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.status = status;
        this.email_sent = email_sent;
    }

    public long getEntity_id() {
        return entity_id;
    }

    public void setEntity_id(long entity_id) {
        this.entity_id = entity_id;
    }

    public String getIncrement_id() {
        return increment_id;
    }

    public void setIncrement_id(String increment_id) {
        this.increment_id = increment_id;
    }

    public float getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(float subtotal) {
        this.subtotal = subtotal;
    }

    public float getShipping_amount() {
        return shipping_amount;
    }

    public void setShipping_amount(float shipping_amount) {
        this.shipping_amount = shipping_amount;
    }

    public float getDiscount_amount() {
        return discount_amount;
    }

    public void setDiscount_amount(float discount_amount) {
        this.discount_amount = discount_amount;
    }

    public float getTax_amount() {
        return tax_amount;
    }

    public void setTax_amount(float tax_amount) {
        this.tax_amount = tax_amount;
    }

    public float getGrand_total() {
        return grand_total;
    }

    public void setGrand_total(float grand_total) {
        this.grand_total = grand_total;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getEmail_sent() {
        return email_sent;
    }

    public void setEmail_sent(int email_sent) {
        this.email_sent = email_sent;
    }
}
