package digimatic.shangcommerce.callback;

/**
 * Created by USER on 3/7/2016.
 */
public interface CartItemCallback {
    void subQuantity(int position);

    void addQuantity(int position);

    void updateQuantity(int quantity, int position);
}
