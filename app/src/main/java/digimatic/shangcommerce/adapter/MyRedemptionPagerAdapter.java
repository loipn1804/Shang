package digimatic.shangcommerce.adapter;

import android.app.Activity;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import digimatic.shangcommerce.fragment.MyRedemptionEVoucherFragment;
import digimatic.shangcommerce.fragment.MyRedemptionProductFragment;

/**
 * Created by USER on 05/04/2016.
 */
public class MyRedemptionPagerAdapter extends FragmentPagerAdapter {

    private Activity activity;

    public MyRedemptionPagerAdapter(FragmentManager fragmentManager, Activity activity) {
        super(fragmentManager);
        this.activity = activity;
    }

    @Override
    public Fragment getItem(int i) {
        if (i == 0) {
            MyRedemptionProductFragment myRedemptionProductFragment = new MyRedemptionProductFragment();
            return myRedemptionProductFragment;
        } else {
            MyRedemptionEVoucherFragment myRedemptionEVoucherFragment = new MyRedemptionEVoucherFragment();
            return myRedemptionEVoucherFragment;
        }
    }

    @Override
    public int getCount() {

        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "PRODUCTS";
        } else {
            return "eVOUCHERS";
        }
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
