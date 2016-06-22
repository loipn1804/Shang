package digimatic.shangcommerce.model;

/**
 * Created by USER on 4/5/2016.
 */
public class Category {

    private long entity_id;
    private String name;
    private String image_url;
    private String thumbnail_url;
    private boolean isChosen;
    private long id;

    public Category(long entity_id, String name, String image_url, String thumbnail_url, boolean isChosen, long id) {
        this.entity_id = entity_id;
        this.name = name;
        this.image_url = image_url;
        this.thumbnail_url = thumbnail_url;
        this.isChosen = isChosen;
        this.id = id;
    }

    public long getEntity_id() {
        return entity_id;
    }

    public void setEntity_id(long entity_id) {
        this.entity_id = entity_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
