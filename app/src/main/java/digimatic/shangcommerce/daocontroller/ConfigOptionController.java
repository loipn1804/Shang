package digimatic.shangcommerce.daocontroller;

import android.content.Context;

import java.util.List;

import digimatic.shangcommerce.application.MyApplication;
import greendao.ConfigOption;
import greendao.ConfigOptionDao;

/**
 * Created by user on 3/21/2016.
 */
public class ConfigOptionController {

    private static ConfigOptionDao getDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getConfigOptionDao();
    }

    public static void insertOrUpdate(Context context, ConfigOption mConfigOption) {
        if (getDao(context).load(mConfigOption.getOption_id()) == null) {
            getDao(context).insert(mConfigOption);
        } else {
            getDao(context).update(mConfigOption);
        }
    }

    public static List<ConfigOption> getAll(Context context) {
        return getDao(context).loadAll();
    }

    public static ConfigOption getById(Context context, long option_id) {
        List<ConfigOption> optionList = getDao(context).queryRaw(" WHERE option_id = ? ORDER BY position ASC", option_id + "");
        if (optionList.size() > 0) {
            return optionList.get(0);
        } else {
            return null;
        }
    }

    public static String getLabelById(Context context, long option_id) {
        ConfigOption configOption = getDao(context).load(option_id);
        if (configOption != null) {
            return configOption.getLabel();
        } else {
            return "";
        }
    }

    public static void clearAll(Context context) {
        getDao(context).deleteAll();
    }
}
