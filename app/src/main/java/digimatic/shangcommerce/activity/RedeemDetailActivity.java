package digimatic.shangcommerce.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.connection.callback.UICallback;
import digimatic.shangcommerce.connection.threadconnection.execute.CRMExecute;
import digimatic.shangcommerce.daocontroller.CustomerController;
import digimatic.shangcommerce.model.ItemListReward;
import digimatic.shangcommerce.staticfunction.Font;
import greendao.Customer;

/**
 * Created by USER on 4/5/2016.
 */
public class RedeemDetailActivity extends BaseActivity implements View.OnClickListener {

    private Font font;

    private RelativeLayout rltBack;
    private RelativeLayout rltSearch;
    private RelativeLayout rltCart;
    private TextView txtCart;
    private TextView txtTitle;

    // banner
    private ImageView imageViewBanner;

    private TextView txtDesc;
    private LinearLayout lnlShowmore;
    private View viewDesc;

    private TextView txtRedeemNow;

    private RelativeLayout rltLoading;

    private ItemListReward itemListReward;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem_detail);

        options = new DisplayImageOptions.Builder().cacheInMemory(false)
                .showImageForEmptyUri(R.drawable.noimg)
                .showImageOnLoading(R.drawable.noimg)
                .cacheOnDisk(true).build();

        initView();
        initData();
    }

    private void initView() {
        rltBack = (RelativeLayout) findViewById(R.id.rltBack);
        rltSearch = (RelativeLayout) findViewById(R.id.rltSearch);
        rltCart = (RelativeLayout) findViewById(R.id.rltCart);
        txtCart = (TextView) findViewById(R.id.txtCart);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtRedeemNow = (TextView) findViewById(R.id.txtRedeemNow);

        rltBack.setOnClickListener(this);
        rltSearch.setOnClickListener(this);
        rltCart.setOnClickListener(this);
        txtRedeemNow.setOnClickListener(this);

        imageViewBanner = (ImageView) findViewById(R.id.imageViewBanner);

        txtDesc = (TextView) findViewById(R.id.txtDesc);
        lnlShowmore = (LinearLayout) findViewById(R.id.lnlShowmore);
        viewDesc = findViewById(R.id.viewDesc);

        lnlShowmore.setOnClickListener(this);

        rltLoading = (RelativeLayout) findViewById(R.id.rltLoading);

        rltLoading.setVisibility(View.GONE);

        font = new Font(this);
        font.overrideFontsLight(findViewById(R.id.root));
        font.overrideFontsBold(txtTitle);

        font.overrideFontsBold(findViewById(R.id.txtDescLabel));
        font.overrideFontsBold(txtRedeemNow);

        lnlShowmore.setVisibility(View.GONE);
        viewDesc.setVisibility(View.GONE);
    }

    private void initData() {
        if (getIntent().hasExtra("ItemList")) {
            String ItemListString = getIntent().getStringExtra("ItemList");
            try {
                JSONObject ItemList = new JSONObject(ItemListString);
                String RedeemRuleID = ItemList.getString("RedeemRuleID");
                String ItemCode = ItemList.getString("ItemCode");
                String ItemName = ItemList.getString("ItemName");
                String ItemDescription = ItemList.getString("ItemDescription");
                String ItemURL = ItemList.getString("ItemURL");
                String ItemImageURL = ItemList.getString("ItemImageURL");
                int MinRewardtoDeduct = ItemList.getInt("MinRewardtoDeduct");
                int MaxRewardtoDeduct = ItemList.getInt("MaxRewardtoDeduct");
                String ActiveFrom = ItemList.getString("ActiveFrom");
                String ActiveTo = ItemList.getString("ActiveTo");
                int MaxRedeem = ItemList.getInt("MaxRedeem");

                itemListReward = new ItemListReward(RedeemRuleID, ItemCode, ItemName, ItemDescription, ItemURL, ItemImageURL, MinRewardtoDeduct, MaxRewardtoDeduct, ActiveFrom, ActiveTo, MaxRedeem);

                getProductDetail();
            } catch (JSONException e) {
                e.printStackTrace();
                finish();
            }
        } else {
            finish();
        }
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
                if (CustomerController.isLogin(this)) {
                    Intent intentCart = new Intent(this, CartActivity.class);
                    startActivity(intentCart);
                } else {
                    Intent intentLogin = new Intent(this, LoginActivity.class);
                    startActivity(intentLogin);
                }
                break;
            case R.id.lnlShowmore:
                txtDesc.setMaxLines(100);
                lnlShowmore.setVisibility(View.GONE);
                viewDesc.setVisibility(View.GONE);
                break;
            case R.id.txtRedeemNow:
                GiftRedeem();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupCart();
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

    private void getProductDetail() {
        txtTitle.setText(itemListReward.getItemName());
        imageLoader.displayImage(itemListReward.getItemImageURL(), imageViewBanner, options);

        txtDesc.setText(itemListReward.getItemDescription());
        txtDesc.setMaxLines(5);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (txtDesc.getLineCount() <= 5) {
                    lnlShowmore.setVisibility(View.GONE);
                    viewDesc.setVisibility(View.GONE);
                } else {
                    lnlShowmore.setVisibility(View.VISIBLE);
                    viewDesc.setVisibility(View.VISIBLE);
                }
            }
        }, 100);
    }

    private void GiftRedeem() {
        JSONObject object = new JSONObject();
        try {
            JSONObject Shared = new JSONObject();
            JSONObject Data = new JSONObject();

            Shared.put("Type", "GiftRedeem");
            Shared.put("AppCode", "shangcart");
            Shared.put("UserName", "app");
            Shared.put("Password", "123456");

            Data.put("CardCode", CustomerController.getCurrentCustomer(this).getCard_code());
            Data.put("RedeemRuleID", itemListReward.getRedeemRuleID());
            Data.put("ItemCode", itemListReward.getItemCode());
            Data.put("RedeemQuantity", "1");

            object.put("Shared", Shared);
            object.put("Data", Data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        UICallback callback = new UICallback() {
            @Override
            public void onResult(boolean success, String data) {
                if (success) {
                    showToastOk(getString(R.string.redeem_item_success));
                    CARDENQUIRY();
                } else {
                    showToastError(data);
                    hideProgressDialog();
                }
            }
        };
        CRMExecute.GiftRedeem(this, callback, object.toString());
        showProgressDialog(false);
    }

    private void CARDENQUIRY() {
        JSONObject object = new JSONObject();
        try {
            JSONObject Shared = new JSONObject();
            JSONObject Data = new JSONObject();

            Shared.put("Type", "CARDENQUIRY");
            Shared.put("APPcode", "IOS");
            Shared.put("StoreCode", "Default");
            Shared.put("POSID", "Default");
            Shared.put("CashierCode", "cs001");
            Shared.put("UserName", "app");
            Shared.put("Password", "123456");

            Data.put("CardCode", CustomerController.getCurrentCustomer(this).getCard_code());
            Data.put("MobileNo", "");
            Data.put("ID", "");
            Data.put("Passport", "");

            object.put("Shared", Shared);
            object.put("Data", Data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        UICallback callback = new UICallback() {
            @Override
            public void onResult(boolean success, String data) {
                if (success) {
                    try {
                        JSONObject Data = new JSONObject(data);
                        JSONArray CardListArray = Data.getJSONArray("CardList");
                        if (CardListArray.length() > 0) {
                            JSONObject CardList = CardListArray.getJSONObject(0);
                            JSONArray RewardListArray = CardList.getJSONArray("RewardList");
                            int reward_points = 0;
                            if (RewardListArray.length() > 0) {
                                JSONObject RewardList = RewardListArray.getJSONObject(0);
                                int Value = RewardList.getInt("Value");
                                reward_points = Value;
                            }
                            JSONArray VoucherListArray = CardList.getJSONArray("VoucherList");
                            double eVoucherTotal = 0;
                            for (int i = 0; i < VoucherListArray.length(); i++) {
                                JSONObject VoucherList = VoucherListArray.getJSONObject(i);
                                double Amount = VoucherList.getDouble("Amount");
                                eVoucherTotal += Amount;
                            }
                            Customer customer = CustomerController.getCurrentCustomer(RedeemDetailActivity.this);
                            customer.setReward_points(reward_points);
                            customer.setEvoucher_amount(eVoucherTotal);
                            CustomerController.insertOrUpdate(RedeemDetailActivity.this, customer);
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
        CRMExecute.CARDENQUIRY(this, callback, object.toString());
    }
}
