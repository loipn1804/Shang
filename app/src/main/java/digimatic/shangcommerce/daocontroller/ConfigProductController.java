package digimatic.shangcommerce.daocontroller;

import android.content.Context;

import java.util.List;

import digimatic.shangcommerce.application.MyApplication;
import greendao.ConfigProduct;
import greendao.ConfigProductDao;

/**
 * Created by user on 3/21/2016.
 */
public class ConfigProductController {

    private static ConfigProductDao getDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getConfigProductDao();
    }

    public static void insertOrUpdate(Context context, ConfigProduct mConfigProduct) {
        if (getDao(context).load(mConfigProduct.getProduct_id()) == null) {
            getDao(context).insert(mConfigProduct);
        } else {
            getDao(context).update(mConfigProduct);
        }
    }

    public static ConfigProduct getById(Context context, long product_id) {
        List<ConfigProduct> optionList = getDao(context).queryRaw(" WHERE product_id = ? ORDER BY position ASC", product_id + "");
        if (optionList.size() > 0) {
            return optionList.get(0);
        } else {
            return null;
        }
    }

    public static List<ConfigProduct> getAll(Context context) {
        return getDao(context).loadAll();
    }

    public static void clearAll(Context context) {
        getDao(context).deleteAll();
    }
}
