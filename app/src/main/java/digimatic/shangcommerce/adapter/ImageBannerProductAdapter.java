package digimatic.shangcommerce.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import digimatic.shangcommerce.fragment.ImageBannerProductFragment;
import digimatic.shangcommerce.fragment.VideoBannerProductFragment;
import digimatic.shangcommerce.model.Banner;

/**
 * Created by USER on 3/5/2016.
 */
public class ImageBannerProductAdapter extends FragmentPagerAdapter {

    private Activity activity;
    private List<Banner> listData;
    private List<Fragment> fragments = new ArrayList<>();

    public ImageBannerProductAdapter(FragmentManager fragmentManager, Activity activity, List<Banner> listData) {
        super(fragmentManager);
        this.activity = activity;
        this.listData = new ArrayList<>();
        this.listData.addAll(listData);
        for (Banner banner : listData) {
            if (banner.isImage()) {
                ImageBannerProductFragment imageBannerProductFragment = new ImageBannerProductFragment();
                Bundle bundle = new Bundle();
                bundle.putString("url", banner.getUrl());
                imageBannerProductFragment.setArguments(bundle);
                fragments.add(imageBannerProductFragment);
            } else {
                VideoBannerProductFragment videoBannerProductFragment = new VideoBannerProductFragment();
                Bundle bundle = new Bundle();
                bundle.putString("url", banner.getUrl());
                bundle.putString("url_video", banner.getUrl_video());
                videoBannerProductFragment.setArguments(bundle);
                fragments.add(videoBannerProductFragment);
            }
        }
    }

    public void removeListFragment(FragmentManager fragmentManager) {
        for (int i = 0; i < fragments.size(); i++)
            fragmentManager.beginTransaction().remove(fragments.get(i)).commit();
        fragments.clear();
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {

        return listData.size();
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
