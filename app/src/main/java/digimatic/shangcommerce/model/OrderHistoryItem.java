package digimatic.shangcommerce.model;

/**
 * Created by USER on 3/15/2016.
 */
public class OrderHistoryItem {

    private long item_id;
    private long product_id;
    private String type_id;
    private String name;
    private String sku;
    private String manufacturer;
    private float price;
    private String image_url;
    private int qty_ordered;
    private float row_total;
    private String created_at;
    private String options;

    public OrderHistoryItem(long item_id, long product_id, String type_id, String name, String sku, String manufacturer, float price, String image_url, int qty_ordered, float row_total, String created_at, String options) {
        this.item_id = item_id;
        this.product_id = product_id;
        this.type_id = type_id;
        this.name = name;
        this.sku = sku;
        this.manufacturer = manufacturer;
        this.price = price;
        this.image_url = image_url;
        this.qty_ordered = qty_ordered;
        this.row_total = row_total;
        this.created_at = created_at;
        this.options = options;
    }

    public long getItem_id() {
        return item_id;
    }

    public void setItem_id(long item_id) {
        this.item_id = item_id;
    }

    public long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(long product_id) {
        this.product_id = product_id;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public int getQty_ordered() {
        return qty_ordered;
    }

    public void setQty_ordered(int qty_ordered) {
        this.qty_ordered = qty_ordered;
    }

    public float getRow_total() {
        return row_total;
    }

    public void setRow_total(float row_total) {
        this.row_total = row_total;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }
}
