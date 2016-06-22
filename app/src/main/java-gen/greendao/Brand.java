package greendao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table BRAND.
 */
public class Brand {

    private Long brand_id;
    private String name;
    private String short_description;
    private String image_url;
    private String thumbnail_url;

    public Brand() {
    }

    public Brand(Long brand_id) {
        this.brand_id = brand_id;
    }

    public Brand(Long brand_id, String name, String short_description, String image_url, String thumbnail_url) {
        this.brand_id = brand_id;
        this.name = name;
        this.short_description = short_description;
        this.image_url = image_url;
        this.thumbnail_url = thumbnail_url;
    }

    public Long getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(Long brand_id) {
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

}
