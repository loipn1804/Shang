package digimatic.shangcommerce.daocontroller;

import android.content.Context;

import java.util.List;

import digimatic.shangcommerce.application.MyApplication;
import greendao.Notification;
import greendao.NotificationDao;

/**
 * Created by USER on 3/22/2016.
 */
public class NotificationController {

    private static NotificationDao getDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getNotificationDao();
    }

    public static void insertOrUpdate(Context context, Notification mNotification) {
        if (getDao(context).load(mNotification.getId()) == null) {
            getDao(context).insert(mNotification);
        } else {
            getDao(context).update(mNotification);
        }
    }

    public static void setReadAlready(Context context, long id) {
        Notification notification = getDao(context).load(id);
        if (notification != null) {
            notification.setIs_read(1);
            getDao(context).update(notification);
        }
    }

    public static List<Notification> getAll(Context context) {
        return getDao(context).queryRaw(" ORDER BY id DESC");
    }

    public static boolean isExist(Context context, long id) {
        Notification notification = getDao(context).load(id);
        if (notification != null) {
            return true;
        } else {
            return false;
        }
    }

    public static void delete(Context context, Notification mNotification) {
        getDao(context).delete(mNotification);
    }

    public static void deleteById(Context context, long id) {
        getDao(context).deleteByKey(id);
    }

    public static void clearAll(Context context) {
        getDao(context).deleteAll();
    }
}
