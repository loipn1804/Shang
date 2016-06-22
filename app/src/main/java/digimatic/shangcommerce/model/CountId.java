package digimatic.shangcommerce.model;

/**
 * Created by USER on 3/21/2016.
 */
public class CountId {

    public long id;
    public int count;

    public CountId(long id, int count) {
        this.id = id;
        this.count = count;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
