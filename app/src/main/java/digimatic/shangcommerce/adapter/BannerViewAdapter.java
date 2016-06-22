package digimatic.shangcommerce.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import digimatic.shangcommerce.fragment.ImageBannerViewFragment;
import digimatic.shangcommerce.fragment.VideoBannerViewFragment;
import digimatic.shangcommerce.model.Banner;

/**
 * Created by USER on 16/6/2016.
 */
public class BannerViewAdapter extends FragmentPagerAdapter {

    private ArrayList<Banner> mListBanner;

    public BannerViewAdapter(ArrayList<Banner> listBanner, FragmentManager fragmentManager) {
        super(fragmentManager);
        mListBanner = listBanner;
    }

    @Override
    public Fragment getItem(int position) {
        Banner currentBanner = mListBanner.get(position);
        if (currentBanner.isImage()) {
            return ImageBannerViewFragment.getInstance(currentBanner.getUrl());
        }
        return VideoBannerViewFragment.getInstance(currentBanner.getUrl_video());
    }

    @Override
    public int getCount() {
        return mListBanner.size();
    }
}
