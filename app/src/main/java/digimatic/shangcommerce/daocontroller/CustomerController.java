package digimatic.shangcommerce.daocontroller;

import android.content.Context;

import java.util.List;

import digimatic.shangcommerce.application.MyApplication;
import greendao.Customer;
import greendao.CustomerDao;

/**
 * Created by USER on 2/16/2016.
 */
public class CustomerController {

    private static CustomerDao getDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getCustomerDao();
    }

    public static void insertOrUpdate(Context context, Customer mCustomer) {
        if (getDao(context).load(mCustomer.getEntity_id()) == null) {
            getDao(context).insert(mCustomer);
        } else {
            getDao(context).update(mCustomer);
        }
    }

    public static boolean isLogin(Context context) {
        List<Customer> list = getDao(context).loadAll();
        if (list.size() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public static Customer getCurrentCustomer(Context context) {
        List<Customer> list = getDao(context).loadAll();
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public static void delete(Context context, Customer mCustomer) {
        getDao(context).delete(mCustomer);
    }

    public static void clearAll(Context context) {
        getDao(context).deleteAll();
    }
}
