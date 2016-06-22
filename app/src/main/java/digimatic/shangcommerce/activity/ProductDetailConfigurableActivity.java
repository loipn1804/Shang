package digimatic.shangcommerce.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import cn.lightsky.infiniteindicator.InfiniteIndicator;
import cn.lightsky.infiniteindicator.indicator.CircleIndicator;
import cn.lightsky.infiniteindicator.indicator.RecyleAdapter;
import cn.lightsky.infiniteindicator.page.OnPageClickListener;
import cn.lightsky.infiniteindicator.page.Page;
import digimatic.shangcommerce.R;
import digimatic.shangcommerce.adapter.OptionConfigAdapter;
import digimatic.shangcommerce.connection.Constant;
import digimatic.shangcommerce.connection.callback.UICallback;
import digimatic.shangcommerce.connection.threadconnection.execute.CartExecute;
import digimatic.shangcommerce.connection.threadconnection.execute.CustomerExecute;
import digimatic.shangcommerce.connection.threadconnection.execute.ProductExecute;
import digimatic.shangcommerce.connection.threadconnection.execute.WishlistExecute;
import digimatic.shangcommerce.daocontroller.CartDetailController;
import digimatic.shangcommerce.daocontroller.CartItemController;
import digimatic.shangcommerce.daocontroller.ConfigAttributeController;
import digimatic.shangcommerce.daocontroller.ConfigMergeController;
import digimatic.shangcommerce.daocontroller.ConfigOptionController;
import digimatic.shangcommerce.daocontroller.ConfigProductController;
import digimatic.shangcommerce.daocontroller.CustomerController;
import digimatic.shangcommerce.daocontroller.WishlistController;
import digimatic.shangcommerce.infiniteindicator.UILoader;
import digimatic.shangcommerce.model.AddWishList;
import digimatic.shangcommerce.model.Banner;
import digimatic.shangcommerce.model.CountId;
import digimatic.shangcommerce.model.ProductAttribute;
import digimatic.shangcommerce.model.ProductAttributeItem;
import digimatic.shangcommerce.staticfunction.Font;
import digimatic.shangcommerce.staticfunction.StaticFunction;
import greendao.CartDetail;
import greendao.CartItem;
import greendao.ConfigAttribute;
import greendao.ConfigMerge;
import greendao.ConfigOption;
import greendao.ConfigProduct;
import greendao.Customer;
import greendao.Product;
import greendao.Wishlist;

/**
 * Created by USER on 3/21/2016.
 */
public class ProductDetailConfigurableActivity extends BaseActivity implements View.OnClickListener, OnPageClickListener {

    private Font font;

    private RelativeLayout rltBack;
    private RelativeLayout rltSearch;
    private RelativeLayout rltCart;
    private TextView txtCart;
    private TextView txtTitle;

    private LinearLayout lnlListOption;
    private List<ProductAttribute> productAttributes;
    private TextView txtOptionLabel;
//    private List<MediaImage> mediaImages;

    // banner
//    private ViewPager pager;
//    private CirclePageIndicator indicator;
//    private ImageBannerProductAdapter imageBannerProductAdapter;
    private ArrayList<Banner> listBanner;
    private List<Banner> listImageDefault;
    private ToggleButton toggleFavorite;
    private TextView txtBannerDiscount;

    private ArrayList<Page> mPageViews;
    private InfiniteIndicator mDefaultIndicator;

    private ProgressBar progressBarDesc;
    private WebView webViewDesc;
    private LinearLayout lnlShowmore;
    private View viewDesc;
    private RelativeLayout rltWebDesc;
    private TextView txtShowmore;
    private ImageView imvShowmore;
    private boolean isShowFull = false;

    private TextView txtOldPrice;
    private TextView txtPrice;
    private TextView txtTotal;
    private TextView txtBuyNow;
    private TextView txtAddToCart;

    private RelativeLayout rltSub;
    private RelativeLayout rltAdd;
    private EditText txtQuantity;

    private RelativeLayout rltLoading;

    private long product_id;
    private String product_name;

    private TextView txtProductName;

    private Product mProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        if (getIntent().hasExtra("product_id")) {
            product_id = getIntent().getLongExtra("product_id", 0);
            product_name = "";
            if (product_id == 0) {
                finish();
            }
        } else {
            finish();
        }

        initView();
        initData();

        rltLoading.setVisibility(View.VISIBLE);
        listBanner = new ArrayList<>();
        listImageDefault = new ArrayList<>();
        productAttributes = new ArrayList<>();
//        mediaImages = new ArrayList<>();
        getProductDetail();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sendEvent("[Product]" + product_id + "#" + product_name, getString(R.string.analytic_out));
        ConfigAttributeController.clearAll(this);
        ConfigOptionController.clearAll(this);
        ConfigProductController.clearAll(this);
        ConfigMergeController.clearAll(this);
    }

    private void initView() {
        rltBack = (RelativeLayout) findViewById(R.id.rltBack);
        rltSearch = (RelativeLayout) findViewById(R.id.rltSearch);
        rltCart = (RelativeLayout) findViewById(R.id.rltCart);
        txtCart = (TextView) findViewById(R.id.txtCart);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        lnlListOption = (LinearLayout) findViewById(R.id.lnlListOption);
        txtOptionLabel = (TextView) findViewById(R.id.txtOptionLabel);

        rltBack.setOnClickListener(this);
        rltSearch.setOnClickListener(this);
        rltCart.setOnClickListener(this);

//        pager = (ViewPager) findViewById(R.id.pager);
//        indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        toggleFavorite = (ToggleButton) findViewById(R.id.toggleFavorite);
        txtBannerDiscount = (TextView) findViewById(R.id.txtBannerDiscount);

        progressBarDesc = (ProgressBar) findViewById(R.id.progressBarDesc);
        webViewDesc = (WebView) findViewById(R.id.webViewDesc);
        lnlShowmore = (LinearLayout) findViewById(R.id.lnlShowmore);
        viewDesc = findViewById(R.id.viewDesc);
        rltWebDesc = (RelativeLayout) findViewById(R.id.rltWebDesc);
        txtShowmore = (TextView) findViewById(R.id.txtShowmore);
        imvShowmore = (ImageView) findViewById(R.id.imvShowmore);

        txtOldPrice = (TextView) findViewById(R.id.txtOldPrice);
        txtPrice = (TextView) findViewById(R.id.txtPrice);
        txtTotal = (TextView) findViewById(R.id.txtTotal);
        txtBuyNow = (TextView) findViewById(R.id.txtBuyNow);
        txtAddToCart = (TextView) findViewById(R.id.txtAddToCart);

        txtProductName = (TextView) findViewById(R.id.txtProductName);

        lnlShowmore.setOnClickListener(this);
        txtBuyNow.setOnClickListener(this);
        txtAddToCart.setOnClickListener(this);

        rltSub = (RelativeLayout) findViewById(R.id.rltSub);
        rltAdd = (RelativeLayout) findViewById(R.id.rltAdd);
        rltSub.setOnClickListener(this);
        rltAdd.setOnClickListener(this);

        txtQuantity = (EditText) findViewById(R.id.txtQuantity);
        txtQuantity.setText("1");

        rltLoading = (RelativeLayout) findViewById(R.id.rltLoading);

        webViewDesc.setEnabled(false);
        webViewDesc.setClickable(false);
        webViewDesc.setVisibility(View.INVISIBLE);

        font = new Font(this);
        font.overrideFontsLight(findViewById(R.id.root));
        font.overrideFontsBold(txtTitle);
        font.overrideFontsBold(txtOldPrice);
        font.overrideFontsBold(txtPrice);
        font.overrideFontsBold(txtTotal);
        font.overrideFontsBold(txtBuyNow);
        font.overrideFontsBold(txtAddToCart);

        font.overrideFontsBold(findViewById(R.id.txtPriceLabel));
        font.overrideFontsBold(txtOptionLabel);
        font.overrideFontsBold(findViewById(R.id.txtDescLabel));
        font.overrideFontsBold(findViewById(R.id.txtQuantityLabel));
        font.overrideFontsBold(findViewById(R.id.txtTotalLabel));
        font.overrideFontsBold(txtBannerDiscount);
        font.overrideFontsBold(txtProductName);

        txtOldPrice.setPaintFlags(txtOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        txtQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() == 0) {
                    showToastError(getString(R.string.null_quantity));
                } else if (s.toString().length() > 2) {
                    txtQuantity.setText(s.subSequence(0, 2));
                    showToastError(getString(R.string.long_quantity));
                } else {
                    calculateTotalPrice();
                }
                txtQuantity.setSelection(txtQuantity.getText().length());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initData() {
        setFavoriteAction(product_id);
    }

    private void setListOption() {
        lnlListOption.removeAllViews();
        List<ConfigMerge> configMerges = ConfigMergeController.getAllAttributeId(this);
        int size = configMerges.size();
        if (size == 0) {
            txtOptionLabel.setVisibility(View.GONE);
        }
        for (int i = 0; i < size; i++) {
            final int j = i;
            final ConfigMerge configMerge = configMerges.get(j);
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.item_product_option, null);
            RelativeLayout rltOption = (RelativeLayout) view.findViewById(R.id.rltOption);
            final TextView txtOption = (TextView) view.findViewById(R.id.txtOption);
            font.overrideFontsLight(txtOption);

            if (configMerges.get(j).getIs_chosen_att() == 1) {
                txtOption.setText(ConfigMergeController.getOptionLabelChosenByAttributeId(this, configMerge.getAttribute_id()));
            } else {
                txtOption.setText(ConfigAttributeController.getLabelById(this, configMerge.getAttribute_id()));
            }

            rltOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopupListOption(configMerge.getAttribute_id());
                }
            });
            lnlListOption.addView(view);
        }
    }

    private void setListBanner() {
//        imageBannerProductAdapter = new ImageBannerProductAdapter(getSupportFragmentManager(), this, listBanner);
//        pager.setAdapter(imageBannerProductAdapter);
//        pager.setOffscreenPageLimit(listBanner.size());
//        indicator.setViewPager(pager);
//        pager.setCurrentItem(0);

        mPageViews = new ArrayList<>();
        for (Banner banner : listBanner) {
            mPageViews.add(new Page(banner.getUrl(), banner.isImage(), banner.getUrl_video(), this));
        }
        mDefaultIndicator = (InfiniteIndicator) findViewById(R.id.indicator_default_circle);
        mDefaultIndicator.setCallback(recyleAdapterCallback);
        mDefaultIndicator.setImageLoader(new UILoader());
        mDefaultIndicator.addPages(mPageViews);
        mDefaultIndicator.setPosition(InfiniteIndicator.IndicatorPosition.Center_Bottom);

        CircleIndicator circleIndicator = ((CircleIndicator) mDefaultIndicator.getPagerIndicator());
//        final float density = getResources().getDisplayMetrics().density;
//        circleIndicator.setBackgroundColor(0xFFCCCCCC);
        circleIndicator.setRadius(getResources().getDimension(R.dimen.dm_4dp));
        circleIndicator.setPageColor(getResources().getColor(R.color.default_circle_indicator_page_color));
        circleIndicator.setFillColor(getResources().getColor(R.color.default_circle_indicator_fill_color));
//        circleIndicator.setStrokeColor(0xFF000000);
        circleIndicator.setStrokeWidth(0);
    }

    private RecyleAdapter.RecyleAdapterCallback recyleAdapterCallback = new RecyleAdapter.RecyleAdapterCallback() {
        @Override
        public void clickOnItem(int position, Page page) {
            Log.e("Position",position + "");
//            if (page.isImage()) {
//                Intent intent = new Intent(ProductDetailConfigurableActivity.this, PhotoZoomActivity.class);
//                intent.putExtra("url", page.getUrl());
//                startActivity(intent);
//            } else {
//                Intent intent = new Intent(ProductDetailConfigurableActivity.this, VideoViewActivity.class);
//                intent.putExtra("url", page.getUrl_video());
//                startActivity(intent);
//            }
            Intent intent = new Intent(ProductDetailConfigurableActivity.this, BannerViewActivity.class);
            intent.putParcelableArrayListExtra(BannerViewActivity.EXTRA_ARRAY_LIST_PAGE, listBanner);
            intent.putExtra(BannerViewActivity.EXTRA_POSITION,position);
            startActivity(intent);
        }
    };

    private void setFavoriteAction(long product_id) {
        toggleFavorite.setOnCheckedChangeListener(null);
        List<Wishlist> wishlists = WishlistController.getAll(this);
        boolean isAdded = false;
        for (Wishlist wishlist : wishlists) {
            if (wishlist.getProduct_id() == product_id) {
                isAdded = true;
                break;
            }
        }
        toggleFavorite.setChecked(isAdded);
        toggleFavorite.setOnCheckedChangeListener(toggleOnCheckedChangeListener);
    }

    private CompoundButton.OnCheckedChangeListener toggleOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                addToWishlist();
            } else {
                Wishlist wishlist = WishlistController.getByProductId(ProductDetailConfigurableActivity.this, product_id);
                if (wishlist == null) {
                    setFavoriteAction(product_id);
                } else {
                    deleteFromWishlist(wishlist.getWishlist_item_id());
                }
            }
        }
    };

    private void showPopupListOption(long attribute_id) {
        final List<ConfigOption> configOptions = ConfigMergeController.getOptionListByFilterChosen(this, attribute_id);
        final List<ConfigOption> configOptionsAll = ConfigMergeController.getOptionListByOptionId(this, attribute_id);
        final List<Boolean> booleanList = new ArrayList<>();
        for (ConfigOption configOptionAll : configOptionsAll) {
            boolean isHave = false;
            for (ConfigOption configOption : configOptions) {
                if (configOptionAll.getOption_id().equals(configOption.getOption_id())) {
                    isHave = true;
                    break;
                }
            }
            booleanList.add(isHave);
        }
        List<String> strings = new ArrayList<>();
        for (ConfigOption configOption : configOptionsAll) {
            strings.add(configOption.getLabel());
        }
        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_list_option);

        ListView listView = (ListView) dialog.findViewById(R.id.listView);
        OptionConfigAdapter adapter = new OptionConfigAdapter(this, strings, booleanList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ConfigMergeController.setChosenOptionAndAttribute(ProductDetailConfigurableActivity.this, configOptionsAll.get(position).getOption_id());
                if (!booleanList.get(position)) {
                    ConfigMergeController.resetOtherWhenChosenNotCompatible(ProductDetailConfigurableActivity.this, configOptionsAll.get(position).getOption_id());
                }
                setListOption();

                List<ConfigMerge> mergeList = ConfigMergeController.getListNotChosen(ProductDetailConfigurableActivity.this);
                if (mergeList.size() == 0) {
                    long product_config_id = ConfigMergeController.getProductToAddCart(ProductDetailConfigurableActivity.this);
                    ConfigProduct configProduct = ConfigProductController.getById(ProductDetailConfigurableActivity.this, product_config_id);
                    if (configProduct != null) {
                        try {
                            JSONArray array = new JSONArray(configProduct.getImages());
                            listBanner.clear();
                            for (int i = 0; i < array.length(); i++) {
                                String url = array.getString(i);
                                listBanner.add(new Banner(url, true, ""));
                            }
//                            imageBannerProductAdapter.removeListFragment(getSupportFragmentManager());
//                            pager.setAdapter(null);
//                            imageBannerProductAdapter = new ImageBannerProductAdapter(getSupportFragmentManager(), ProductDetailConfigurableActivity.this, listBanner);
//                            pager.setAdapter(imageBannerProductAdapter);
//                            pager.setOffscreenPageLimit(listBanner.size());
//                            indicator.setViewPager(pager);
//                            pager.setCurrentItem(0);

                            mPageViews.clear();
                            for (Banner banner : listBanner) {
                                mPageViews.add(new Page(banner.getUrl(), banner.isImage(), banner.getUrl_video(), ProductDetailConfigurableActivity.this));
                            }
                            mDefaultIndicator.setListData(mPageViews);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    listBanner.clear();
                    listBanner.addAll(listImageDefault);
                }
//                imageBannerProductAdapter.removeListFragment(getSupportFragmentManager());
//                pager.setAdapter(null);
//                imageBannerProductAdapter = new ImageBannerProductAdapter(getSupportFragmentManager(), ProductDetailConfigurableActivity.this, listBanner);
//                pager.setAdapter(imageBannerProductAdapter);
//                pager.setOffscreenPageLimit(listBanner.size());
//                indicator.setViewPager(pager);
//                pager.setCurrentItem(0);

                mPageViews.clear();
                for (Banner banner : listBanner) {
                    mPageViews.add(new Page(banner.getUrl(), banner.isImage(), banner.getUrl_video(), ProductDetailConfigurableActivity.this));
                }
                mDefaultIndicator.setListData(mPageViews);

                calculateTotalPrice();

                dialog.dismiss();
            }
        });

        dialog.show();
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
                if (isShowFull) {
                    int height = getResources().getDimensionPixelOffset(R.dimen.dm_100dp);

                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rltWebDesc.getLayoutParams();
                    params.height = height;
                    rltWebDesc.setLayoutParams(params);

                    txtShowmore.setText("SHOW MORE");
                    viewDesc.setVisibility(View.VISIBLE);
                    imvShowmore.setImageResource(R.drawable.ic_showmore);
                    isShowFull = false;
                } else {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rltWebDesc.getLayoutParams();
                    params.height = LinearLayout.LayoutParams.MATCH_PARENT;
                    rltWebDesc.setLayoutParams(params);

                    txtShowmore.setText("SHOW LESS");
                    viewDesc.setVisibility(View.GONE);
                    imvShowmore.setImageResource(R.drawable.ic_showless);
                    isShowFull = true;
                }
                break;
            case R.id.rltSub:
                subQuantity();
                break;
            case R.id.rltAdd:
                addQuantity();
                break;
            case R.id.txtBuyNow:
                buyNow();
                break;
            case R.id.txtAddToCart:
                addToCart();
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
            toggleFavorite.setVisibility(View.VISIBLE);
            Customer customer = CustomerController.getCurrentCustomer(this);
            txtCart.setText(customer.getCart_items_count() + "");
            if (customer.getCart_items_count() > 99) {
                txtCart.setText("99+");
            }
        } else {
            txtCart.setVisibility(View.GONE);
            toggleFavorite.setVisibility(View.GONE);
        }
    }

    private void subQuantity() {
        String qty_str = txtQuantity.getText().toString().trim();
        if (qty_str.length() > 0) {
            int quantity = Integer.parseInt(qty_str);
            if (quantity > 1) {
                quantity--;
                txtQuantity.setText(quantity + "");
                calculateTotalPrice();
            }
        } else {
            showToastError(getString(R.string.null_quantity));
        }
    }

    private void addQuantity() {
        String qty_str = txtQuantity.getText().toString().trim();
        if (qty_str.length() > 0) {
            int quantity = Integer.parseInt(qty_str);
            quantity++;
            txtQuantity.setText(quantity + "");
            calculateTotalPrice();
        } else {
            showToastError(getString(R.string.null_quantity));
        }
    }

    private void calculateTotalPrice() {
        int quantity = Integer.parseInt(txtQuantity.getText().toString());
        float price;
        if (mProduct.getSpecial_price() == 0) {
            price = mProduct.getPrice();
        } else {
            price = mProduct.getSpecial_price();
        }

        float option_price = ConfigMergeController.getTotalPriceOptionChosen(this);

        price = price + option_price;

        if (mProduct.getSpecial_price() == 0) {
            txtOldPrice.setVisibility(View.GONE);
            txtBannerDiscount.setVisibility(View.GONE);
            txtBannerDiscount.setText("0%");
        } else {
            txtOldPrice.setVisibility(View.VISIBLE);
            BigDecimal b_special_price = new BigDecimal(mProduct.getPrice() + option_price).setScale(2, BigDecimal.ROUND_HALF_UP);
            txtOldPrice.setText("$" + StaticFunction.formatPrice(b_special_price));

            int percent = (int) ((mProduct.getPrice() + option_price - price) * 100 / (mProduct.getPrice() + option_price));
            txtBannerDiscount.setVisibility(View.VISIBLE);
            txtBannerDiscount.setText(percent + "%");
        }
        BigDecimal b_price = new BigDecimal(price).setScale(2, BigDecimal.ROUND_HALF_UP);
        txtPrice.setText("$" + StaticFunction.formatPrice(b_price));
        BigDecimal b_total = new BigDecimal(price * quantity).setScale(2, BigDecimal.ROUND_HALF_UP);
        txtTotal.setText("$" + StaticFunction.formatPrice(b_total));
    }

    private float getCurrentPrice() {
        float price = 0;
        if (mProduct != null) {
            if (mProduct.getSpecial_price() == 0) {
                price = mProduct.getPrice();
            } else {
                price = mProduct.getSpecial_price();
            }

            float option_price = ConfigMergeController.getTotalPriceOptionChosen(this);

            price = price + option_price;
        }
        return price;
    }

    private void getProductDetail() {
        UICallback callback = new UICallback() {
            @Override
            public void onResult(boolean success, String data) {
                if (success) {
                    try {
                        JSONObject object = new JSONObject(data);
                        long entity_id = object.getLong("entity_id");
                        String type_id = object.getString("type_id");
                        String name = object.getString("name");
                        String sku = object.getString("sku");
                        String manufacturer = object.optString("manufacturer", "");
                        String short_description = object.getString("short_description");
                        String description = object.getString("description");
                        String price = object.getString("price");
                        float price_f;
                        if (price.equalsIgnoreCase("null")) {
                            price_f = 0;
                        } else {
                            price_f = Float.parseFloat(price);
                        }
                        String special_price = object.getString("special_price");
                        float special_price_f;
                        if (special_price.equalsIgnoreCase("null")) {
                            special_price_f = 0;
                        } else {
                            special_price_f = Float.parseFloat(special_price);
                        }
                        String small_image_url = object.getString("small_image_url");
                        String thumbnail_url = object.getString("thumbnail_url");
                        String news_from_date = object.getString("news_from_date");
                        String news_to_date = object.getString("news_to_date");
                        String created_at = object.getString("created_at");
                        String updated_at = object.getString("updated_at");

                        listBanner.clear();
                        listImageDefault.clear();
                        JSONObject video = object.getJSONObject("video");
                        String source_url = video.optString("source_url", "");
                        String screenshot_url = video.optString("screenshot_url", "");
                        if (source_url.length() > 0) {
                            listBanner.add(new Banner(screenshot_url, false, source_url));
                        }
                        JSONArray media_gallery = object.getJSONArray("media_gallery");
                        for (int i = 0; i < media_gallery.length(); i++) {
                            JSONObject gallery = media_gallery.getJSONObject(i);
                            String url = gallery.getString("url");
                            listImageDefault.add(new Banner(url, true, ""));
                        }
                        listBanner.addAll(listImageDefault);
                        setListBanner();

                        mProduct = new Product();
                        mProduct.setPrice(price_f);
                        mProduct.setSpecial_price(special_price_f);

                        product_name = name;
                        txtProductName.setText(name);
                        calculateTotalPrice();

                        webViewDesc.getSettings().setJavaScriptEnabled(true);
                        final String url_desc = Constant.BASE_URL + "webapp/product/view/id/" + entity_id;
                        webViewDesc.loadUrl(url_desc);
                        webViewDesc.setWebViewClient(new WebViewClient() {
                            @Override
                            public void onPageFinished(WebView view, String url) {
                                webViewDesc.setVisibility(View.VISIBLE);
                                progressBarDesc.setVisibility(View.GONE);
                                Handler handler = new Handler();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        int height = getResources().getDimensionPixelOffset(R.dimen.dm_100dp);

                                        if (rltWebDesc.getMeasuredHeight() >= height) {
                                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rltWebDesc.getLayoutParams();
                                            params.height = height;
                                            rltWebDesc.setLayoutParams(params);

                                            txtShowmore.setText("SHOW MORE");
                                            viewDesc.setVisibility(View.VISIBLE);
                                            imvShowmore.setImageResource(R.drawable.ic_showmore);
                                            isShowFull = false;
                                        } else {
                                            lnlShowmore.setVisibility(View.GONE);
                                            viewDesc.setVisibility(View.GONE);
                                        }
                                    }
                                });
                            }

                            @Override
                            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                if (url.equals(url_desc)) {
                                    view.loadUrl(url);
                                }
                                return true;
                            }
                        });
                        rltLoading.setVisibility(View.GONE);

                        getProductConfigurable();

                        sendEvent("[Product]" + product_id + "#" + product_name, getString(R.string.analytic_in));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showToastError(e.getMessage());
                    }
                } else {
                    showToastError(data);
                }
            }
        };
        ProductExecute.getProductById(this, callback, product_id);
    }

    private void getProductConfigurable() {
        UICallback callback = new UICallback() {
            @Override
            public void onResult(boolean success, String data) {
                if (success) {
                    try {
                        JSONObject root = new JSONObject(data);
                        JSONArray attributes = root.getJSONArray("attributes");
                        ConfigAttributeController.clearAll(ProductDetailConfigurableActivity.this);
                        ConfigOptionController.clearAll(ProductDetailConfigurableActivity.this);
                        ConfigMergeController.clearAll(ProductDetailConfigurableActivity.this);
                        int att = 1;
                        int opt = 1;
                        int pro = 1;
                        int mer = 1;
                        long count = 1;
                        for (int i = 0; i < attributes.length(); i++) {
                            JSONObject attribute = attributes.getJSONObject(i);
                            long id = attribute.getLong("id");
                            String code = attribute.getString("code");
                            String label = attribute.getString("label");

                            JSONArray options = attribute.getJSONArray("options");
                            for (int j = 0; j < options.length(); j++) {
                                JSONObject option = options.getJSONObject(j);
                                long id_op = option.getLong("id");
                                String label_op = option.getString("label");
                                String price_op = option.getString("price");
                                if (price_op.equalsIgnoreCase("null")) price_op = "0";
                                float price_op_f = Float.parseFloat(price_op);

                                JSONArray products = option.getJSONArray("products");
                                for (int k = 0; k < products.length(); k++) {
                                    String id_pro = products.getString(k);
                                    long id_pro_l = Long.parseLong(id_pro);

                                    ConfigMergeController.insertOrUpdate(ProductDetailConfigurableActivity.this, new ConfigMerge(count++, id, id_op, id_pro_l, 0, 0, mer++));
                                }

                                ConfigOptionController.insertOrUpdate(ProductDetailConfigurableActivity.this, new ConfigOption(id_op, label_op, price_op_f, opt++));
                            }

                            ConfigAttributeController.insertOrUpdate(ProductDetailConfigurableActivity.this, new ConfigAttribute(id, code, label, att++));
                        }

                        JSONArray media_images = root.getJSONArray("media_images");
                        ConfigProductController.clearAll(ProductDetailConfigurableActivity.this);
                        for (int i = 0; i < media_images.length(); i++) {
                            JSONObject media_image = media_images.getJSONObject(i);
                            long product_id = media_image.getLong("product_id");
                            String base_image = media_image.getString("base_image");
                            JSONArray images = media_image.getJSONArray("images");

                            ConfigProductController.insertOrUpdate(ProductDetailConfigurableActivity.this, new ConfigProduct(product_id, base_image, images.toString(), pro++));
                        }
                        setListOption();
                    } catch (Exception e) {
                        e.printStackTrace();
                        showToastError(e.getMessage());
                    }
                } else {
                    showToastError(data);
                }
            }
        };
        ProductExecute.getProductConfigurable(this, callback, product_id);
    }

    private void getWishlist() {
        Customer customer = CustomerController.getCurrentCustomer(this);
        UICallback callback = new UICallback() {
            @Override
            public void onResult(boolean success, String data) {
                if (success) {
                    try {
                        JSONArray array = new JSONArray(data);
                        WishlistController.clearAll(ProductDetailConfigurableActivity.this);
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

                            WishlistController.insertOrUpdate(ProductDetailConfigurableActivity.this, new Wishlist(wishlist_item_id, product_id, name, sku, manufacturer, price_f, image_url, added_at, type_id));
                        }
                        setFavoriteAction(product_id);
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
        WishlistExecute.getByCustomerId(this, callback, customer.getEntity_id());
//        showProgressDialog(false);
    }

    private void addToWishlist() {
        AddWishList addWishList = new AddWishList(CustomerController.getCurrentCustomer(this).getEntity_id(), product_id);
        String body = new Gson().toJson(addWishList);
        UICallback callback = new UICallback() {
            @Override
            public void onResult(boolean success, String data) {
                if (success) {
                    try {
                        JSONObject object = new JSONObject(data);
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

                        WishlistController.insertOrUpdate(ProductDetailConfigurableActivity.this, new Wishlist(wishlist_item_id, product_id, name, sku, manufacturer, price_f, image_url, added_at, type_id));
                        setFavoriteAction(product_id);
                        showToastOk(getString(R.string.add_wishlist));
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
        WishlistExecute.addToWishlist(this, callback, body);
        showProgressDialog(false);
    }

    private void deleteFromWishlist(final long wishlist_item_id) {
        UICallback callback = new UICallback() {
            @Override
            public void onResult(boolean success, String data) {
                if (success) {
                    WishlistController.deleteById(ProductDetailConfigurableActivity.this, wishlist_item_id);
                    setFavoriteAction(product_id);
                    showToastOk(getString(R.string.remove_wishlist));
                } else {
                    showToastError(data);
                }
                hideProgressDialog();
            }
        };
        WishlistExecute.deleteFromWishlist(this, callback, wishlist_item_id);
        showProgressDialog(false);
    }

    private void addToCart() {
        String qty_str = txtQuantity.getText().toString().trim();
        if (qty_str.length() == 0) {
            showToastError(getString(R.string.null_quantity));
        } else if (qty_str.length() > 2) {
            showToastError(getString(R.string.long_quantity));
        } else if (Integer.parseInt(qty_str) == 0) {
            showToastError(getString(R.string.zero_quantity));
        } else if (CustomerController.isLogin(this)) {
            List<ConfigMerge> mergeList = ConfigMergeController.getListNotChosen(this);
            if (mergeList.size() > 0) {
                showToastInfo("Please choose " + ConfigAttributeController.getLabelById(this, mergeList.get(0).getAttribute_id()));
                return;
            }

            final int qty = Integer.parseInt(txtQuantity.getText().toString().trim());
            final Customer customer = CustomerController.getCurrentCustomer(this);

            long product_config_id = ConfigMergeController.getProductToAddCart(this);
            List<ConfigMerge> configMerges = ConfigMergeController.getListChosen(this, product_config_id);
            JSONObject optionObj = new JSONObject();
            for (ConfigMerge configMerge : configMerges) {
                try {
                    optionObj.put(configMerge.getAttribute_id() + "", configMerge.getOption_id() + "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            JSONObject bodyObj = new JSONObject();
            try {
                bodyObj.put("customer_id", customer.getEntity_id());
                bodyObj.put("price", getCurrentPrice());
                bodyObj.put("product_id", product_id);
                bodyObj.put("qty", qty);
                bodyObj.put("options", optionObj);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String body = bodyObj.toString();
            UICallback callback = new UICallback() {
                @Override
                public void onResult(boolean success, String data) {
                    if (success) {
//                        getCart(customer);
                        getCustomerDetailToGetCountNumbers();
                        showToastOk("Added to cart!");
                        txtQuantity.setText("1");
                    } else {
                        showToastError(data);
                    }
                    hideProgressDialog();
                }
            };
            CartExecute.addProductToCart(this, callback, body);
            showProgressDialog(false);
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    private long getProductIdConfig() {
        List<List<Long>> bigList = new ArrayList<>();
        for (int j = 0; j < productAttributes.size(); j++) {
            List<ProductAttributeItem> listFull = productAttributes.get(j).getProductAttributeItems();
            final List<ProductAttributeItem> list = new ArrayList<>();
            if (j - 1 >= 0) {
                List<ProductAttributeItem> listChosen = productAttributes.get(j - 1).getProductAttributeItems();
                List<Long> listProductChosen = new ArrayList<>();
                for (ProductAttributeItem productAttributeItem : listChosen) {
                    if (productAttributeItem.isChosen()) {
                        listProductChosen.addAll(productAttributeItem.getProducts());
                    }
                }
                for (Long productChosen : listProductChosen) {
                    for (ProductAttributeItem productAttributeItem : listFull) {
                        for (Long product_id : productAttributeItem.getProducts()) {
                            if (product_id.equals(productChosen)) {
                                list.add(productAttributeItem);
                                break;
                            }
                        }
                    }
                }
            } else {
                list.addAll(listFull);
            }
            List<ProductAttributeItem> productAttributeItems = list;
            for (ProductAttributeItem productAttributeItem : productAttributeItems) {
                if (productAttributeItem.isChosen()) {
                    bigList.add(productAttributeItem.getProducts());
                    break;
                }
            }
        }

        List<CountId> countIds = new ArrayList<>();
        for (List<Long> smallList : bigList) {
            for (long small : smallList) {
                boolean isExist = false;
                for (CountId countId : countIds) {
                    if (countId.getId() == small) {
                        countId.setCount(countId.getCount() + 1);
                        isExist = true;
                        break;
                    }
                }
                if (!isExist) {
                    countIds.add(new CountId(small, 1));
                }
            }
        }
        int maxCount = countIds.get(0).getCount();
        for (int i = 0; i < countIds.size(); i++) {
            if (maxCount < countIds.get(i).getCount()) {
                maxCount = countIds.get(i).getCount();
            }
        }
        long product_id = 0;
        for (CountId countId : countIds) {
            if (maxCount == countId.getCount()) {
                product_id = countId.getId();
                break;
            }
        }
        return product_id;
    }

    private void buyNow() {
        if (CustomerController.isLogin(this)) {
            List<ConfigMerge> mergeList = ConfigMergeController.getListNotChosen(this);
            if (mergeList.size() > 0) {
                showToastInfo("Please choose " + ConfigAttributeController.getLabelById(this, mergeList.get(0).getAttribute_id()));
                return;
            }

            String strQuantity = txtQuantity.getText().toString();
            if (TextUtils.isEmpty(strQuantity)) {
                strQuantity = "0";
            }
            final int qty = Integer.parseInt(strQuantity.trim());
            final Customer customer = CustomerController.getCurrentCustomer(this);

            long product_config_id = ConfigMergeController.getProductToAddCart(this);

            JSONObject bodyObj = new JSONObject();
            try {
                bodyObj.put("customer_id", customer.getEntity_id());
                bodyObj.put("price", getCurrentPrice());
                bodyObj.put("product_id", product_config_id);
                bodyObj.put("qty", qty);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String body = bodyObj.toString();
            UICallback callback = new UICallback() {
                @Override
                public void onResult(boolean success, String data) {
                    if (success) {
                        Intent intent = new Intent(ProductDetailConfigurableActivity.this, CartActivity.class);
                        startActivity(intent);
                        getCustomerDetailToGetCountNumbers();
                        txtQuantity.setText("1");
                    } else {
                        showToastError(data);
                    }
                    hideProgressDialog();
                }
            };
            CartExecute.addProductToCart(this, callback, body);
            showProgressDialog(false);
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
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
                        if (CustomerController.isLogin(ProductDetailConfigurableActivity.this)) {
                            Customer customer = CustomerController.getCurrentCustomer(ProductDetailConfigurableActivity.this);
                            customer.setCart_items_count(cart_items_count);
                            customer.setUnread_alerts_count(unread_alerts_count);
                            CustomerController.insertOrUpdate(ProductDetailConfigurableActivity.this, customer);
                        }
                        setupCart();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {

                }
            }
        };
        CustomerExecute.getCustomerCounter(this, callback, CustomerController.getCurrentCustomer(this).getEntity_id());
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

                            CartDetailController.clearAll(ProductDetailConfigurableActivity.this);
                            CartDetailController.insertOrUpdate(ProductDetailConfigurableActivity.this, new CartDetail(entity_id, items_count, items_qty, subtotal, grand_total, coupon_code, customer_id, created_at, updated_at, f_tax_amount_cart, f_shipping_amount, f_discount_amount_b, f_use_reward_points, discount_description, voucher_list));

                            JSONArray array = cart.getJSONArray("cart_items");
                            CartItemController.clearAll(ProductDetailConfigurableActivity.this);
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

                                CartItemController.insertOrUpdate(ProductDetailConfigurableActivity.this, new CartItem(item_id, product_id, name, sku, manufacturer, price, image_url, qty, row_total, options, discount_amount, tax_amount, type_id));
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
                setupCart();
            }
        };
        CartExecute.getCart(this, callback, customer.getEntity_id());
//        showProgressDialog(false);
    }

    @Override
    public void onPageClick(int position, Page page) {

    }
}
