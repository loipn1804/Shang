package digimatic.shangcommerce.daocontroller;

import android.content.Context;

import java.util.List;

import digimatic.shangcommerce.application.MyApplication;
import greendao.Category;
import greendao.CategoryDao;

/**
 * Created by USER on 3/11/2016.
 */
public class CategoryController {

    private static CategoryDao getDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getCategoryDao();
    }

    public static void insertOrUpdate(Context context, Category mCategory) {
        if (getDao(context).load(mCategory.getEntity_id()) == null) {
            getDao(context).insert(mCategory);
        } else {
            getDao(context).update(mCategory);
        }
    }

    public static Category getById(Context context, long category_id) {
        return getDao(context).loadByRowId(category_id);
    }

    public static List<Category> getAll(Context context) {
        return getDao(context).queryRaw(" ORDER BY position ASC");
    }

    public static void delete(Context context, Category mCategory) {
        getDao(context).delete(mCategory);
    }

    public static void clearAll(Context context) {
        getDao(context).deleteAll();
    }
}
