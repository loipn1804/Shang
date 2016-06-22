package digimatic.shangcommerce.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.connection.Constant;
import digimatic.shangcommerce.connection.callback.UICallback;
import digimatic.shangcommerce.connection.threadconnection.execute.CustomerExecute;
import digimatic.shangcommerce.connection.threadconnection.execute.NotificationExecute;
import digimatic.shangcommerce.daocontroller.CustomerController;
import digimatic.shangcommerce.daocontroller.NotificationController;
import digimatic.shangcommerce.staticfunction.Font;
import greendao.Customer;
import greendao.Notification;

/**
 * Created by USER on 3/22/2016.
 */
public class AlertDetailActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rltBack;
    private TextView txtTitle;

    private WebView webView;
    private boolean isFromNotification;
    private long type_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_detail);

        if (getIntent().getExtras() != null) {
            type_id = getIntent().getLongExtra("type_id", 0);
            isFromNotification = getIntent().getBooleanExtra("isFromNotification", true);
            if (type_id == 0) {
                finish();
            } else {
                initView();
                initData();
            }
        } else {
            finish();
        }

        if (isFromNotification) {
            getNotifications();
        } else {
            txtTitle.setText(getIntent().getStringExtra("title"));
        }
    }

    private void initView() {
        rltBack = (RelativeLayout) findViewById(R.id.rltBack);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        webView = (WebView) findViewById(R.id.webView);

        rltBack.setOnClickListener(this);

        Font font = new Font(this);
        font.overrideFontsLight(findViewById(R.id.root));
        font.overrideFontsBold(txtTitle);
    }

    private void initData() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(Constant.BASE_URL + "webapp/message/view/id/" + type_id);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltBack:
                finish();
                break;
        }
    }

    private void getNotifications() {
        Customer customer = CustomerController.getCurrentCustomer(this);
        UICallback callback = new UICallback() {
            @Override
            public void onResult(boolean success, String data) {
                if (success) {
                    try {
                        JSONArray array = new JSONArray(data);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            long id = object.getLong("id");
                            long type_id = object.getLong("type_id");
                            String title = object.getString("title");
                            if (title.equalsIgnoreCase("null")) title = "";

                            if (AlertDetailActivity.this.type_id == type_id) {
                                txtTitle.setText(title);
                                updateById(id);
                                break;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showToastError(e.getMessage());
                    }
                } else {
                    showToastError(data);
                }
                hideProgressDialog();
            }
        };
        NotificationExecute.getAll(this, callback, customer.getEntity_id(), 1);
        showProgressDialog(false);
    }

    private void updateById(final long id) {
        JSONObject object = new JSONObject();
        try {
            object.put("is_read", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        UICallback callback = new UICallback() {
            @Override
            public void onResult(boolean success, String data) {

            }
        };
        NotificationExecute.updateById(this, callback, id, object.toString());
    }
}
