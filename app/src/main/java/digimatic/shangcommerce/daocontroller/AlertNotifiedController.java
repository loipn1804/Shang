package digimatic.shangcommerce.daocontroller;

import android.content.Context;

import java.util.List;

import digimatic.shangcommerce.application.MyApplication;
import greendao.AlertNotified;
import greendao.AlertNotifiedDao;

/**
 * Created by USER on 3/26/2016.
 */
public class AlertNotifiedController {

    private static AlertNotifiedDao getDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getAlertNotifiedDao();
    }

    public static void insertOrUpdate(Context context, AlertNotified mAlertNotified) {
        if (getDao(context).load(mAlertNotified.getId()) == null) {
            getDao(context).insert(mAlertNotified);
        } else {
            getDao(context).update(mAlertNotified);
        }
    }

    public static List<AlertNotified> getAll(Context context) {
        return getDao(context).loadAll();
    }

    public static void delete(Context context, AlertNotified mAlertNotified) {
        getDao(context).delete(mAlertNotified);
    }

    public static boolean isExist(Context context, long id) {
        if (getDao(context).load(id) != null) {
            return true;
        } else {
            return false;
        }
    }

    public static void clearAll(Context context) {
        getDao(context).deleteAll();
    }
}
