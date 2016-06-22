package digimatic.shangcommerce.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.application.MyApplication;
import digimatic.shangcommerce.connection.threadconnection.thread.UIThreadExecutor;
import digimatic.shangcommerce.staticfunction.Font;
import digimatic.shangcommerce.staticfunction.StaticFunction;
import digimatic.shangcommerce.view.SimpleToast;

/**
 * Created by USER on 3/3/2016.
 */
public class BaseActivity extends AppCompatActivity {

    private Tracker mTracker;

    private Dialog progress_dialog = null;

    public UIThreadExecutor uiThreadExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiThreadExecutor = UIThreadExecutor.getInstance();
        MyApplication application = (MyApplication) getApplication();
        mTracker = application.getDefaultTracker();
    }

    public void sendScreenName(String name) {
        mTracker.setScreenName(name);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public void sendEvent(String category, String action) {
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .build());
    }

    public void showProgressDialog(boolean cancelable) {
        if (progress_dialog == null) {
            progress_dialog = new Dialog(BaseActivity.this);
            progress_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progress_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            progress_dialog.setContentView(R.layout.progress_dialog);
        }

        if (!progress_dialog.isShowing()) {
            progress_dialog.setCanceledOnTouchOutside(cancelable);
            progress_dialog.setCancelable(cancelable);
            progress_dialog.show();
        }

        if (!StaticFunction.isNetworkAvailable(this)) {
            progress_dialog.dismiss();
        }
    }

    public void hideProgressDialog() {
        if (progress_dialog != null && progress_dialog.isShowing()) {
            progress_dialog.dismiss();
        }
    }

    public void showToastError(String message) {
        if (message != null) {
            SimpleToast.error(BaseActivity.this, message);
        }
    }

    public void showToastInfo(String message) {
        if (message != null) {
            SimpleToast.info(BaseActivity.this, message);
        }
    }

    public void showToastOk(String message) {
        if (message != null) {
            SimpleToast.ok(BaseActivity.this, message);
        }
    }

    public void showPopupPrompt(String message) {
        // custom dialog
        final Dialog dialog = new Dialog(this);

//        dialog.getWindow().clearFlags(
//                WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_prompt);

        TextView txtMessage = (TextView) dialog.findViewById(R.id.txtMessage);
        TextView txtOk = (TextView) dialog.findViewById(R.id.txtOk);
        txtMessage.setText(message);

        Font font = new Font(this);
        font.overrideFontsLight(txtMessage);
        font.overrideFontsBold(txtOk);

        txtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
