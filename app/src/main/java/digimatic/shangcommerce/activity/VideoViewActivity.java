package digimatic.shangcommerce.activity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import digimatic.shangcommerce.R;

/**
 * Created by USER on 06/02/2016.
 */
public class VideoViewActivity extends BaseActivity {

    private VideoView videoView;
    private MediaController mediaController;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);

        if (getIntent().hasExtra("url")) {
            url = getIntent().getStringExtra("url");
            initView();
            initData();
        } else {
            finish();
        }
    }

    private void initView() {
        videoView = (VideoView) findViewById(R.id.videoView);
    }

    private void initData() {
        mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(Uri.parse(url));
        videoView.start();
    }
}
