package digimatic.shangcommerce.daocontroller;

import android.content.Context;

import java.util.List;

import digimatic.shangcommerce.application.MyApplication;
import greendao.Wishlist;
import greendao.WishlistDao;

/**
 * Created by USER on 3/14/2016.
 */
public class WishlistController {

    private static WishlistDao getDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getWishlistDao();
    }

    public static void insertOrUpdate(Context context, Wishlist mWishlist) {
        if (getDao(context).load(mWishlist.getWishlist_item_id()) == null) {
            getDao(context).insert(mWishlist);
        } else {
            getDao(context).update(mWishlist);
        }
    }

    public static Wishlist getByProductId(Context context, long product_id) {
        List<Wishlist> list = getDao(context).queryRaw(" WHERE product_id = ?", product_id + "");
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public static List<Wishlist> getAll(Context context) {
        return getDao(context).loadAll();
    }

    public static void delete(Context context, Wishlist mWishlist) {
        getDao(context).delete(mWishlist);
    }

    public static void deleteById(Context context, long wishlist_item_id) {
        getDao(context).deleteByKey(wishlist_item_id);
    }

    public static void clearAll(Context context) {
        getDao(context).deleteAll();
    }
}
