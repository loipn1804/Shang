package digimatic.shangcommerce.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.staticfunction.Font;

public class ImageBannerShopFragment extends Fragment {

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .showImageForEmptyUri(R.drawable.noimg)
                .showImageOnLoading(R.drawable.noimg)
                .cacheOnDisk(true).build();

        String url = getArguments().getString("url");

        View view = inflater.inflate(R.layout.fragment_image_banner_shop, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageViewBanner);
        imageLoader.displayImage(url, imageView, options);

        return view;
    }
}
