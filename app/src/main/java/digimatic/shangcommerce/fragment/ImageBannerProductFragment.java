package digimatic.shangcommerce.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.activity.PhotoZoomActivity;

/**
 * Created by USER on 3/5/2016.
 */
public class ImageBannerProductFragment extends Fragment {

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    private ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        imageLoader.clearDiskCache();
        options = new DisplayImageOptions.Builder().cacheInMemory(false)
                .showImageForEmptyUri(R.drawable.noimg)
                .showImageOnLoading(R.drawable.noimg)
                .cacheOnDisk(true).build();

        final String url = getArguments().getString("url", "");

        View view = inflater.inflate(R.layout.fragment_image_banner_product, container, false);
        imageView = (ImageView) view.findViewById(R.id.imageViewBanner);
        imageLoader.displayImage(url, imageView, options);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PhotoZoomActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
            }
        });

        return view;
    }

    public void updateImage(String url) {
        imageLoader.displayImage(url, imageView, options);
    }
}
