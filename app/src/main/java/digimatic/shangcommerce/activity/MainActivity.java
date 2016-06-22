package digimatic.shangcommerce.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.adapter.CategoryPagerAdapter;
import digimatic.shangcommerce.adapter.ImageBannerShopAdapter;
import digimatic.shangcommerce.connection.callback.UICallback;
import digimatic.shangcommerce.connection.threadconnection.execute.BannerExecute;
import digimatic.shangcommerce.connection.threadconnection.execute.CRMExecute;
import digimatic.shangcommerce.connection.threadconnection.execute.CartExecute;
import digimatic.shangcommerce.connection.threadconnection.execute.CategoryExecute;
import digimatic.shangcommerce.connection.threadconnection.execute.CustomerExecute;
import digimatic.shangcommerce.connection.threadconnection.execute.FollowingExecute;
import digimatic.shangcommerce.daocontroller.CartDetailController;
import digimatic.shangcommerce.daocontroller.CartItemController;
import digimatic.shangcommerce.daocontroller.CategoryController;
import digimatic.shangcommerce.daocontroller.CustomerController;
import digimatic.shangcommerce.model.Following;
import digimatic.shangcommerce.staticfunction.Font;
import greendao.CartDetail;
import greendao.CartItem;
import greendao.Category;
import greendao.Customer;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private DrawerLayout drawerLayout;

    private RelativeLayout rltMenu;
    private RelativeLayout rltSearch;
    private RelativeLayout rltCart;
    private TextView txtCart;
    private TextView txtTitle;

    // banner
    private ViewPager pager;
    private CirclePageIndicator indicator;
    private ImageBannerShopAdapter imageBannerShopAdapter;
    private List<String> listBanner;
    private CountDownTimer timer;

    // voucherItemList
    private ViewPager viewPager;
    private PagerSlidingTabStrip tabStrip;
    private CategoryPagerAdapter categoryPagerAdapter;
    private List<Category> listCategory;

    private ImageView imvAvatar;
    private TextView txtUsername;
    private TextView txtTransactionHistory;
    private TextView txtPTS;
    private TextView txtEVoucher;

    // menu
    private TextView txtMyAccount;
    private LinearLayout lnlAlerts;
    private TextView txtAlerts;
    private TextView txtAlertsNumber;
    private LinearLayout lnlCartMenu;
    private TextView txtCartMenu;
    private TextView txtCartMenuNumber;
    private TextView txtWishlist;
    private TextView txtShop;
    private TextView txtFollowing;
    private TextView txtAbout;
    private TextView txtTerm;
    private TextView txtFeedback;
    private TextView txtFAQ;
    private TextView txtReward;
    private TextView txtSetting;

    private LinearLayout lnlAccount;

    private LinearLayout lnlFollowing;

    private Font font;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendEvent(getString(R.string.analytic_shipping), getString(R.string.analytic_in));

        options = new DisplayImageOptions.Builder()
                .displayer(new RoundedBitmapDisplayer(500))
                .showImageForEmptyUri(R.drawable.avatar_default)
                .showImageOnLoading(R.drawable.avatar_default)
                .cacheOnDisk(true).build();

        if (getIntent().hasExtra("just_login")) {
            getListFollowing();
        }

        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer = null;
        }
        sendEvent(getString(R.string.analytic_shipping), getString(R.string.analytic_out));
    }

    private void initView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        rltMenu = (RelativeLayout) findViewById(R.id.rltMenu);
        rltSearch = (RelativeLayout) findViewById(R.id.rltSearch);
        rltCart = (RelativeLayout) findViewById(R.id.rltCart);
        txtCart = (TextView) findViewById(R.id.txtCart);
        txtTitle = (TextView) findViewById(R.id.txtTitle);

        pager = (ViewPager) findViewById(R.id.pager);
        indicator = (CirclePageIndicator) findViewById(R.id.indicator);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);

        imvAvatar = (ImageView) findViewById(R.id.imvAvatar);
        txtUsername = (TextView) findViewById(R.id.txtUsername);
        txtTransactionHistory = (TextView) findViewById(R.id.txtTransactionHistory);
        txtPTS = (TextView) findViewById(R.id.txtPTS);
        txtEVoucher = (TextView) findViewById(R.id.txtEVoucher);

        lnlFollowing = (LinearLayout) findViewById(R.id.lnlFollowing);

        rltMenu.setOnClickListener(this);
        rltSearch.setOnClickListener(this);
        rltCart.setOnClickListener(this);
        imvAvatar.setOnClickListener(this);
        txtUsername.setOnClickListener(this);
        txtTransactionHistory.setOnClickListener(this);
        lnlFollowing.setOnClickListener(this);

        // menu
        txtMyAccount = (TextView) findViewById(R.id.txtMyAccount);
        lnlAlerts = (LinearLayout) findViewById(R.id.lnlAlerts);
        txtAlerts = (TextView) findViewById(R.id.txtAlerts);
        txtAlertsNumber = (TextView) findViewById(R.id.txtAlertsNumber);
        lnlCartMenu = (LinearLayout) findViewById(R.id.lnlCartMenu);
        txtCartMenu = (TextView) findViewById(R.id.txtCartMenu);
        txtCartMenuNumber = (TextView) findViewById(R.id.txtCartMenuNumber);
        txtWishlist = (TextView) findViewById(R.id.txtWishlist);
        txtShop = (TextView) findViewById(R.id.txtShop);
        txtFollowing = (TextView) findViewById(R.id.txtFollowing);
        txtAbout = (TextView) findViewById(R.id.txtAbout);
        txtTerm = (TextView) findViewById(R.id.txtTerm);
        txtFeedback = (TextView) findViewById(R.id.txtFeedback);
        txtFAQ = (TextView) findViewById(R.id.txtFAQ);
        txtReward = (TextView) findViewById(R.id.txtReward);
        txtSetting = (TextView) findViewById(R.id.txtSetting);

        txtMyAccount.setOnClickListener(this);
        lnlAlerts.setOnClickListener(this);
        lnlCartMenu.setOnClickListener(this);
        txtWishlist.setOnClickListener(this);
        txtShop.setOnClickListener(this);
        txtFollowing.setOnClickListener(this);
        txtAbout.setOnClickListener(this);
        txtTerm.setOnClickListener(this);
        txtFeedback.setOnClickListener(this);
        txtFAQ.setOnClickListener(this);
        txtReward.setOnClickListener(this);
        txtSetting.setOnClickListener(this);

        lnlAccount = (LinearLayout) findViewById(R.id.lnlAccount);

        font = new Font(this);
        font.overrideFontsLight(findViewById(R.id.root));
        font.overrideFontsBold(txtTitle);
        font.overrideFontsBold(txtUsername);
        font.overrideFontsBold(txtTransactionHistory);
        font.overrideFontsBold(txtMyAccount);
        font.overrideFontsBold(txtAlerts);
        font.overrideFontsBold(txtCartMenu);
        font.overrideFontsBold(txtWishlist);
        font.overrideFontsBold(txtShop);
        font.overrideFontsBold(txtFollowing);
        font.overrideFontsBold(txtAbout);
        font.overrideFontsBold(txtTerm);
        font.overrideFontsBold(txtFeedback);
        font.overrideFontsBold(txtFAQ);
        font.overrideFontsBold(txtAlertsNumber);
        font.overrideFontsBold(txtCartMenuNumber);
        font.overrideFontsBold(txtReward);
        font.overrideFontsBold(txtSetting);
        font.overrideFontsBold(txtPTS);
        font.overrideFontsBold(txtEVoucher);
        font.overrideFontsBold(findViewById(R.id.txtPTSLabel));
        font.overrideFontsBold(findViewById(R.id.txtEVoucherLabel));
    }

    private void initData() {
        listBanner = new ArrayList<>();
        getBanners();
        getCategories();
        if (CustomerController.isLogin(this)) {
            getCustomerDetailToGetCountNumbers();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltMenu:
                drawerLayout.openDrawer(Gravity.LEFT);
                break;
            case R.id.rltSearch:
                Intent intentSearch = new Intent(this, SearchActivity.class);
                startActivity(intentSearch);
                break;
            case R.id.rltCart:
                if (CustomerController.isLogin(this)) {
                    Intent intentCart1 = new Intent(this, CartActivity.class);
                    startActivity(intentCart1);
                } else {
                    Intent intentLogin = new Intent(this, LoginActivity.class);
                    startActivity(intentLogin);
                    drawerLayout.closeDrawers();
                }
                break;
            case R.id.imvAvatar:
                Intent intentAccount1 = new Intent(this, AccountActivity.class);
                startActivity(intentAccount1);
                break;
            case R.id.txtUsername:
                Intent intentAccount2 = new Intent(this, AccountActivity.class);
                startActivity(intentAccount2);
                break;
            case R.id.txtTransactionHistory:
                Intent intentTransactionHistory = new Intent(this, TransactionHistoryActivity.class);
                startActivity(intentTransactionHistory);
                break;
            case R.id.txtMyAccount:
                if (CustomerController.isLogin(this)) {
                    Intent intentAccount3 = new Intent(this, AccountActivity.class);
                    startActivity(intentAccount3);
                } else {
                    Intent intentLogin = new Intent(this, LoginActivity.class);
                    startActivity(intentLogin);
                    drawerLayout.closeDrawers();
                }
                break;
            case R.id.lnlAlerts:
                Intent intentAlert = new Intent(this, AlertActivity.class);
                startActivity(intentAlert);
                break;
            case R.id.lnlCartMenu:
                Intent intentCart2 = new Intent(this, CartActivity.class);
                startActivity(intentCart2);
                break;
            case R.id.txtWishlist:
                Intent intentWishlist = new Intent(this, WishlistActivity.class);
                startActivity(intentWishlist);
                break;
            case R.id.txtShop:
                drawerLayout.closeDrawers();
                break;
            case R.id.txtFollowing:
                Intent intentFollowing = new Intent(this, FollowingActivity.class);
                startActivity(intentFollowing);
                drawerLayout.closeDrawers();
                break;
            case R.id.txtAbout:
                Intent intentAbout = new Intent(this, AboutActivity.class);
                startActivity(intentAbout);
                break;
            case R.id.txtTerm:
                Intent intentTerm = new Intent(this, TermActivity.class);
                startActivity(intentTerm);
                break;
            case R.id.txtFeedback:
                Intent intentFeedback = new Intent(this, FeedbackActivity.class);
                startActivity(intentFeedback);
                break;
            case R.id.txtSetting:
                Intent intentSetting = new Intent(this, SettingActivity.class);
                startActivity(intentSetting);
                break;
            case R.id.txtFAQ:
                Intent intentFaq = new Intent(this, FaqActivity.class);
                startActivity(intentFaq);
                break;
            case R.id.txtReward:
                Intent intentReward = new Intent(this, RewardActivity.class);
                startActivity(intentReward);
                break;
            case R.id.lnlFollowing:
                Intent intentFollowing2 = new Intent(this, FollowingActivity.class);
                startActivity(intentFollowing2);
                drawerLayout.closeDrawers();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (timer != null) {
            timer.start();
        }
        if (CustomerController.isLogin(this)) {
            lnlAccount.setVisibility(View.VISIBLE);
            txtWishlist.setVisibility(View.VISIBLE);
            lnlAlerts.setVisibility(View.VISIBLE);
            txtMyAccount.setText("MY ACCOUNT");
            txtReward.setVisibility(View.VISIBLE);
            txtFollowing.setVisibility(View.VISIBLE);

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
            txtAlertsNumber.setText(customer.getUnread_alerts_count() + "");
            if (customer.getUnread_alerts_count() == 0) {
                txtAlertsNumber.setVisibility(View.GONE);
            } else {
                txtAlertsNumber.setVisibility(View.VISIBLE);
            }

            setupCart();

            txtPTS.setText(customer.getReward_points() + "");
            BigDecimal b_eVoucherTotal = new BigDecimal(customer.getEvoucher_amount()).setScale(2, BigDecimal.ROUND_HALF_UP);
            txtEVoucher.setText("$" + b_eVoucherTotal);
        } else {
            lnlAccount.setVisibility(View.GONE);
            txtWishlist.setVisibility(View.GONE);
            lnlAlerts.setVisibility(View.GONE);
            txtMyAccount.setText("SIGN IN");
            txtReward.setVisibility(View.GONE);
            txtFollowing.setVisibility(View.GONE);
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (timer != null) {
            timer.cancel();
        }
    }

    private void setupCart() {
        if (CustomerController.isLogin(this)) {
            txtCart.setVisibility(View.VISIBLE);
            lnlCartMenu.setVisibility(View.VISIBLE);
            Customer customer = CustomerController.getCurrentCustomer(this);
            txtCart.setText(customer.getCart_items_count() + "");
            txtCartMenuNumber.setText(customer.getCart_items_count() + "");
            if (customer.getCart_items_count() > 99) {
                txtCart.setText("99+");
                txtCartMenuNumber.setText("99+");
            }
            if (customer.getCart_items_count() == 0) {
                txtCartMenuNumber.setVisibility(View.GONE);
            } else {
                txtCartMenuNumber.setVisibility(View.VISIBLE);
            }
        } else {
            txtCart.setVisibility(View.GONE);
            lnlCartMenu.setVisibility(View.GONE);
        }
    }

    private void getCategories() {
        UICallback callback = new UICallback() {
            @Override
            public void onResult(boolean success, String data) {
                if (success) {
                    try {
                        JSONArray array = new JSONArray(data);
                        CategoryController.clearAll(MainActivity.this);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            long entity_id = object.getLong("entity_id");
                            String name = object.getString("name");
                            int position = object.getInt("position");
                            String image_url = object.getString("image_url");
                            if (image_url.equalsIgnoreCase("null")) image_url = "";
                            String thumbnail_url = object.getString("thumbnail_url");
                            if (thumbnail_url.equalsIgnoreCase("null")) thumbnail_url = "";
                            int product_count = object.getInt("product_count");

                            CategoryController.insertOrUpdate(MainActivity.this, new Category(entity_id, name, position, image_url, thumbnail_url, product_count));
                            setCategoryPagerAdapter();
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
        CategoryExecute.getCategories(this, callback);
        showProgressDialog(false);
    }

    private void getBanners() {
        UICallback callback = new UICallback() {
            @Override
            public void onResult(boolean success, String data) {
                if (success) {
                    try {
                        JSONArray array = new JSONArray(data);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            String image_url = object.getString("image_url");

                            listBanner.add(image_url);
                        }
                        setListBanner();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showToastError(e.getMessage());
                    }
                } else {
                    showToastError(data);
                }
            }
        };
        BannerExecute.getAll(this, callback);
    }

    private void slideBannerPager(final int page) {
        if (timer != null) {
            timer.cancel();
        }
        timer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                pager.setCurrentItem(page, true);
            }
        };
        timer.start();
    }

    private void getCart(Customer customer) {
        final UICallback callback = new UICallback() {
            @Override
            public void onResult(boolean success, String data) {
                if (success) {
                    try {
                        JSONObject cart = new JSONObject(data);
                        long entity_id = cart.optLong("entity_id", 0);
                        if (entity_id > 0) {
                            int items_count = cart.getInt("items_count");
                            int items_qty = cart.getInt("items_qty");
                            float subtotal = Float.parseFloat(cart.optString("subtotal", "0"));
                            float grand_total = Float.parseFloat(cart.optString("grand_total", "0"));
                            String coupon_code = cart.optString("coupon_code", "");
                            if (coupon_code.equalsIgnoreCase("null")) coupon_code = "";
                            long customer_id = cart.getLong("customer_id");
                            String created_at = cart.getString("created_at");
                            String updated_at = cart.getString("updated_at");
                            String tax_amount_cart = cart.getString("tax_amount");
                            if (tax_amount_cart.equalsIgnoreCase("null")) tax_amount_cart = "0";
                            float f_tax_amount_cart = Float.parseFloat(tax_amount_cart);
                            String shipping_amount = cart.getString("shipping_amount");
                            if (shipping_amount.equalsIgnoreCase("null")) shipping_amount = "0";
                            float f_shipping_amount = Float.parseFloat(shipping_amount);
                            String discount_amount_b = cart.getString("discount_amount");
                            if (discount_amount_b.equalsIgnoreCase("null")) discount_amount_b = "0";
                            float f_discount_amount_b = Math.abs(Float.parseFloat(discount_amount_b));
                            String use_reward_points = cart.getString("use_reward_points");
                            if (use_reward_points.equalsIgnoreCase("null")) use_reward_points = "0";
                            float f_use_reward_points = Math.abs(Float.parseFloat(use_reward_points));
                            String discount_description = cart.getString("discount_description");
                            String voucher_list = cart.getString("voucher_list");

                            CartDetailController.clearAll(MainActivity.this);
                            CartDetailController.insertOrUpdate(MainActivity.this, new CartDetail(entity_id, items_count, items_qty, subtotal, grand_total, coupon_code, customer_id, created_at, updated_at, f_tax_amount_cart, f_shipping_amount, f_discount_amount_b, f_use_reward_points, discount_description, voucher_list));

                            JSONArray array = cart.getJSONArray("cart_items");
                            CartItemController.clearAll(MainActivity.this);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                long item_id = object.getLong("item_id");
                                long product_id = object.getLong("product_id");
                                String name = object.getString("name");
                                String sku = object.getString("sku");
                                String manufacturer = object.optString("manufacturer", "");
                                float price = Float.parseFloat(object.optString("price", "0"));
                                String image_url = object.getString("image_url");
                                int qty = object.getInt("qty");
                                float row_total = Float.parseFloat(object.optString("row_total", "0"));
                                String options = object.getString("options");
                                float discount_amount = Float.parseFloat(object.optString("discount_amount", "0"));
                                float tax_amount = Float.parseFloat(object.optString("tax_amount", "0"));
                                String type_id = object.getString("type_id");

                                CartItemController.insertOrUpdate(MainActivity.this, new CartItem(item_id, product_id, name, sku, manufacturer, price, image_url, qty, row_total, options, discount_amount, tax_amount, type_id));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showToastError(e.getMessage());
                    }
                } else {
                    showToastError(data);
                }
                setupCart();
            }
        };
        CartExecute.getCart(this, callback, customer.getEntity_id());
    }

    private void getCustomerDetailToGetCountNumbers() {
        UICallback callback = new UICallback() {
            @Override
            public void onResult(boolean success, String data) {
                if (success) {
                    try {
                        JSONObject object = new JSONObject(data);
                        int cart_items_count = object.getInt("cart_items_count");
                        int unread_alerts_count = object.getInt("unread_alerts_count");
                        if (CustomerController.isLogin(MainActivity.this)) {
                            Customer customer = CustomerController.getCurrentCustomer(MainActivity.this);
                            customer.setCart_items_count(cart_items_count);
                            customer.setUnread_alerts_count(unread_alerts_count);
                            CustomerController.insertOrUpdate(MainActivity.this, customer);
                        }
                        txtAlertsNumber.setText(unread_alerts_count + "");
                        setupCart();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showToastError(e.getMessage());
                    }
                } else {
                    showToastError(data);
                }
                hideProgressDialog();
                CARDENQUIRY();
            }
        };
        CustomerExecute.getCustomerCounter(this, callback, CustomerController.getCurrentCustomer(this).getEntity_id());
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
                                txtPTS.setText(Value + "");
                            }
                            JSONArray VoucherListArray = CardList.getJSONArray("VoucherList");
                            double eVoucherTotal = 0;
                            for (int i = 0; i < VoucherListArray.length(); i++) {
                                JSONObject VoucherList = VoucherListArray.getJSONObject(i);
                                double Amount = VoucherList.getDouble("Amount");
                                eVoucherTotal += Amount;
                            }
                            BigDecimal b_eVoucherTotal = new BigDecimal(eVoucherTotal).setScale(2, BigDecimal.ROUND_HALF_UP);
                            txtEVoucher.setText("$" + b_eVoucherTotal);
                            Customer customer = CustomerController.getCurrentCustomer(MainActivity.this);
                            customer.setReward_points(reward_points);
                            customer.setEvoucher_amount(eVoucherTotal);
                            CustomerController.insertOrUpdate(MainActivity.this, customer);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showToastError(e.getMessage());
                    }
                } else {
                    showToastError(data);
                }
            }
        };
        CRMExecute.CARDENQUIRY(this, callback, object.toString());
    }

    private void getListFollowing() {
        UICallback callback = new UICallback() {
            @Override
            public void onResult(boolean success, String data) {
                List<Following> followings = new ArrayList<>();
                if (success) {
                    try {
                        JSONArray array = new JSONArray(data);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            long id = object.getLong("id");
                            String type = object.getString("type");
                            long type_id = object.getLong("type_id");

                            boolean isCategory = false;
                            if (type.equalsIgnoreCase("category")) {
                                isCategory = true;
                            }

                            followings.add(new Following(id, type, type_id, isCategory));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {

                }
                if (followings.size() == 0) {
                    Intent intentFollowing = new Intent(MainActivity.this, CategoryActivity.class);
                    startActivity(intentFollowing);
                }
            }
        };
        FollowingExecute.getListFollowing(this, callback, CustomerController.getCurrentCustomer(this).getEntity_id());
    }

    private void setListBanner() {
        imageBannerShopAdapter = new ImageBannerShopAdapter(getSupportFragmentManager(), this, listBanner);
        pager.setAdapter(imageBannerShopAdapter);
        pager.setOffscreenPageLimit(listBanner.size());
        indicator.setViewPager(pager);
        pager.setCurrentItem(0);
        slideBannerPager(1);

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                indicator.setCurrentItem(position);
                if (position == listBanner.size() - 1) {
                    slideBannerPager(0);
                } else {
                    slideBannerPager(position + 1);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setCategoryPagerAdapter() {
        listCategory = new ArrayList<>();
        listCategory.add(new Category(0l, "All", 0, "", "", 0));
        listCategory.addAll(CategoryController.getAll(this));

        categoryPagerAdapter = new CategoryPagerAdapter(getSupportFragmentManager(), this, listCategory);
        viewPager.setAdapter(categoryPagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        tabStrip.setViewPager(viewPager);
        tabStrip.setTextColor(getResources().getColor(R.color.txt_black));
        tabStrip.setDividerColor(getResources().getColor(R.color.transparent));
        tabStrip.setIndicatorColor(getResources().getColor(R.color.main_color));
        tabStrip.setUnderlineColor(getResources().getColor(R.color.main_color));
        tabStrip.setUnderlineHeight((int) getResources().getDimension(R.dimen.dm_1dp));
        tabStrip.setTextSize((int) getResources().getDimension(R.dimen.txt_15sp));
        tabStrip.setBackgroundColor(getResources().getColor(R.color.white));
        tabStrip.setTabBackground(R.drawable.background_tab);
        tabStrip.setTypeface(font.light, 0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setTextColorTabStrip(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setTextColorTabStrip(0);
    }

    private void setTextColorTabStrip(int position) {
        LinearLayout lnl = (LinearLayout) tabStrip.getChildAt(0);
        for (int i = 0; i < lnl.getChildCount(); i++) {
            View v = lnl.getChildAt(i);
            try {
                if (v instanceof TextView) {
                    if (position == i) {
                        ((TextView) v).setTextColor(getResources().getColor(R.color.main_color));
                    } else {
                        ((TextView) v).setTextColor(getResources().getColor(R.color.txt_black));
                    }
                }
            } catch (Exception e) {

            }
        }
    }
}