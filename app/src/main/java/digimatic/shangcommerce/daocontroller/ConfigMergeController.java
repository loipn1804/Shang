package digimatic.shangcommerce.daocontroller;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import digimatic.shangcommerce.application.MyApplication;
import digimatic.shangcommerce.model.CountId;
import greendao.ConfigMerge;
import greendao.ConfigMergeDao;
import greendao.ConfigOption;

/**
 * Created by user on 3/21/2016.
 */
public class ConfigMergeController {

    private static ConfigMergeDao getDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getConfigMergeDao();
    }

    public static void insertOrUpdate(Context context, ConfigMerge mConfigMerge) {
        getDao(context).insert(mConfigMerge);
    }

    public static List<ConfigMerge> getAllAttributeId(Context context) {
        return getDao(context).queryRaw(" GROUP BY attribute_id ORDER BY position ASC");
    }

    public static List<ConfigMerge> getListNotChosen(Context context) {
        List<ConfigMerge> mergeList = new ArrayList<>();
        mergeList.addAll(getDao(context).queryRaw(" WHERE is_chosen_att = 0 GROUP BY attribute_id ORDER BY position ASC"));
        return mergeList;
    }

    public static String getOptionLabelChosenByAttributeId(Context context, long attribute_id) {
        List<ConfigMerge> mergeList = getDao(context).queryRaw(" WHERE attribute_id = ? AND is_chosen_opt = 1 GROUP BY option_id ORDER BY position ASC", attribute_id + "");
        if (mergeList.size() > 0) {
            return ConfigOptionController.getLabelById(context, mergeList.get(0).getOption_id());
        } else {
            return "";
        }
    }

    public static List<ConfigOption> getOptionListByFilterChosen(Context context, long attribute_id) {
        List<ConfigOption> optionList = new ArrayList<>();
        List<ConfigMerge> mergeList = getDao(context).queryRaw(" WHERE attribute_id <> ? AND is_chosen_opt = 1 ORDER BY position ASC", attribute_id + "");
        if (mergeList.size() > 0) {
            List<ConfigMerge> sizeList = getDao(context).queryRaw(" WHERE attribute_id <> ? AND is_chosen_opt = 1 GROUP BY option_id ORDER BY position ASC", attribute_id + "");
            int size = sizeList.size();
            List<ConfigMerge> mergeListFiltered = getMaxCountProductId(size, mergeList);
            for (ConfigMerge configMerge : mergeListFiltered) {
                ConfigMerge merge = getByProductIdAndAttributeId(context, attribute_id, configMerge.getProduct_id());
                if (merge != null) {
                    optionList.add(ConfigOptionController.getById(context, merge.getOption_id()));
                }
            }
        } else {
            List<ConfigMerge> listAll = getDao(context).queryRaw(" WHERE attribute_id = ? GROUP BY option_id ORDER BY position ASC", attribute_id + "");
            for (ConfigMerge configMerge : listAll) {
                optionList.add(ConfigOptionController.getById(context, configMerge.getOption_id()));
            }
        }

        return optionList;
    }

    private static List<ConfigMerge> getMaxCountProductId(int size, List<ConfigMerge> mergeList) {
        List<ConfigMerge> list = new ArrayList<>();
        List<CountId> countIds = new ArrayList<>();
        for (ConfigMerge merge : mergeList) {
            boolean isExist = false;
            for (CountId countId : countIds) {
                if (merge.getProduct_id() == countId.getId()) {
                    countId.setCount(countId.getCount() + 1);
                    isExist = true;
                    break;
                }
            }
            if (!isExist) {
                countIds.add(new CountId(merge.getProduct_id(), 1));
            }
        }
        for (CountId countId : countIds) {
            if (countId.getCount() >= size) {
                for (ConfigMerge merge : mergeList) {
                    if (merge.getProduct_id() == countId.getId()) {
                        boolean isExist = false;
                        for (ConfigMerge mergeInList : list) {
                            if (merge.getProduct_id().equals(mergeInList.getProduct_id())) {
                                isExist = true;
                            }
                        }
                        if (!isExist) {
                            list.add(merge);
                        }
                        break;
                    }
                }
            }
        }
        return list;
    }

    public static List<ConfigOption> getOptionListByOptionId(Context context, long attribute_id) {
        List<ConfigOption> optionList = new ArrayList<>();
        List<ConfigMerge> listAll = getDao(context).queryRaw(" WHERE attribute_id = ? GROUP BY option_id ORDER BY position ASC", attribute_id + "");
        for (ConfigMerge configMerge : listAll) {
            optionList.add(ConfigOptionController.getById(context, configMerge.getOption_id()));
        }

        return optionList;
    }

    public static float getTotalPriceOptionChosen(Context context) {
        List<ConfigMerge> mergeList = getDao(context).queryRaw(" WHERE is_chosen_opt = 1 GROUP BY option_id ORDER BY position ASC");
        float total = 0;
        for (ConfigMerge configMerge : mergeList) {
            ConfigOption configOption = ConfigOptionController.getById(context, configMerge.getOption_id());
            if (configOption != null) {
                total += configOption.getPrice();
            }
        }

        return total;
    }

    public static ConfigMerge getByProductIdAndAttributeId(Context context, long attribute_id, long product_id) {
        List<ConfigMerge> mergeList = getDao(context).queryRaw(" WHERE attribute_id = ? AND product_id = ? ORDER BY position ASC", attribute_id + "", product_id + "");
        if (mergeList.size() > 0) {
            return mergeList.get(0);
        } else {
            return null;
        }
    }

    public static long getProductToAddCart(Context context) {
        List<ConfigMerge> mergeList = getDao(context).queryRaw(" WHERE is_chosen_att = 1 AND is_chosen_opt = 1 ORDER BY position ASC");
        if (mergeList.size() > 0) {
            List<CountId> countIds = new ArrayList<>();
            for (ConfigMerge configMerge : mergeList) {
                boolean isHave = false;
                for (CountId countId : countIds) {
                    if (countId.getId() == configMerge.getProduct_id()) {
                        countId.setCount(countId.getCount() + 1);
                        isHave = true;
                        break;
                    }
                }
                if (!isHave) {
                    countIds.add(new CountId(configMerge.getProduct_id(), 1));
                }
            }
            int maxCount = countIds.get(0).getCount();
            for (int i = 0; i < countIds.size(); i++) {
                if (maxCount < countIds.get(i).getCount()) {
                    maxCount = countIds.get(i).getCount();
                }
            }
            long product_id = 0;
            for (CountId countId : countIds) {
                if (maxCount == countId.getCount()) {
                    product_id = countId.getId();
                    break;
                }
            }
            return product_id;
        } else {
            return 0;
        }
    }

    public static List<ConfigMerge> getListChosen(Context context, long product_id) {
        List<ConfigMerge> mergeList = getDao(context).queryRaw(" WHERE is_chosen_att = 1 AND is_chosen_opt = 1 AND product_id = ? ORDER BY position ASC", product_id + "");
        return mergeList;
    }

    public static void setChosenOptionAndAttribute(Context context, long option_id) {
        List<ConfigMerge> mergeList = getDao(context).queryRaw(" WHERE option_id = ? ORDER BY position ASC", option_id + "");
        if (mergeList.size() > 0) {
            long attribute_id = mergeList.get(0).getAttribute_id();
            List<ConfigMerge> mergeListToUpdate = getDao(context).queryRaw(" WHERE attribute_id = ? ORDER BY position ASC", attribute_id + "");
            for (ConfigMerge configMerge : mergeListToUpdate) {
                configMerge.setIs_chosen_att(1);
                if (configMerge.getOption_id() == option_id) {
                    configMerge.setIs_chosen_opt(1);
                } else {
                    configMerge.setIs_chosen_opt(0);
                }
                getDao(context).update(configMerge);
            }
        }
    }

    public static void resetOtherWhenChosenNotCompatible(Context context, long option_id) {
        List<ConfigMerge> mergeList = getDao(context).queryRaw(" WHERE option_id = ? ORDER BY position ASC", option_id + "");
        if (mergeList.size() > 0) {
            long attribute_id = mergeList.get(0).getAttribute_id();
            List<ConfigMerge> mergeListToUpdate = getDao(context).queryRaw(" WHERE attribute_id <> ? ORDER BY position ASC", attribute_id + "");
            for (ConfigMerge configMerge : mergeListToUpdate) {
                configMerge.setIs_chosen_att(0);
                configMerge.setIs_chosen_opt(0);
                getDao(context).update(configMerge);
            }
        }
    }

    public static void clearAll(Context context) {
        getDao(context).deleteAll();
    }
}
