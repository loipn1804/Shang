package digimatic.shangcommerce.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.connection.callback.UICallback;
import digimatic.shangcommerce.connection.threadconnection.execute.CustomerExecute;
import digimatic.shangcommerce.daocontroller.CountryController;
import digimatic.shangcommerce.daocontroller.CustomerController;
import digimatic.shangcommerce.fragment.TransactionHistoryFragment;
import digimatic.shangcommerce.staticfunction.Font;
import greendao.Country;
import greendao.Customer;

/**
 * Created by USER on 3/7/2016.
 */
public class AccountActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rltBack;
    private RelativeLayout rltSearch;
    private RelativeLayout rltCart;
    private TextView txtCart;
    private TextView txtTitle;
    private LinearLayout lnlFragment;
    private TextView txtChangeProfile;
    private TextView txtPTS;
    private TextView txtEVoucher;

    private ImageView imvAvatar;
    private TextView txtUsername;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        sendEvent(getString(R.string.analytic_account), getString(R.string.analytic_in));

        options = new DisplayImageOptions.Builder()
                .displayer(new RoundedBitmapDisplayer(500))
                .showImageForEmptyUri(R.drawable.avatar_default)
                .showImageOnLoading(R.drawable.avatar_default)
                .cacheOnDisk(true).build();

        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sendEvent(getString(R.string.analytic_account), getString(R.string.analytic_out));
    }

    private void initView() {
        rltBack = (RelativeLayout) findViewById(R.id.rltBack);
        rltSearch = (RelativeLayout) findViewById(R.id.rltSearch);
        rltCart = (RelativeLayout) findViewById(R.id.rltCart);
        txtCart = (TextView) findViewById(R.id.txtCart);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        lnlFragment = (LinearLayout) findViewById(R.id.lnlFragment);
        txtChangeProfile = (TextView) findViewById(R.id.txtChangeProfile);
        imvAvatar = (ImageView) findViewById(R.id.imvAvatar);
        txtUsername = (TextView) findViewById(R.id.txtUsername);
        txtPTS = (TextView) findViewById(R.id.txtPTS);
        txtEVoucher = (TextView) findViewById(R.id.txtEVoucher);

        rltBack.setOnClickListener(this);
        rltSearch.setOnClickListener(this);
        rltCart.setOnClickListener(this);
        txtChangeProfile.setOnClickListener(this);

        Font font = new Font(this);
        font.overrideFontsLight(findViewById(R.id.root));
        font.overrideFontsBold(txtTitle);
        font.overrideFontsBold(findViewById(R.id.txtUsername));
        font.overrideFontsBold(txtChangeProfile);
        font.overrideFontsBold(findViewById(R.id.txtMyOrders));
        font.overrideFontsBold(txtPTS);
        font.overrideFontsBold(txtEVoucher);
        font.overrideFontsBold(findViewById(R.id.txtPTSLabel));
        font.overrideFontsBold(findViewById(R.id.txtEVoucherLabel));
    }

    private void initData() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        TransactionHistoryFragment transactionHistoryFragment = new TransactionHistoryFragment();
        fragmentTransaction.add(R.id.lnlFragment, transactionHistoryFragment, "transactionHistoryFragment");
        fragmentTransaction.commit();

        Customer customer = CustomerController.getCurrentCustomer(this);
        txtUsername.setText(customer.getFirstname() + " " + customer.getLastname());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltBack:
                finish();
                break;
            case R.id.rltSearch:
                Intent intentSearch = new Intent(this, SearchActivity.class);
                startActivity(intentSearch);
                break;
            case R.id.rltCart:
                Intent intentCart = new Intent(this, CartActivity.class);
                startActivity(intentCart);
                break;
            case R.id.txtChangeProfile:
                Intent intentProfileEdit = new Intent(this, AccountEditActivity.class);
                startActivity(intentProfileEdit);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (CustomerController.isLogin(this)) {
            Customer customer = CustomerController.getCurrentCustomer(this);
            String full = customer.getFirstname();
            String middle = customer.getMiddlename();
            if (middle.length() > 0) {
                full += " " + middle;
            }
            String last = customer.getLastname();
            if (last.length() > 0) {
                full += " " + last;
            }
            txtUsername.setText(full);
            if (customer.getIs_login_fb()) {
                imageLoader.displayImage("https://graph.facebook.com/" + customer.getFacebook_id() + "/picture?height=200&width=200", imvAvatar, options);
            } else {
                imageLoader.displayImage(customer.getPicture_url(), imvAvatar, options);
            }

            setupCart();

            txtPTS.setText(customer.getReward_points() + "");
            BigDecimal b_eVoucherTotal = new BigDecimal(customer.getEvoucher_amount()).setScale(2, BigDecimal.ROUND_HALF_UP);
            txtEVoucher.setText("$" + b_eVoucherTotal);
        } else {
            finish();
        }
    }

    private void setupCart() {
        if (CustomerController.isLogin(this)) {
            txtCart.setVisibility(View.VISIBLE);
            Customer customer = CustomerController.getCurrentCustomer(this);
            txtCart.setText(customer.getCart_items_count() + "");
            if (customer.getCart_items_count() > 99) {
                txtCart.setText("99+");
            }
        } else {
            txtCart.setVisibility(View.GONE);
        }
    }
}
