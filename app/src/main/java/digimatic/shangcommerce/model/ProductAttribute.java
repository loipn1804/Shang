package digimatic.shangcommerce.model;

import java.util.List;

/**
 * Created by USER on 3/21/2016.
 */
public class ProductAttribute {

    public long id;
    public String code;
    public String label;
    private List<ProductAttributeItem> productAttributeItems;
    private boolean isChosen;

    public ProductAttribute(long id, String code, String label, List<ProductAttributeItem> productAttributeItems, boolean isChosen) {
        this.id = id;
        this.code = code;
        this.label = label;
        this.productAttributeItems = productAttributeItems;
        this.isChosen = isChosen;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public List<ProductAttributeItem> getProductAttributeItems() {
        return productAttributeItems;
    }

    public void setProductAttributeItems(List<ProductAttributeItem> productAttributeItems) {
        this.productAttributeItems = productAttributeItems;
    }

    public boolean isChosen() {
        return isChosen;
    }

    public void setIsChosen(boolean isChosen) {
        this.isChosen = isChosen;
    }
}
