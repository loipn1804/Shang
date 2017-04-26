package digimatic.shangcommerce.testmode.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import digimatic.shangcommerce.R;
import digimatic.shangcommerce.testmode.Util.FileUtil;
import digimatic.shangcommerce.testmode.Util.ImageUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public final int CAMERA_REQUEST_VIDEO = 111;
    public final int CAMERA_REQUEST_PICTURE = 112;

    @Bind(R.id.btnRecord)
    Button btnRecord;
    @Bind(R.id.btnTakePicture)
    Button btnTakePicture;
    @Bind(R.id.btnLoad)
    Button btnLoad;
    @Bind(R.id.imv)
    ImageView imv;
    @Bind(R.id.videoView)
    VideoView videoView;

    @Bind(R.id.btnValidName)
    Button btnValidName;
    @Bind(R.id.edtName)
    EditText edtName;
    @Bind(R.id.txtLog)
    TextView txtLog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Log.e("LIO", "onCreate");

        initView();
        initData();
    }

    private void initView() {
        ButterKnife.bind(this);

        btnRecord.setOnClickListener(this);
        btnTakePicture.setOnClickListener(this);
        btnLoad.setOnClickListener(this);
        btnValidName.setOnClickListener(this);
    }

    private void initData() {
        grandPermission();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRecord:
                openCamera();
                break;
            case R.id.btnTakePicture:
                openCameraToTakePicture();
                break;
            case R.id.btnLoad:
                FileUtil.deleteTempFile(this);
                break;
            case R.id.btnValidName:
                validName();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        Log.e("LIO", "onDestroy");
        super.onDestroy();
    }

    private void openCamera() {
//        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileUtil.createTempFile(this));
//        startActivityForResult(cameraIntent, CAMERA_REQUEST);

        Intent cameraIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileUtil.createTempFile(this));
        startActivityForResult(cameraIntent, CAMERA_REQUEST_VIDEO);
    }

    private void openCameraToTakePicture() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, ImageUtil.createTempFile(this));
        startActivityForResult(cameraIntent, CAMERA_REQUEST_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST_VIDEO && resultCode == RESULT_OK) {
            Uri uri = FileUtil.getTempFile(this);
            Toast.makeText(this, uri.toString(), Toast.LENGTH_SHORT).show();
            try {
//                Bitmap bmImvMain = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
//                imv.setImageBitmap(bmImvMain);
                String root = FileUtil.getRootFolder(this);
                File myDir = new File(root + "/" + FileUtil.FOLDER);

                if(myDir.exists()){
                    File from = new File(myDir, FileUtil.FULL_NAME);
                    File to = new File(myDir, FileUtil.FULL_NAME + "." + FileUtil.EXTENSION);
                    if(from.exists())
                        from.renameTo(to);
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(to)));
                    MediaController mediaController = new MediaController(this);
                    mediaController.setAnchorView(videoView);
                    mediaController.setMediaPlayer(videoView);
                    videoView.setZOrderOnTop(false);
                    videoView.setMediaController(mediaController);
                    videoView.setVisibility(View.VISIBLE);
                    imv.setVisibility(View.GONE);
                    try {
                        videoView.setVideoURI(Uri.fromFile(to));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                Toast.makeText(this, "Fail " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == CAMERA_REQUEST_PICTURE && resultCode == RESULT_OK) {
            try {
                Uri uri = ImageUtil.getTempFile(this);
                Toast.makeText(this, uri.toString(), Toast.LENGTH_SHORT).show();
                Bitmap bmImvMain = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                imv.setImageBitmap(bmImvMain);
                videoView.setVisibility(View.GONE);
                imv.setVisibility(View.VISIBLE);

                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            } catch (Exception e) {
                Toast.makeText(this, "Fail " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show();
        }
    }

    private void grandPermission() {
        String permission = "android.permission.CAMERA";
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, 1);
        }
        permission = "android.permission.READ_EXTERNAL_STORAGE";
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, 1);
        }
        permission = "android.permission.WRITE_EXTERNAL_STORAGE";
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, 1);
        }
        permission = "android.permission.STORAGE";
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, 1);
        }
    }

    private String getPathUpload(String filename) {

        return null;
    }

    private void validName() {
        String name = edtName.getText().toString().trim();
        name = name.replaceAll("[^\\w.-]", "_");
        txtLog.setText(name);
    }
}