package digimatic.shangcommerce.daocontroller;

import android.content.Context;

import java.util.List;

import digimatic.shangcommerce.application.MyApplication;
import greendao.Brand;
import greendao.BrandDao;

/**
 * Created by USER on 04/11/2016.
 */
public class BrandController {

    private static BrandDao getDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getBrandDao();
    }

    public static void insertOrUpdate(Context context, Brand mBrand) {
        if (getDao(context).load(mBrand.getBrand_id()) == null) {
            getDao(context).insert(mBrand);
        } else {
            getDao(context).update(mBrand);
        }
    }

    public static List<Brand> getAll(Context context) {
        return getDao(context).loadAll();
    }

    public static void delete(Context context, Brand mBrand) {
        getDao(context).delete(mBrand);
    }

    public static void clearAll(Context context) {
        getDao(context).deleteAll();
    }
}
