package digimatic.shangcommerce.adapter;

import android.app.Activity;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import digimatic.shangcommerce.fragment.IntroP31Fragment;
import digimatic.shangcommerce.fragment.IntroP32Fragment;
import digimatic.shangcommerce.fragment.IntroP33Fragment;

/**
 * Created by USER on 05/10/2016.
 */
public class IntroP3PagerAdapter extends FragmentPagerAdapter {

    private Activity activity;

    public IntroP3PagerAdapter(FragmentManager fragmentManager, Activity activity) {
        super(fragmentManager);
        this.activity = activity;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                IntroP31Fragment introP31Fragment = new IntroP31Fragment();
                return introP31Fragment;
            case 1:
                IntroP32Fragment introP32Fragment = new IntroP32Fragment();
                return introP32Fragment;
            default:
                IntroP33Fragment introP33Fragment = new IntroP33Fragment();
                return introP33Fragment;
        }
    }

    @Override
    public int getCount() {

        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
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
