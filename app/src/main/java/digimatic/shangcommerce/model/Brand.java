package digimatic.shangcommerce.model;

/**
 * Created by USER on 4/5/2016.
 */
public class Brand {

    private long brand_id;
    private String name;
    private String short_description;
    private String image_url;
    private String thumbnail_url;
    private boolean isChosen;
    private long id;

    public Brand(long brand_id, String name, String short_description, String image_url, String thumbnail_url, boolean isChosen, long id) {
        this.brand_id = brand_id;
        this.name = name;
        this.short_description = short_description;
        this.image_url = image_url;
        this.thumbnail_url = thumbnail_url;
        this.isChosen = isChosen;
        this.id = id;
    }

    public long getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(long brand_id) {
        this.brand_id = brand_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }

    public boolean isChosen() {
        return isChosen;
    }

    public void setIsChosen(boolean isChosen) {
        this.isChosen = isChosen;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
