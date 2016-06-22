package digimatic.shangcommerce.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import digimatic.shangcommerce.fragment.AllCategoryFragment;
import digimatic.shangcommerce.fragment.ListProductFragment;
import greendao.Category;

/**
 * Created by USER on 3/4/2016.
 */
public class CategoryPagerAdapter extends FragmentPagerAdapter {

    private Activity activity;
    private List<Category> listData;

    public CategoryPagerAdapter(FragmentManager fragmentManager, Activity activity, List<Category> listData) {
        super(fragmentManager);
        this.activity = activity;
        this.listData = listData;
    }

    @Override
    public Fragment getItem(int i) {
        if (i == 0) {
            AllCategoryFragment allCategoryFragment = new AllCategoryFragment();
            return allCategoryFragment;
        }
        ListProductFragment listProductFragment = new ListProductFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("category_id", listData.get(i).getEntity_id());
        listProductFragment.setArguments(bundle);
        return listProductFragment;
    }

    @Override
    public int getCount() {

        return listData.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return listData.get(position).getName();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        super.destroyItem(container, position, object);
    }

    @Override
    public void finishUpdate(ViewGroup container) {

        super.finishUpdate(container);
    }

    @Override
    public long getItemId(int position) {

        return super.getItemId(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        return super.instantiateItem(container, position);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {

        return super.isViewFromObject(view, object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {

        super.restoreState(state, loader);
    }

    @Override
    public Parcelable saveState() {

        return super.saveState();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {

        super.setPrimaryItem(container, position, object);
    }

    @Override
    public void startUpdate(ViewGroup container) {

        super.startUpdate(container);
    }

}
