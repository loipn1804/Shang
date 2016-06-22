package digimatic.shangcommerce.daocontroller;

import android.content.Context;

import java.util.List;

import digimatic.shangcommerce.application.MyApplication;
import greendao.ConfigAttribute;
import greendao.ConfigAttributeDao;

/**
 * Created by user on 3/21/2016.
 */
public class ConfigAttributeController {

    private static ConfigAttributeDao getDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getConfigAttributeDao();
    }

    public static void insertOrUpdate(Context context, ConfigAttribute mConfigAttribute) {
        if (getDao(context).load(mConfigAttribute.getAttribute_id()) == null) {
            getDao(context).insert(mConfigAttribute);
        } else {
            getDao(context).update(mConfigAttribute);
        }
    }

    public static List<ConfigAttribute> getAll(Context context) {
        return getDao(context).loadAll();
    }

    public static String getLabelById(Context context, long attribute_id) {
        ConfigAttribute configAttribute = getDao(context).load(attribute_id);
        if (configAttribute != null) {
            return configAttribute.getLabel();
        } else {
            return "";
        }
    }

    public static void clearAll(Context context) {
        getDao(context).deleteAll();
    }
}
