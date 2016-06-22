package digimatic.shangcommerce.model;

/**
 * Created by USER on 3/15/2016.
 */
public class ProductOptionItem {

    public long option_type_id;
    public String sku;
    public String title;
    public float price;
    public String price_type;

    public ProductOptionItem(long option_type_id, String sku, String title, float price, String price_type) {
        this.option_type_id = option_type_id;
        this.sku = sku;
        this.title = title;
        this.price = price;
        this.price_type = price_type;
    }

    public long getOption_type_id() {
        return option_type_id;
    }

    public void setOption_type_id(long option_type_id) {
        this.option_type_id = option_type_id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getPrice_type() {
        return price_type;
    }

    public void setPrice_type(String price_type) {
        this.price_type = price_type;
    }
}
