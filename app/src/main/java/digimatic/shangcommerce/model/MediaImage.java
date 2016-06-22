package digimatic.shangcommerce.model;

import java.util.List;

/**
 * Created by USER on 3/21/2016.
 */
public class MediaImage {

    private long product_id;
    private String base_image;
    private List<String> listImage;

    public MediaImage(long product_id, String base_image, List<String> listImage) {
        this.product_id = product_id;
        this.base_image = base_image;
        this.listImage = listImage;
    }

    public long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(long product_id) {
        this.product_id = product_id;
    }

    public String getBase_image() {
        return base_image;
    }

    public void setBase_image(String base_image) {
        this.base_image = base_image;
    }

    public List<String> getListImage() {
        return listImage;
    }

    public void setListImage(List<String> listImage) {
        this.listImage = listImage;
    }
}
