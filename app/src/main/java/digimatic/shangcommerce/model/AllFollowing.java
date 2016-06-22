package digimatic.shangcommerce.model;

/**
 * Created by USER on 04/11/2016.
 */
public class AllFollowing {

    boolean isCategory;

    private Long entity_id;
    private String name_category;
    private Integer position;
    private Integer product_count;

    private Long brand_id;
    private String name_brand;
    private String short_description;

    private String image_url;
    private String thumbnail_url;

    boolean isFollow;
    private long id;
    private String type;
    private long type_id;

    public AllFollowing() {

    }

    public AllFollowing(boolean isCategory, Long entity_id, String name_category, Integer position, Integer product_count, Long brand_id, String name_brand, String short_description, String image_url, String thumbnail_url, boolean isFollow, long id, String type, long type_id) {
        this.isCategory = isCategory;
        this.entity_id = entity_id;
        this.name_category = name_category;
        this.position = position;
        this.product_count = product_count;
        this.brand_id = brand_id;
        this.name_brand = name_brand;
        this.short_description = short_description;
        this.image_url = image_url;
        this.thumbnail_url = thumbnail_url;
        this.isFollow = isFollow;
        this.id = id;
        this.type = type;
        this.type_id = type_id;
    }

    public boolean isCategory() {
        return isCategory;
    }

    public void setIsCategory(boolean isCategory) {
        this.isCategory = isCategory;
    }

    public Long getEntity_id() {
        return entity_id;
    }

    public void setEntity_id(Long entity_id) {
        this.entity_id = entity_id;
    }

    public String getName_category() {
        return name_category;
    }

    public void setName_category(String name_category) {
        this.name_category = name_category;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getProduct_count() {
        return product_count;
    }

    public void setProduct_count(Integer product_count) {
        this.product_count = product_count;
    }

    public Long getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(Long brand_id) {
        this.brand_id = brand_id;
    }

    public String getName_brand() {
        return name_brand;
    }

    public void setName_brand(String name_brand) {
        this.name_brand = name_brand;
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

    public boolean isFollow() {
        return isFollow;
    }

    public void setIsFollow(boolean isFollow) {
        this.isFollow = isFollow;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getType_id() {
        return type_id;
    }

    public void setType_id(long type_id) {
        this.type_id = type_id;
    }
}
