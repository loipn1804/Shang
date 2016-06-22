package digimatic.shangcommerce.daocontroller;

import android.content.Context;

import java.util.List;

import digimatic.shangcommerce.application.MyApplication;
import greendao.CartDetail;
import greendao.CartDetailDao;

/**
 * Created by USER on 3/14/2016.
 */
public class CartDetailController {

    private static CartDetailDao getDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getCartDetailDao();
    }

    public static void insertOrUpdate(Context context, CartDetail mCartDetail) {
        if (getDao(context).load(mCartDetail.getEntity_id()) == null) {
            getDao(context).insert(mCartDetail);
        } else {
            getDao(context).update(mCartDetail);
        }
    }

    public static boolean isHaveCart(Context context) {
        List<CartDetail> list = getDao(context).loadAll();
        if (list.size() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public static CartDetail getCurrentCartDetail(Context context) {
        List<CartDetail> list = getDao(context).loadAll();
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public static void delete(Context context, CartDetail mCartDetail) {
        getDao(context).delete(mCartDetail);
    }

    public static void clearAll(Context context) {
        getDao(context).deleteAll();
    }
}
