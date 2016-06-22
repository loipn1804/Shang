package digimatic.shangcommerce.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 3/15/2016.
 */
public class ProductOption {

    public long option_id;
    public String title;
    public String type;
    public int is_require;
    public List<ProductOptionItem> listProductOptionItem;

    public ProductOption(long option_id, String title, String type, int is_require, List<ProductOptionItem> listProductOptionItem) {
        this.option_id = option_id;
        this.title = title;
        this.type = type;
        this.is_require = is_require;
        this.listProductOptionItem = listProductOptionItem;
    }

    public long getOption_id() {
        return option_id;
    }

    public void setOption_id(long option_id) {
        this.option_id = option_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIs_require() {
        return is_require;
    }

    public void setIs_require(int is_require) {
        this.is_require = is_require;
    }

    public List<ProductOptionItem> getListProductOptionItem() {
        return listProductOptionItem;
    }

    public void setListProductOptionItem(List<ProductOptionItem> listProductOptionItem) {
        this.listProductOptionItem = listProductOptionItem;
    }
}
