package digimatic.shangcommerce.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.listeners.OnLogoutListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.connection.callback.UICallback;
import digimatic.shangcommerce.connection.threadconnection.execute.CustomerExecute;
import digimatic.shangcommerce.daocontroller.CartDetailController;
import digimatic.shangcommerce.daocontroller.CartItemController;
import digimatic.shangcommerce.daocontroller.CustomerController;
import digimatic.shangcommerce.daocontroller.NotificationController;
import digimatic.shangcommerce.daocontroller.WishlistController;
import digimatic.shangcommerce.gcm.PlayServicesHelper;
import digimatic.shangcommerce.staticfunction.Font;
import greendao.Customer;

/**
 * Created by USER on 3/8/2016.
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rltBack;
    private TextView txtTitle;

    private RelativeLayout rltEditProfile;
    private RelativeLayout rltChangePassword;
    private RelativeLayout rltAbout;
    private RelativeLayout rltTerm;
    private TextView txtBuildNo;
    private TextView txtSignout;
    private TextView txtCardCode;

    private TextView txtNotificationLabel;
    private RelativeLayout rltNotification;
    private ToggleButton toggleNotification;

    // facebook
    private SimpleFacebook mSimpleFacebook;
    // facebook

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        sendEvent(getString(R.string.analytic_setting), getString(R.string.analytic_in));

        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sendEvent(getString(R.string.analytic_setting), getString(R.string.analytic_out));
    }

    private void initView() {
        rltBack = (RelativeLayout) findViewById(R.id.rltBack);
        txtTitle = (TextView) findViewById(R.id.txtTitle);

        rltEditProfile = (RelativeLayout) findViewById(R.id.rltEditProfile);
        rltChangePassword = (RelativeLayout) findViewById(R.id.rltChangePassword);
        rltAbout = (RelativeLayout) findViewById(R.id.rltAbout);
        rltTerm = (RelativeLayout) findViewById(R.id.rltTerm);
        txtBuildNo = (TextView) findViewById(R.id.txtBuildNo);
        txtSignout = (TextView) findViewById(R.id.txtSignout);
        txtCardCode = (TextView) findViewById(R.id.txtCardCode);

        txtNotificationLabel = (TextView) findViewById(R.id.txtNotificationLabel);
        rltNotification = (RelativeLayout) findViewById(R.id.rltNotification);
        toggleNotification = (ToggleButton) findViewById(R.id.toggleNotification);

        rltBack.setOnClickListener(this);
        rltEditProfile.setOnClickListener(this);
        rltChangePassword.setOnClickListener(this);
        rltAbout.setOnClickListener(this);
        rltTerm.setOnClickListener(this);
        txtSignout.setOnClickListener(this);
        txtCardCode.setOnClickListener(this);

        Font font = new Font(this);
        font.overrideFontsLight(findViewById(R.id.root));
        font.overrideFontsBold(txtTitle);
        font.overrideFontsBold(txtNotificationLabel);
        font.overrideFontsBold(findViewById(R.id.txtAccountLabel));
        font.overrideFontsBold(findViewById(R.id.txtAboutLabel));

        txtNotificationLabel.setVisibility(View.GONE);
        rltNotification.setVisibility(View.GONE);
    }

    private void initData() {
        if (CustomerController.isLogin(this)) {
            txtSignout.setVisibility(View.VISIBLE);
            if (CustomerController.getCurrentCustomer(this).getIs_login_fb()) {
                rltChangePassword.setVisibility(View.GONE);
            }

            txtNotificationLabel.setVisibility(View.VISIBLE);
            rltNotification.setVisibility(View.VISIBLE);
            setToggleNotification();
            txtCardCode.setText("Card Code: " + CustomerController.getCurrentCustomer(this).getCard_code());
        } else {
            txtSignout.setVisibility(View.GONE);
            txtNotificationLabel.setVisibility(View.GONE);
            rltNotification.setVisibility(View.GONE);
            toggleNotification.setOnCheckedChangeListener(null);
        }
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            Date date = new Date(packageInfo.lastUpdateTime);
            SimpleDateFormat format = new SimpleDateFormat("ddMMyyyy");
            txtBuildNo.setText("Build No.: " + format.format(date));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltBack:
                finish();
                break;
            case R.id.rltEditProfile:
                Intent intentProfileEdit = new Intent(this, AccountEditActivity.class);
                startActivity(intentProfileEdit);
                break;
            case R.id.rltChangePassword:
                Intent intentChangePassword = new Intent(this, ChangePasswordActivity.class);
                startActivity(intentChangePassword);
                break;
            case R.id.rltAbout:
                Intent intentAboutApp = new Intent(this, AboutAppActivity.class);
                startActivity(intentAboutApp);
                break;
            case R.id.rltTerm:
                Intent intentTerm = new Intent(this, TermActivity.class);
                startActivity(intentTerm);
                break;
            case R.id.txtSignout:
                showPopupConfirmLogout("Do you wish to sign out?");
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSimpleFacebook = SimpleFacebook.getInstance(this);
    }

    private void setToggleNotification() {
        toggleNotification.setOnCheckedChangeListener(null);
        if (CustomerController.getCurrentCustomer(this).getIs_notification_sound() == 1) {
            toggleNotification.setChecked(true);
        } else {
            toggleNotification.setChecked(false);
        }
        toggleNotification.setOnCheckedChangeListener(toggleOnCheckedChangeListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mSimpleFacebook.onActivityResult(requestCode, resultCode, data);
    }

    private CompoundButton.OnCheckedChangeListener toggleOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                updateNotification(1);
            } else {
                updateNotification(0);
            }
        }
    };

    public void showPopupConfirmLogout(String message) {
        // custom dialog
        final Dialog dialog = new Dialog(this);

//        dialog.getWindow().clearFlags(
//                WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_confirm);

        TextView txtMessage = (TextView) dialog.findViewById(R.id.txtMessage);
        TextView txtOk = (TextView) dialog.findViewById(R.id.txtOk);
        TextView txtCancel = (TextView) dialog.findViewById(R.id.txtCancel);
        txtMessage.setText(message);

        Font font = new Font(this);
        font.overrideFontsLight(txtMessage);
        font.overrideFontsLight(txtCancel);
        font.overrideFontsBold(txtOk);

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        txtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void logout() {
        JSONObject object = new JSONObject();
        try {
            object.put("account_id", CustomerController.getCurrentCustomer(this).getEntity_id());
            String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            object.put("device_id", android_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        UICallback callback = new UICallback() {
            @Override
            public void onResult(boolean success, String data) {
                if (success) {
//                    showToastOk("Successfully logout.");
                    if (CustomerController.getCurrentCustomer(SettingActivity.this).getIs_login_fb()) {
                        mSimpleFacebook.logout(onLogoutListener);
                    }
                    PlayServicesHelper playServicesHelper = new PlayServicesHelper(SettingActivity.this);
                    playServicesHelper.removeRegId();
                    sendEvent(getString(R.string.analytic_sign_out), getString(R.string.analytic_action_sign_out));
                    CustomerController.clearAll(SettingActivity.this);
                    WishlistController.clearAll(SettingActivity.this);
                    CartDetailController.clearAll(SettingActivity.this);
                    CartItemController.clearAll(SettingActivity.this);
                    NotificationController.clearAll(SettingActivity.this);
                    txtSignout.setVisibility(View.GONE);
                    finish();
                } else {
                    showToastError(data);
                }
                hideProgressDialog();
            }
        };
        CustomerExecute.logout(this, callback, object.toString());
        showProgressDialog(false);
    }

    private void updateNotification(final int isOn) {
        JSONObject object = new JSONObject();
        try {
            object.put("is_notification_sound", isOn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        UICallback callback = new UICallback() {
            @Override
            public void onResult(boolean success, String data) {
//                if (success) {
//                    if (isOn == 1) {
//                        showToastOk("Turned on notification successfully");
//                    } else {
//                        showToastOk("Turned off notification successfully");
//                    }
//                } else {
//                    if (isOn == 1) {
//                        showToastOk("Turned on notification unsuccessfully");
//                    } else {
//                        showToastOk("Turned off notification unsuccessfully");
//                    }
//                }
                getCustomerDetailToUpdateNotification();
            }
        };
        CustomerExecute.updateNotification(this, callback, CustomerController.getCurrentCustomer(this).getEntity_id(), object.toString());
        showProgressDialog(false);
    }

    private void getCustomerDetailToUpdateNotification() {
        UICallback callback = new UICallback() {
            @Override
            public void onResult(boolean success, String data) {
                if (success) {
                    try {
                        JSONObject object = new JSONObject(data);
                        int is_notification_sound = object.getInt("is_notification_sound");
                        if (CustomerController.isLogin(SettingActivity.this)) {
                            Customer customer = CustomerController.getCurrentCustomer(SettingActivity.this);
                            customer.setIs_notification_sound(is_notification_sound);
                            CustomerController.insertOrUpdate(SettingActivity.this, customer);
                        }
                        initData();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {

                }
                hideProgressDialog();
            }
        };
        CustomerExecute.getCustomerDetail(this, callback, CustomerController.getCurrentCustomer(this).getEntity_id());
    }

    // facebook
    OnLogoutListener onLogoutListener = new OnLogoutListener() {
        @Override
        public void onLogout() {
//            showToastInfo("Facebook logged out");
        }
    };
    // facebook
}
