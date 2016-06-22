package digimatic.shangcommerce.daocontroller;

import android.content.Context;

import java.util.List;

import digimatic.shangcommerce.application.MyApplication;
import greendao.Country;
import greendao.CountryDao;

/**
 * Created by USER on 3/16/2016.
 */
public class CountryController {

    private static CountryDao getDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getCountryDao();
    }

    public static void insertOrUpdate(Context context, Country mCountry) {
        if (getDao(context).load(mCountry.getValue()) == null) {
            getDao(context).insert(mCountry);
        } else {
            getDao(context).update(mCountry);
        }
    }

    public static List<Country> getAll(Context context) {
        return getDao(context).loadAll();
    }

    public static boolean isLoaded(Context context) {
        List<Country> list = getDao(context).loadAll();
        if (list.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public static Country getCountryByCode(Context context, String code) {
        List<Country> list = getDao(context).queryRaw(" WHERE value = ?", code);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public static String getCountryLabel(Context context, String code) {
        List<Country> list = getDao(context).queryRaw(" WHERE value = ?", code);
        if (list.size() > 0) {
            return list.get(0).getLabel();
        } else {
            return code;
        }
    }

    public static void delete(Context context, Country mCountry) {
        getDao(context).delete(mCountry);
    }

    public static void clearAll(Context context) {
        getDao(context).deleteAll();
    }
}
