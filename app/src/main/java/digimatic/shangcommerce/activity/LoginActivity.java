package digimatic.shangcommerce.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Profile;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnLogoutListener;
import com.sromku.simple.fb.listeners.OnProfileListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.connection.callback.UICallback;
import digimatic.shangcommerce.connection.threadconnection.execute.CartExecute;
import digimatic.shangcommerce.connection.threadconnection.execute.CustomerExecute;
import digimatic.shangcommerce.connection.threadconnection.execute.WishlistExecute;
import digimatic.shangcommerce.daocontroller.CartDetailController;
import digimatic.shangcommerce.daocontroller.CartItemController;
import digimatic.shangcommerce.daocontroller.CustomerController;
import digimatic.shangcommerce.daocontroller.WishlistController;
import digimatic.shangcommerce.gcm.PlayServicesHelper;
import digimatic.shangcommerce.model.User;
import digimatic.shangcommerce.receiver.KeyboardVisibilityListener;
import digimatic.shangcommerce.receiver.KeyboardVisibilityListener.OnKeyboardVisibilityListener;
import digimatic.shangcommerce.staticfunction.ClickSpan;
import digimatic.shangcommerce.staticfunction.Font;
import greendao.CartDetail;
import greendao.CartItem;
import greendao.Customer;
import greendao.Wishlist;

/**
 * Created by USER on 3/4/2016.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener, OnKeyboardVisibilityListener {

    private int REQUEST_REGISTER = 1234;

    private EditText edtEmail;
    private EditText edtPassword;
    private TextView txtForgotPass;
    private RelativeLayout rltSignin;
    private RelativeLayout rltSigninFb;
    private TextView txtRegister;
    private TextView txtTerm;

    private KeyboardVisibilityListener keyboardVisibilityListener;

    // facebook
    private SimpleFacebook mSimpleFacebook;
    Profile.Properties properties = new Profile.Properties.Builder()
            .add(Profile.Properties.ID)
            .add(Profile.Properties.NAME)
            .add(Profile.Properties.FIRST_NAME)
            .add(Profile.Properties.LAST_NAME)
            .add(Profile.Properties.GENDER)
            .add(Profile.Properties.EMAIL)
            .build();
    // facebook

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sendEvent(getString(R.string.analytic_sign_in_screen), getString(R.string.analytic_in));

        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sendEvent(getString(R.string.analytic_sign_in_screen), getString(R.string.analytic_out));
    }

    private void initView() {
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        txtForgotPass = (TextView) findViewById(R.id.txtForgotPass);
        rltSignin = (RelativeLayout) findViewById(R.id.rltSignin);
        rltSigninFb = (RelativeLayout) findViewById(R.id.rltSigninFb);
        txtRegister = (TextView) findViewById(R.id.txtRegister);
        txtTerm = (TextView) findViewById(R.id.txtTerm);

        txtForgotPass.setOnClickListener(this);
        rltSignin.setOnClickListener(this);
        rltSigninFb.setOnClickListener(this);
        txtRegister.setOnClickListener(this);

        edtEmail.setText(getEmailLogin());
//        edtPassword.setText("qwertyui");

        Font font = new Font(this);
        font.overrideFontsLight(findViewById(R.id.root));
        font.overrideFontsBold(findViewById(R.id.txtSignin));
        font.overrideFontsBold(findViewById(R.id.txtSigninFb));
        font.overrideFontsBold(txtRegister);

        clickify(txtTerm, getString(R.string.term), new ClickSpan.OnClickListener() {
            @Override
            public void onClick() {
                Intent intentTerm = new Intent(LoginActivity.this, TermActivity.class);
                startActivity(intentTerm);
            }
        });

        keyboardVisibilityListener = new KeyboardVisibilityListener();
        keyboardVisibilityListener.setKeyboardListener(this, this,
                R.id.lnlRoot);

        edtPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                int result = actionId & EditorInfo.IME_MASK_ACTION;
                switch (result) {
                    case EditorInfo.IME_ACTION_DONE:
                        login();
                        break;
                }
                return false;
            }
        });
    }

    private void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSimpleFacebook = SimpleFacebook.getInstance(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtForgotPass:
                Intent intentForgot = new Intent(this, ForgotPasswordActivity.class);
                startActivity(intentForgot);
                break;
            case R.id.rltSignin:
                login();
                break;
            case R.id.rltSigninFb:
                mSimpleFacebook.login(onLoginListener);
                showProgressDialog(false);
                break;
            case R.id.txtRegister:
                Intent intent3 = new Intent(this, RegisterActivity.class);
                startActivityForResult(intent3, REQUEST_REGISTER);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        hideProgressDialog();
        mSimpleFacebook.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_REGISTER && resultCode == RESULT_OK) {
            String email = data.getStringExtra("email");
            edtEmail.setText(email);
        }
    }

    private void saveEmailLogin(String email) {
        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email", email);
        editor.commit();
    }

    private String getEmailLogin() {
        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        String email = preferences.getString("email", "");
        return email;
    }

    public void clickify(TextView view, final String clickableText, final ClickSpan.OnClickListener listener) {

        CharSequence text = view.getText();
        String string = text.toString();
        ClickSpan span = new ClickSpan(listener);

        int start = string.indexOf(clickableText);
        int end = start + clickableText.length();
        if (start == -1) return;

        if (text instanceof Spannable) {
            ((Spannable) text).setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ((Spannable) text).setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ((Spannable) text).setSpan(new UnderlineSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            SpannableString s = SpannableString.valueOf(text);
            s.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            s.setSpan(new UnderlineSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            view.setText(s);
        }

        MovementMethod m = view.getMovementMethod();
        if ((m == null) || !(m instanceof LinkMovementMethod)) {
            view.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    @Override
    public void onVisibilityChanged(boolean visible) {
        if (visible) {
            txtTerm.setVisibility(View.GONE);
        } else {
            txtTerm.setVisibility(View.VISIBLE);
        }
    }

    private void login() {
        String username = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        if (username.length() == 0) {
            showToastError(getString(R.string.blank_email));
        } else if (password.length() == 0) {
            showToastError(getString(R.string.blank_pass));
        } else {
            User user = new User(username, password);
            String body = new Gson().toJson(user);

            UICallback callback = new UICallback() {
                @Override
                public void onResult(boolean success, String data) {
                    if (success) {
                        try {
                            JSONObject object = new JSONObject(data);
                            long entity_id = object.getLong("entity_id");
                            String firstname = object.getString("firstname");
                            if (firstname.equalsIgnoreCase("null")) firstname = "";
                            String middlename = object.getString("middlename");
                            if (middlename.equalsIgnoreCase("null")) middlename = "";
                            String lastname = object.getString("lastname");
                            if (lastname.equalsIgnoreCase("null")) lastname = "";
                            String email = object.getString("email");
                            if (email.equalsIgnoreCase("null")) email = "";
                            String dob = object.getString("dob");
                            if (dob.equalsIgnoreCase("null")) dob = "";
                            String picture_url = object.getString("picture_url");
                            if (picture_url.equalsIgnoreCase("null")) picture_url = "";
                            int cart_items_count = 0;
                            int wishlist_items_count = 0;
                            int unread_alerts_count = 0;
                            String shipping_address = object.getString("shipping_address");
                            String billing_address = object.getString("billing_address");
                            int is_notification_sound = object.getInt("is_notification_sound");
                            int orders_count = 0;
                            int reward_points = 0;
                            double evoucher_amount = 0;
                            int log_num = object.getInt("log_num");
                            String card_code = object.getString("card_code");
                            if (card_code.equalsIgnoreCase("null")) card_code = "";

                            Customer customer = new Customer(entity_id, firstname, middlename, lastname, email, dob, "", false, picture_url, cart_items_count, wishlist_items_count, unread_alerts_count, shipping_address, billing_address, is_notification_sound, orders_count, reward_points, evoucher_amount, log_num, card_code);
                            CustomerController.insertOrUpdate(LoginActivity.this, customer);
                            saveEmailLogin(email);

                            getCustomerDetailToGetCountNumbers(customer);
                            sendEvent(getString(R.string.analytic_sign_in), getString(R.string.analytic_action_sign_in));
                            PlayServicesHelper playServicesHelper = new PlayServicesHelper(LoginActivity.this);
                            playServicesHelper.checkRegId();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            showToastError(e.getMessage());
                            hideProgressDialog();
                        }
                    } else {
                        showToastError(data);
                        hideProgressDialog();
                    }
                }
            };
            CustomerExecute.login(this, callback, body);
            showProgressDialog(false);
        }
    }

    private void loginFacebook(final String facebook_id, String firstnme, String lastname, String email, String birthday) {
        JSONObject object = new JSONObject();
        try {
            object.put("facebook_id", facebook_id);
            object.put("firstname", firstnme);
            object.put("lastname", lastname);
            object.put("email", email);
            if (birthday.length() > 0) {
                object.put("dob", birthday);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String body = object.toString();

        UICallback callback = new UICallback() {
            @Override
            public void onResult(boolean success, String data) {
                if (success) {
                    try {
                        JSONObject object = new JSONObject(data);
                        long entity_id = object.getLong("entity_id");
                        String firstname = object.getString("firstname");
                        if (firstname.equalsIgnoreCase("null")) firstname = "";
                        String middlename = object.getString("middlename");
                        if (middlename.equalsIgnoreCase("null")) middlename = "";
                        String lastname = object.getString("lastname");
                        if (lastname.equalsIgnoreCase("null")) lastname = "";
                        String email = object.getString("email");
                        if (email.equalsIgnoreCase("null")) email = "";
                        String dob = object.getString("dob");
                        if (dob.equalsIgnoreCase("null")) dob = "";
                        int cart_items_count = 0;
                        int wishlist_items_count = 0;
                        int unread_alerts_count = 0;
                        String shipping_address = object.getString("shipping_address");
                        String billing_address = object.getString("billing_address");
                        int is_notification_sound = object.getInt("is_notification_sound");
                        int orders_count = 0;
                        int reward_points = 0;
                        double evoucher_amount = 0;
                        int log_num = object.getInt("log_num");
                        String card_code = object.getString("card_code");
                        if (card_code.equalsIgnoreCase("null")) card_code = "";

                        Customer customer = new Customer(entity_id, firstname, middlename, lastname, email, dob, facebook_id, true, "", cart_items_count, wishlist_items_count, unread_alerts_count, shipping_address, billing_address, is_notification_sound, orders_count, reward_points, evoucher_amount, log_num, card_code);
                        CustomerController.insertOrUpdate(LoginActivity.this, customer);
//                        saveEmailLogin(email);

                        getCustomerDetailToGetCountNumbers(customer);
                        sendEvent(getString(R.string.analytic_sign_in), getString(R.string.analytic_action_sign_in));
                        PlayServicesHelper playServicesHelper = new PlayServicesHelper(LoginActivity.this);
                        playServicesHelper.checkRegId();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showToastError(e.getMessage());
                        hideProgressDialog();
                    }
                } else {
                    showToastError(data);
                    hideProgressDialog();
                }
            }
        };
        CustomerExecute.loginFacebook(this, callback, body);
        showProgressDialog(false);
    }

    private void getCustomerDetailToGetCountNumbers(final Customer customer) {
        UICallback callback = new UICallback() {
            @Override
            public void onResult(boolean success, String data) {
                if (success) {
                    try {
                        JSONObject object = new JSONObject(data);
                        int cart_items_count = object.getInt("cart_items_count");
                        int unread_alerts_count = object.getInt("unread_alerts_count");
                        int orders_count = object.getInt("orders_count");
//                        int reward_points = object.getInt("reward_points");
//                        int evoucher_amount = object.getInt("evoucher_amount");
                        if (CustomerController.isLogin(LoginActivity.this)) {
                            customer.setCart_items_count(cart_items_count);
                            customer.setUnread_alerts_count(unread_alerts_count);
                            customer.setOrders_count(orders_count);
                            CustomerController.insertOrUpdate(LoginActivity.this, customer);
                        }
                        getWishlist(customer);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        hideProgressDialog();
                        showToastError(data);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("just_login", true);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    hideProgressDialog();
                    showToastError(data);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("just_login", true);
                    startActivity(intent);
                    finish();
                }
            }
        };
        CustomerExecute.getCustomerCounter(this, callback, CustomerController.getCurrentCustomer(this).getEntity_id());
    }

    private void getWishlist(final Customer customer) {
        UICallback callback = new UICallback() {
            @Override
            public void onResult(boolean success, String data) {
                if (success) {
                    try {
                        JSONArray array = new JSONArray(data);
                        WishlistController.clearAll(LoginActivity.this);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            long wishlist_item_id = object.getLong("wishlist_item_id");
                            long product_id = object.getLong("product_id");
                            String name = object.getString("name");
                            String sku = object.getString("sku");
                            String manufacturer = object.optString("manufacturer", "");
                            String price = object.getString("price");
                            if (price.equalsIgnoreCase("null")) price = "";
                            float price_f = 0;
                            if (price.length() > 0) {
                                price_f = Float.parseFloat(price);
                            }
                            String image_url = object.getString("image_url");
                            String added_at = object.getString("added_at");
                            String type_id = object.getString("type_id");

                            WishlistController.insertOrUpdate(LoginActivity.this, new Wishlist(wishlist_item_id, product_id, name, sku, manufacturer, price_f, image_url, added_at, type_id));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showToastError(e.getMessage());
                    }
                } else {
                    showToastError(data);
                }
                hideProgressDialog();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("just_login", true);
                startActivity(intent);
                finish();
            }
        };
        WishlistExecute.getByCustomerId(this, callback, customer.getEntity_id());
    }

    // facebook
    OnLoginListener onLoginListener = new OnLoginListener() {

        @Override
        public void onLogin(String accessToken, List<Permission> acceptedPermissions, List<Permission> declinedPermissions) {
            // change the state of the button or do whatever you want
            mSimpleFacebook.getProfile(properties, onProfileListener);
        }

        @Override
        public void onCancel() {
            // user canceled the dialog
        }

        @Override
        public void onFail(String reason) {
            // failed to login
        }

        @Override
        public void onException(Throwable throwable) {
            // exception from facebook
        }

    };

    OnProfileListener onProfileListener = new OnProfileListener() {
        @Override
        public void onComplete(Profile profile) {
            String id = profile.getId();
            String first = profile.getFirstName();
            String last = profile.getLastName();
            String email = profile.getEmail();
            String get_birthday = profile.getBirthday();
            String birthday = "";
            if (get_birthday != null) {
                birthday = get_birthday; // format again dd/mm/yy
            }
            loginFacebook(id, first, last, email, birthday);
        }

        @Override
        public void onFail(String reason) {
            super.onFail(reason);
            hideProgressDialog();
        }

        @Override
        public void onException(Throwable throwable) {
            super.onException(throwable);
            hideProgressDialog();
        }

        @Override
        public void onThinking() {
            super.onThinking();
            hideProgressDialog();
        }

        /*
     * You can override other methods here:
     * onThinking(), onFail(String reason), onException(Throwable throwable)
     */
    };
    // facebook
}
