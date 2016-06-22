package digimatic.shangcommerce.daocontroller;

import android.content.Context;

import java.util.List;

import digimatic.shangcommerce.application.MyApplication;
import greendao.CartItem;
import greendao.CartItemDao;

/**
 * Created by USER on 3/14/2016.
 */
public class CartItemController {

    private static CartItemDao getDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getCartItemDao();
    }

    public static void insertOrUpdate(Context context, CartItem mCartItem) {
        if (getDao(context).load(mCartItem.getItem_id()) == null) {
            getDao(context).insert(mCartItem);
        } else {
            getDao(context).update(mCartItem);
        }
    }

    public static List<CartItem> getAll(Context context) {
        return getDao(context).loadAll();
    }

    public static void delete(Context context, CartItem mCartItem) {
        getDao(context).delete(mCartItem);
    }

    public static void clearAll(Context context) {
        getDao(context).deleteAll();
    }
}
