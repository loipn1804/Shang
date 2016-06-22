package digimatic.shangcommerce.model;

/**
 * Created by USER on 04/11/2016.
 */
public class Following {

    private long id;
    private String type;
    private long type_id;
    private boolean isCategory;

    public Following(long id, String type, long type_id, boolean isCategory) {
        this.id = id;
        this.type = type;
        this.type_id = type_id;
        this.isCategory = isCategory;
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

    public boolean isCategory() {
        return isCategory;
    }

    public void setIsCategory(boolean isCategory) {
        this.isCategory = isCategory;
    }
}
