package digimatic.shangcommerce.model;

/**
 * Created by USER on 3/16/2016.
 */
public class ChosenOption {

    public long option_id;
    public long option_type_id;
    public boolean isChosen;
    public boolean isRequire;
    public String label_option;

    public ChosenOption(long option_id, long option_type_id, boolean isChosen, boolean isRequire, String label_option) {
        this.option_id = option_id;
        this.option_type_id = option_type_id;
        this.isChosen = isChosen;
        this.isRequire = isRequire;
        this.label_option = label_option;
    }

    public long getOption_id() {
        return option_id;
    }

    public void setOption_id(long option_id) {
        this.option_id = option_id;
    }

    public long getOption_type_id() {
        return option_type_id;
    }

    public void setOption_type_id(long option_type_id) {
        this.option_type_id = option_type_id;
    }

    public boolean isChosen() {
        return isChosen;
    }

    public void setIsChosen(boolean isChosen) {
        this.isChosen = isChosen;
    }

    public boolean isRequire() {
        return isRequire;
    }

    public void setIsRequire(boolean isRequire) {
        this.isRequire = isRequire;
    }

    public String getLabel_option() {
        return label_option;
    }

    public void setLabel_option(String label_option) {
        this.label_option = label_option;
    }
}
