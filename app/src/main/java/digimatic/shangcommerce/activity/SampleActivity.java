package digimatic.shangcommerce.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import digimatic.shangcommerce.R;

/**
 * Created by USER on 04/13/2016.
 */
public class SampleActivity extends BaseActivity {

    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        videoView = (VideoView) findViewById(R.id.videoView);
        videoView.setVideoURI(Uri.parse("http://shang-dev.techub.io/media/video/movie_1.mp4"));
//        videoView.start();

        MediaController mediaController = new
                MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.requestFocus();

        videoView.seekTo(100);

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://shang-dev.techub.io/media/video/movie_1.mp4"));
        intent.setDataAndType(Uri.parse("http://shang-dev.techub.io/media/video/movie_1.mp4"), "video/*");
        startActivity(intent);

//        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                showToastInfo(videoView.getDuration() + "");
//            }
//        });
    }
}
