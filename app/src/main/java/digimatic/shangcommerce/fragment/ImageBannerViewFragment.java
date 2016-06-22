package digimatic.shangcommerce.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.polites.android.GestureImageView;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.activity.PhotoZoomActivity;
import digimatic.shangcommerce.model.Banner;

/**
 * Created by USER on 16/6/2016.
 */
public class ImageBannerViewFragment extends Fragment {

    private GestureImageView mImgBanner;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    private String mUrl;

    public static Fragment getInstance(String url) {
        ImageBannerViewFragment imageBannerViewFragment = new ImageBannerViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        imageBannerViewFragment.setArguments(bundle);
        return imageBannerViewFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        imageLoader.clearDiskCache();
        options = new DisplayImageOptions.Builder().cacheInMemory(false)
                .showImageForEmptyUri(R.drawable.noimg)
                .showImageOnLoading(R.drawable.noimg)
                .cacheOnDisk(true).build();

        mUrl = getArguments().getString("url", "");

        View view = inflater.inflate(R.layout.fragment_banner_image_view, container, false);
        mImgBanner = (GestureImageView) view.findViewById(R.id.imgBanner);
        mImgBanner.setImageLevel(0);

        imageLoader.loadImage(mUrl, options, new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String arg0, View arg1) {

            }

            @Override
            public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {

            }

            @Override
            public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {

                mImgBanner.setImageBitmap(arg2);
            }

            @Override
            public void onLoadingCancelled(String arg0, View arg1) {

            }
        });
        return view;
    }

}

