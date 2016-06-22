package digimatic.shangcommerce.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import digimatic.shangcommerce.R;

/**
 * Created by USER on 16/6/2016.
 */
public class VideoBannerViewFragment extends Fragment {


    public static Fragment getInstance(String url) {
        VideoBannerViewFragment videoBannerViewFragment = new VideoBannerViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        videoBannerViewFragment.setArguments(bundle);
        return videoBannerViewFragment;
    }

    private VideoView mVideoBanner;
    private MediaController mMediaController;
    private String mUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mUrl = getArguments().getString("url", "");
        View view = inflater.inflate(R.layout.fragment_banner_video_view, container, false);
        mVideoBanner = (VideoView) view.findViewById(R.id.videoBanner);
        initData();
        return view;
    }

    private void initData() {
        mMediaController = new MediaController(getActivity());
        mMediaController.setAnchorView(mVideoBanner);
        mVideoBanner.setMediaController(mMediaController);
        mVideoBanner.setVideoURI(Uri.parse(mUrl));
        mVideoBanner.seekTo(100);
    }
}
