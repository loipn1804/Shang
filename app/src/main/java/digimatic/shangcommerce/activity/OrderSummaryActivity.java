package digimatic.shangcommerce.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.adapter.EVoucherAdapter;
import digimatic.shangcommerce.adapter.OrderSummaryAdapter;
import digimatic.shangcommerce.connection.callback.UICallback;
import digimatic.shangcommerce.connection.threadconnection.execute.CRMExecute;
import digimatic.shangcommerce.connection.threadconnection.execute.CartExecute;
import digimatic.shangcommerce.connection.threadconnection.execute.OrderExecute;
import digimatic.shangcommerce.daocontroller.CartDetailController;
import digimatic.shangcommerce.daocontroller.CartItemController;
import digimatic.shangcommerce.daocontroller.CustomerController;
import digimatic.shangcommerce.model.OrderSummary;
import digimatic.shangcommerce.model.VoucherItem;
import digimatic.shangcommerce.staticfunction.Font;
import digimatic.shangcommerce.staticfunction.StaticFunction;
import greendao.CartDetail;
import greendao.CartItem;
import greendao.Customer;

/**
 * Created by USER on 3/17/2016.
 */
public class OrderSummaryActivity extends BaseActivity implements View.OnClickListener {

    private ListView listView;
    private OrderSummaryAdapter adapter;
    private List<CartItem> listCart;

    private RelativeLayout rltBack;
    private TextView txtTitle;

    private TextView txtPaymentPrice;

    private TextView txtApply;
    private EditText edtCoupon;

    private RelativeLayout rltBuyNow;

    private TextView txtDiscountOrVoucherLabel;
    private TextView txtDeliveryFee;
    private TextView txtGST;
    private TextView txtSubtotal;
    private TextView txtDiscount;
    private TextView txtTotal;
    private TextView txtDiscountLabel;
    private TextView txtEVoucher;
    private TextView txtCoupon;

    private ImageView imvInfo;

    private EditText edtRemark;

    private ImageView imvToggle;
    private LinearLayout lnlInfo;
    private LinearLayout lnlSubInfo;
    private Animation animationDown;
    private Animation animationUp;
    private boolean isShowInfo;
    private int animationTime = 1000;

    private boolean isDiscount;
    private Dialog dialogDiscountCodeOrEVoucher;

    private List<VoucherItem> voucherItemList;
    private EVoucherAdapter eVoucherAdapter;

    //////////////// setup paypal
    private String PAY_ID = "";

    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;

    // note that these credentials will differ between live & sandbox environments.
    private static final String CONFIG_CLIENT_ID = "AWV3Vl3P5kdGORcQcHimVb44SYwhhUcAb3JraSaq2naXr0HG5ZeISEzKv9zQFT9V8P5Rjd8iuN-MFp6P";
//    private static final String CONFIG_CLIENT_ID = "ASGfOih2a3fNtd0nhC3dBtDLGraR3wnm_ZHCa5fsn53QLzP6QksaM0GI6hSO9BUPvsDaCqhlX9QI5bp8";

    private static final int REQUEST_CODE_PAYMENT = 1;

    private static PayPalConfiguration config;
    /////// setup paypal


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

        isDiscount = true;

        initPaypal();

        initView();
        initData();
    }

    private void initView() {
        rltBack = (RelativeLayout) findViewById(R.id.rltBack);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        listView = (ListView) findViewById(R.id.listView);
        rltBuyNow = (RelativeLayout) findViewById(R.id.rltBuyNow);

        txtPaymentPrice = (TextView) findViewById(R.id.txtPaymentPrice);

        txtApply = (TextView) findViewById(R.id.txtApply);
        edtCoupon = (EditText) findViewById(R.id.edtCoupon);

        rltBack.setOnClickListener(this);
        rltBuyNow.setOnClickListener(this);
        txtApply.setOnClickListener(this);

        txtDiscountOrVoucherLabel = (TextView) findViewById(R.id.txtDiscountOrVoucherLabel);
        txtDeliveryFee = (TextView) findViewById(R.id.txtDeliveryFee);
        txtGST = (TextView) findViewById(R.id.txtGST);
        txtSubtotal = (TextView) findViewById(R.id.txtSubtotal);
        txtDiscount = (TextView) findViewById(R.id.txtDiscount);
        txtTotal = (TextView) findViewById(R.id.txtTotal);
        txtDiscountLabel = (TextView) findViewById(R.id.txtDiscountLabel);
        txtEVoucher = (TextView) findViewById(R.id.txtEVoucher);
        txtCoupon = (TextView) findViewById(R.id.txtCoupon);

        edtRemark = (EditText) findViewById(R.id.edtRemark);

        imvInfo = (ImageView) findViewById(R.id.imvInfo);

        imvToggle = (ImageView) findViewById(R.id.imvToggle);
        lnlInfo = (LinearLayout) findViewById(R.id.lnlInfo);
        lnlSubInfo = (LinearLayout) findViewById(R.id.lnlSubInfo);

        isShowInfo = true;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int height = lnlSubInfo.getMeasuredHeight();
                animationDown = new TranslateAnimation(0, 0, 0, height);
                animationDown.setDuration(animationTime);
                animationDown.setFillAfter(false);
                animationDown.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        lnlSubInfo.setVisibility(View.GONE);
                        isShowInfo = false;
                        imvToggle.setImageResource(R.drawable.ic_show_up);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                animationUp = new TranslateAnimation(0, 0, height, 0);
                animationUp.setDuration(animationTime);
                animationUp.setFillAfter(false);
                animationUp.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        isShowInfo = true;
                        imvToggle.setImageResource(R.drawable.ic_show_down);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        }, 300);

        txtDiscountOrVoucherLabel.setOnClickListener(this);
        imvInfo.setOnClickListener(this);
        imvToggle.setOnClickListener(this);

        Font font = new Font(this);
        font.overrideFontsLight(findViewById(R.id.root));
        font.overrideFontsBold(txtTitle);
        font.overrideFontsBold(txtApply);
        font.overrideFontsBold(findViewById(R.id.txtDeliveryFeeLabel));
        font.overrideFontsBold(txtDeliveryFee);
        font.overrideFontsBold(findViewById(R.id.txtGSTLabel));
        font.overrideFontsBold(txtGST);
        font.overrideFontsBold(findViewById(R.id.txtSubtotalLabel));
        font.overrideFontsBold(txtSubtotal);
        font.overrideFontsBold(txtDiscountLabel);
        font.overrideFontsBold(txtDiscount);
        font.overrideFontsBold(findViewById(R.id.txtTotalLabel));
        font.overrideFontsBold(txtTotal);
        font.overrideFontsBold(findViewById(R.id.txtBuyNow));
        font.overrideFontsBold(txtPaymentPrice);
        font.overrideFontsBold(txtDiscountOrVoucherLabel);
        font.overrideFontsBold(txtEVoucher);
        font.overrideFontsBold(findViewById(R.id.txtEVoucherLabel));
        font.overrideFontsBold(txtCoupon);
        font.overrideFontsBold(findViewById(R.id.txtCouponLabel));

        setUnderLineTextView(txtApply);
        setUnderLineTextView(txtDiscountOrVoucherLabel);

        //listenr keyboard
        final View activityRootView = findViewById(R.id.root);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                if (heightDiff > activityRootView.getRootView().getHeight() / 4) { //
                    //show keyboard
                    imvToggle.setVisibility(View.GONE);
                    txtDiscountOrVoucherLabel.setVisibility(View.GONE);
                } else {
                    //hide keyboard
                    imvToggle.setVisibility(View.VISIBLE);
                    txtDiscountOrVoucherLabel.setVisibility(View.VISIBLE);
                }
            }
        });

    }


    private void setUnderLineTextView(TextView textView) {
        SpannableString s = SpannableString.valueOf(textView.getText().toString());
        s.setSpan(new UnderlineSpan(), 0, textView.getText().toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(s);
    }

    private void initData() {
        listCart = new ArrayList<>();

        adapter = new OrderSummaryAdapter(this, listCart);
        listView.setAdapter(adapter);

        getCart(CustomerController.getCurrentCustomer(this));
        showProgressDialog(false);

        voucherItemList = new ArrayList<>();
        EVoucherAdapter.Callback callback = new EVoucherAdapter.Callback() {
            @Override
            public void check(int position) {
                for (int i = 0; i < voucherItemList.size(); i++) {
                    if (i == position) {
                        voucherItemList.get(i).setIsCheck(true);
                    } else {
                        voucherItemList.get(i).setIsCheck(false);
                    }
                }
                eVoucherAdapter.setListData(voucherItemList);
            }

            @Override
            public void unCheck(int position) {
                removeEVoucher(voucherItemList.get(position).getVoucherCode());
            }
        };
        eVoucherAdapter = new EVoucherAdapter(OrderSummaryActivity.this, voucherItemList, callback);
    }

    private void initSubTotalPrice() {
        CartDetail cartDetail = CartDetailController.getCurrentCartDetail(this);
        if (cartDetail != null) {
            float delivery_fee = cartDetail.getShipping_amount();
            BigDecimal b_delivery_fee = new BigDecimal(delivery_fee).setScale(2, BigDecimal.ROUND_HALF_UP);
//            float delivery_fee = cartDetail.getShipping_amount();
//            BigDecimal b_delivery_fee = new BigDecimal(cartDetail.getShipping_amount()).setScale(2, BigDecimal.ROUND_HALF_UP);
            txtDeliveryFee.setText("$" + b_delivery_fee);

            BigDecimal b_subtotal = new BigDecimal(cartDetail.getSubtotal()).setScale(2, BigDecimal.ROUND_HALF_UP);
            BigDecimal b_grandtotal = new BigDecimal(cartDetail.getGrand_total()).setScale(2, BigDecimal.ROUND_HALF_UP);
            txtSubtotal.setText("$" + StaticFunction.formatPrice(b_subtotal));
            txtTotal.setText("$" + StaticFunction.formatPrice(b_grandtotal));
            txtPaymentPrice.setText("$" + StaticFunction.formatPrice(b_grandtotal));

//            float discount = 0;
//            List<CartItem> cartItems = CartItemController.getAll(this);
//            for (CartItem cartItem : cartItems) {
//                discount += cartItem.getDiscount_amount();
//            }

            BigDecimal b_discount = new BigDecimal(cartDetail.getDiscount_amount()).setScale(2, BigDecimal.ROUND_HALF_UP);
            txtDiscount.setText("-$" + StaticFunction.formatPrice(b_discount));
            if (cartDetail.getDiscount_description().contains("eVoucher")) {
                BigDecimal b_evoucher = new BigDecimal(cartDetail.getUse_reward_points()).setScale(2, BigDecimal.ROUND_HALF_UP);
                txtEVoucher.setText("-$" + StaticFunction.formatPrice(b_evoucher));
            } else {
                txtEVoucher.setText("NA");
            }
            if (cartDetail.getCoupon_code().length() > 0) {
                BigDecimal b_coupon = new BigDecimal(cartDetail.getDiscount_amount() - cartDetail.getUse_reward_points()).setScale(2, BigDecimal.ROUND_HALF_UP);
                txtCoupon.setText(cartDetail.getCoupon_code() + " (" + "-$" + StaticFunction.formatPrice(b_coupon) + ")");
            } else {
                txtCoupon.setText("NA");
            }

            BigDecimal b_gst = new BigDecimal(cartDetail.getTax_amount()).setScale(2, BigDecimal.ROUND_HALF_UP);
            txtGST.setText("$" + StaticFunction.formatPrice(b_gst));

//            if (cartDetail.getCoupon_code().length() > 0) {
//                txtDiscountLabel.setText("DISCOUNT (" + cartDetail.getCoupon_code() + ")");
//            } else {
//                txtDiscountLabel.setText("DISCOUNT");
//            }
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
            case R.id.rltBuyNow:
                if (listCart.size() == 0) {
                    showPopupPrompt(getString(R.string.no_cart));
                } else {
//                    if (OrderSummary.payment_method.equalsIgnoreCase("paypalmobile")) {
//                        CartDetail cartDetail = CartDetailController.getCurrentCartDetail(this);
//                        if (cartDetail != null) {
//                            checkoutByPaypal(cartDetail.getGrand_total() + OrderSummary.delivery_fee);
//                        }
//                    } else {
//                        createOrder();
//                    }
                    OrderSummary.remark = edtRemark.getText().toString().trim();
                    Intent intent = new Intent(this, PaymentMethodActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.txtApply:
                String coupon_code = edtCoupon.getText().toString().trim();
                if (coupon_code.length() == 0) {
                    showToastError(getString(R.string.blank_coupon));
                } else {
                    applyCoupon(coupon_code);
                }
                break;
            case R.id.txtDiscountOrVoucherLabel:
                showPopupDiscountCodeOrEVoucher();
                break;
            case R.id.imvInfo:
                showPopupInfo();
                break;
            case R.id.imvToggle:
                if (isShowInfo) {
                    lnlInfo.startAnimation(animationDown);
//                    Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            lnlSubInfo.setVisibility(View.GONE);
//                            isShowInfo = false;
//                            imvToggle.setImageResource(R.drawable.ic_show_up);
//                        }
//                    }, animationTime);
                } else {
                    lnlInfo.startAnimation(animationUp);
                    lnlSubInfo.setVisibility(View.VISIBLE);
//                    Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            isShowInfo = true;
//                            imvToggle.setImageResource(R.drawable.ic_show_down);
//                        }
//                    }, animationTime);
                }
                break;
        }
    }

    public void showPopupDiscountCodeOrEVoucher() {
        // custom dialogDiscountCodeOrEVoucher
        dialogDiscountCodeOrEVoucher = new Dialog(this);

        dialogDiscountCodeOrEVoucher.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogDiscountCodeOrEVoucher.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogDiscountCodeOrEVoucher.setCanceledOnTouchOutside(true);
        dialogDiscountCodeOrEVoucher.setContentView(R.layout.popup_discount_code);

        final TextView txtDiscountCodeTab = (TextView) dialogDiscountCodeOrEVoucher.findViewById(R.id.txtDiscountCodeTab);
        final TextView txtEVoucherTab = (TextView) dialogDiscountCodeOrEVoucher.findViewById(R.id.txtEVoucherTab);
        final LinearLayout lnlEVoucherNumber = (LinearLayout) dialogDiscountCodeOrEVoucher.findViewById(R.id.lnlEVoucherNumber);
        TextView txtYouHaveLabel = (TextView) dialogDiscountCodeOrEVoucher.findViewById(R.id.txtYouHaveLabel);
        TextView txtEVoucherValue = (TextView) dialogDiscountCodeOrEVoucher.findViewById(R.id.txtEVoucherValue);
        TextView txtEVoucherLabel = (TextView) dialogDiscountCodeOrEVoucher.findViewById(R.id.txtEVoucherLabel);

        final LinearLayout lnlDiscountCode = (LinearLayout) dialogDiscountCodeOrEVoucher.findViewById(R.id.lnlDiscountCode);
        final TextView txtDiscountCode = (TextView) dialogDiscountCodeOrEVoucher.findViewById(R.id.txtDiscountCode);
        final TextView txtDiscountAmount = (TextView) dialogDiscountCodeOrEVoucher.findViewById(R.id.txtDiscountAmount);
        final LinearLayout lnlRemoveDiscount = (LinearLayout) dialogDiscountCodeOrEVoucher.findViewById(R.id.lnlRemoveDiscount);
        final TextView txtRemoveDiscount = (TextView) dialogDiscountCodeOrEVoucher.findViewById(R.id.txtRemoveDiscount);

        final EditText edtCouponOrVoucherCode = (EditText) dialogDiscountCodeOrEVoucher.findViewById(R.id.edtCouponOrVoucherCode);
        TextView txtOk = (TextView) dialogDiscountCodeOrEVoucher.findViewById(R.id.txtOk);
        TextView txtCancel = (TextView) dialogDiscountCodeOrEVoucher.findViewById(R.id.txtCancel);

        final RelativeLayout rltEVoucher = (RelativeLayout) dialogDiscountCodeOrEVoucher.findViewById(R.id.rltEVoucher);
        final ListView listView = (ListView) dialogDiscountCodeOrEVoucher.findViewById(R.id.listView);
        final ProgressBar progressBar = (ProgressBar) dialogDiscountCodeOrEVoucher.findViewById(R.id.progressBar);

        Font font = new Font(this);
        font.overrideFontsBold(txtOk);
        font.overrideFontsLight(txtCancel);
        font.overrideFontsBold(txtDiscountCodeTab);
        font.overrideFontsBold(txtEVoucherTab);
        font.overrideFontsLight(txtYouHaveLabel);
        font.overrideFontsBold(txtEVoucherValue);
        font.overrideFontsBold(txtEVoucherLabel);
        font.overrideFontsLight(edtCouponOrVoucherCode);
        font.overrideFontsLight(txtDiscountCode);
        font.overrideFontsBold(txtDiscountAmount);
        font.overrideFontsBold(txtRemoveDiscount);

        BigDecimal b_amount = new BigDecimal(CustomerController.getCurrentCustomer(this).getEvoucher_amount()).setScale(2, BigDecimal.ROUND_HALF_UP);
        txtEVoucherValue.setText("$" + b_amount);


        isDiscount = true;
        StaticFunction.forceShowKeyboard(OrderSummaryActivity.this);

        if (isDiscount) {
            lnlEVoucherNumber.setVisibility(View.GONE);
            rltEVoucher.setVisibility(View.GONE);
            edtCouponOrVoucherCode.setVisibility(View.VISIBLE);
            edtCouponOrVoucherCode.setHint("Enter Discount Code...");
            txtDiscountCodeTab.setBackgroundResource(R.drawable.tab_discount_chosen);
            txtDiscountCodeTab.setTextColor(getResources().getColor(R.color.white));
            txtEVoucherTab.setBackgroundResource(R.drawable.tab_evoucher_unchosen);
            txtEVoucherTab.setTextColor(getResources().getColor(R.color.main_color));
            lnlDiscountCode.setVisibility(View.VISIBLE);

            CartDetail cartDetail = CartDetailController.getCurrentCartDetail(OrderSummaryActivity.this);
            if (cartDetail != null) {
                if (cartDetail.getCoupon_code().length() > 0) {
                    lnlDiscountCode.setVisibility(View.VISIBLE);
                    txtDiscountCode.setText(cartDetail.getCoupon_code());
                    BigDecimal b_coupon = new BigDecimal(cartDetail.getDiscount_amount() - cartDetail.getUse_reward_points()).setScale(2, BigDecimal.ROUND_HALF_UP);
                    txtDiscountAmount.setText("-$" + StaticFunction.formatPrice(b_coupon));
                } else {
                    lnlDiscountCode.setVisibility(View.GONE);
                }
            }
        } else {
            lnlEVoucherNumber.setVisibility(View.VISIBLE);
            rltEVoucher.setVisibility(View.VISIBLE);
            edtCouponOrVoucherCode.setVisibility(View.GONE);
            edtCouponOrVoucherCode.setHint("Enter eVoucher...");
            txtDiscountCodeTab.setBackgroundResource(R.drawable.tab_discount_unchosen);
            txtDiscountCodeTab.setTextColor(getResources().getColor(R.color.main_color));
            txtEVoucherTab.setBackgroundResource(R.drawable.tab_evoucher_chosen);
            txtEVoucherTab.setTextColor(getResources().getColor(R.color.white));
            lnlDiscountCode.setVisibility(View.GONE);

            listView.setAdapter(eVoucherAdapter);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rltEVoucher.getLayoutParams();
            params.height = StaticFunction.getScreenHeight(OrderSummaryActivity.this) / 2;
            rltEVoucher.setLayoutParams(params);
        }

        txtDiscountCodeTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDiscount = true;
                StaticFunction.forceShowKeyboard(OrderSummaryActivity.this);
                if (isDiscount) {
                    lnlEVoucherNumber.setVisibility(View.GONE);
                    rltEVoucher.setVisibility(View.GONE);
                    edtCouponOrVoucherCode.setVisibility(View.VISIBLE);
                    edtCouponOrVoucherCode.setHint("Enter Discount Code...");
                    txtDiscountCodeTab.setBackgroundResource(R.drawable.tab_discount_chosen);
                    txtDiscountCodeTab.setTextColor(getResources().getColor(R.color.white));
                    txtEVoucherTab.setBackgroundResource(R.drawable.tab_evoucher_unchosen);
                    txtEVoucherTab.setTextColor(getResources().getColor(R.color.main_color));
                    lnlDiscountCode.setVisibility(View.VISIBLE);

                    CartDetail cartDetail = CartDetailController.getCurrentCartDetail(OrderSummaryActivity.this);
                    if (cartDetail != null) {
                        if (cartDetail.getCoupon_code().length() > 0) {
                            lnlDiscountCode.setVisibility(View.VISIBLE);
                            txtDiscountCode.setText(cartDetail.getCoupon_code());
                            BigDecimal b_coupon = new BigDecimal(cartDetail.getDiscount_amount() - cartDetail.getUse_reward_points()).setScale(2, BigDecimal.ROUND_HALF_UP);
                            txtDiscountAmount.setText("-$" + StaticFunction.formatPrice(b_coupon));
                        } else {
                            lnlDiscountCode.setVisibility(View.GONE);
                        }
                    }
                } else {
                    lnlEVoucherNumber.setVisibility(View.VISIBLE);
                    rltEVoucher.setVisibility(View.VISIBLE);
                    edtCouponOrVoucherCode.setVisibility(View.GONE);
                    edtCouponOrVoucherCode.setHint("Enter eVoucher...");
                    txtDiscountCodeTab.setBackgroundResource(R.drawable.tab_discount_unchosen);
                    txtDiscountCodeTab.setTextColor(getResources().getColor(R.color.main_color));
                    txtEVoucherTab.setBackgroundResource(R.drawable.tab_evoucher_chosen);
                    txtEVoucherTab.setTextColor(getResources().getColor(R.color.white));
                    lnlDiscountCode.setVisibility(View.GONE);
                }
            }
        });

        txtEVoucherTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDiscount = false;
                //should hide keyboard
                StaticFunction.forceHideKeyboard(OrderSummaryActivity.this,edtCouponOrVoucherCode);
                if (isDiscount) {
                    lnlEVoucherNumber.setVisibility(View.GONE);
                    rltEVoucher.setVisibility(View.GONE);
                    edtCouponOrVoucherCode.setVisibility(View.VISIBLE);
                    edtCouponOrVoucherCode.setHint("Enter Discount Code...");
                    txtDiscountCodeTab.setBackgroundResource(R.drawable.tab_discount_chosen);
                    txtDiscountCodeTab.setTextColor(getResources().getColor(R.color.white));
                    txtEVoucherTab.setBackgroundResource(R.drawable.tab_evoucher_unchosen);
                    txtEVoucherTab.setTextColor(getResources().getColor(R.color.main_color));
                    lnlDiscountCode.setVisibility(View.VISIBLE);

                    CartDetail cartDetail = CartDetailController.getCurrentCartDetail(OrderSummaryActivity.this);
                    if (cartDetail != null) {
                        if (cartDetail.getCoupon_code().length() > 0) {
                            lnlDiscountCode.setVisibility(View.VISIBLE);
                            txtDiscountCode.setText(cartDetail.getCoupon_code());
                            BigDecimal b_coupon = new BigDecimal(cartDetail.getDiscount_amount() - cartDetail.getUse_reward_points()).setScale(2, BigDecimal.ROUND_HALF_UP);
                            txtDiscountAmount.setText("-$" + StaticFunction.formatPrice(b_coupon));
                        } else {
                            lnlDiscountCode.setVisibility(View.GONE);
                        }
                    }
                } else {
                    lnlEVoucherNumber.setVisibility(View.VISIBLE);
                    rltEVoucher.setVisibility(View.VISIBLE);
                    edtCouponOrVoucherCode.setVisibility(View.GONE);
                    edtCouponOrVoucherCode.setHint("Enter eVoucher...");
                    txtDiscountCodeTab.setBackgroundResource(R.drawable.tab_discount_unchosen);
                    txtDiscountCodeTab.setTextColor(getResources().getColor(R.color.main_color));
                    txtEVoucherTab.setBackgroundResource(R.drawable.tab_evoucher_chosen);
                    txtEVoucherTab.setTextColor(getResources().getColor(R.color.white));
                    lnlDiscountCode.setVisibility(View.GONE);

//                    listView.setAdapter(eVoucherAdapter);
//                    progressBar.setVisibility(View.VISIBLE);
//                    voucherItemList.clear();
//                    eVoucherAdapter.setListData(voucherItemList);
//                    CARDENQUIRY(progressBar);

                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rltEVoucher.getLayoutParams();
                    params.height = StaticFunction.getScreenHeight(OrderSummaryActivity.this) / 2;
                    rltEVoucher.setLayoutParams(params);
                }
            }
        });

        lnlRemoveDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartDetail cartDetail = CartDetailController.getCurrentCartDetail(OrderSummaryActivity.this);
                if (cartDetail != null) {
                    removeCoupon(cartDetail.getCoupon_code());
                }
            }
        });

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StaticFunction.forceHideKeyboard(OrderSummaryActivity.this,edtCouponOrVoucherCode);
                dialogDiscountCodeOrEVoucher.dismiss();
            }
        });

        txtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StaticFunction.forceHideKeyboard(OrderSummaryActivity.this,edtCouponOrVoucherCode);
                if (isDiscount) {
                    String coupon_code = edtCouponOrVoucherCode.getText().toString().trim();
                    if (coupon_code.length() == 0) {
                        showToastError(getString(R.string.blank_coupon));
                    } else {
                        applyCoupon(coupon_code);
                    }
                } else {
//                    showToastInfo("Not Available Now");
                    boolean isChosen = false;
                    for (VoucherItem voucherItem : voucherItemList) {
                        if (voucherItem.isCheck()) {
                            isChosen = true;
                            break;
                        }
                    }
                    if (!isChosen) {
                        showToastInfo(getString(R.string.choose_evoucher));
                    } else {
                        for (VoucherItem voucherItem : voucherItemList) {
                            if (voucherItem.isCheck()) {
//                                VoucherRedeem(voucherItem);
                                applyEVoucher(voucherItem);
                                showProgressDialog(false);
                                break;
                            }
                        }
                    }
                }
            }
        });

        listView.setAdapter(eVoucherAdapter);
        progressBar.setVisibility(View.VISIBLE);
        voucherItemList.clear();
        eVoucherAdapter.setListData(voucherItemList);
        CARDENQUIRY(progressBar, txtEVoucherTab, txtEVoucherValue);

        dialogDiscountCodeOrEVoucher.show();
    }

    public void showPopupInfo() {
        // custom dialog
        final Dialog dialog = new Dialog(this);

//        dialog.getWindow().clearFlags(
//                WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_discount_info);

        ImageView imvClose = (ImageView) dialog.findViewById(R.id.imvClose);
        TextView txtEVoucher = (TextView) dialog.findViewById(R.id.txtEVoucher);
        TextView txtCoupon = (TextView) dialog.findViewById(R.id.txtCoupon);
        LinearLayout lnlListVoucher = (LinearLayout) dialog.findViewById(R.id.lnlListVoucher);

        Font font = new Font(this);
        font.overrideFontsBold(dialog.findViewById(R.id.root));

        CartDetail cartDetail = CartDetailController.getCurrentCartDetail(this);
        if (cartDetail != null) {
            if (cartDetail.getDiscount_description().contains("eVoucher")) {
                BigDecimal b_evoucher = new BigDecimal(cartDetail.getUse_reward_points()).setScale(2, BigDecimal.ROUND_HALF_UP);
                txtEVoucher.setText("-$" + StaticFunction.formatPrice(b_evoucher));
            } else {
                txtEVoucher.setText("$0.00");
            }
            if (cartDetail.getCoupon_code().length() > 0) {
                BigDecimal b_coupon = new BigDecimal(cartDetail.getDiscount_amount() - cartDetail.getUse_reward_points()).setScale(2, BigDecimal.ROUND_HALF_UP);
                txtCoupon.setText(cartDetail.getCoupon_code() + " (" + "-$" + StaticFunction.formatPrice(b_coupon) + ")");
            } else {
                txtCoupon.setText("$0.00");
            }

            List<String> eVoucherUsed = new ArrayList<>();
            List<String> eVoucherAmount = new ArrayList<>();
            try {
                JSONArray voucher_list = new JSONArray(cartDetail.getVoucher_list());
                for (int i = 0; i < voucher_list.length(); i++) {
                    JSONObject voucher = voucher_list.getJSONObject(i);
                    String voucher_code = voucher.getString("voucher_code");
                    double voucher_amount = voucher.getDouble("voucher_amount");
                    BigDecimal b_voucher_amount = new BigDecimal(voucher_amount).setScale(2, BigDecimal.ROUND_HALF_UP);
                    eVoucherUsed.add(voucher_code);
                    eVoucherAmount.add(b_voucher_amount + "");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            lnlListVoucher.removeAllViews();
            for (int i = 0; i < eVoucherUsed.size(); i++) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View viewRow = inflater.inflate(R.layout.item_popup_voucher_info, null);
                TextView txtEVoucherLabel = (TextView) viewRow.findViewById(R.id.txtEVoucherLabel);
                TextView txtEVoucherItem = (TextView) viewRow.findViewById(R.id.txtEVoucher);
                txtEVoucherLabel.setText(eVoucherUsed.get(i));
                txtEVoucherItem.setText("-$" + eVoucherAmount.get(i));
                font.overrideFontsBold(txtEVoucherLabel);
                font.overrideFontsBold(txtEVoucherItem);
                lnlListVoucher.addView(viewRow);
            }
        }

        imvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void applyCoupon(String coupon_code) {
        JSONObject object = new JSONObject();
        try {
            object.put("customer_id", CustomerController.getCurrentCustomer(this).getEntity_id());
            object.put("coupon_code", coupon_code);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        UICallback callback = new UICallback() {
            @Override
            public void onResult(boolean success, String data) {
                if (success) {
                    showToastOk("Apply coupon successfully!");
                    getCart(CustomerController.getCurrentCustomer(OrderSummaryActivity.this));
                    dialogDiscountCodeOrEVoucher.dismiss();
                } else {
                    showToastError(data);
                    hideProgressDialog();
                }
            }
        };
        CartExecute.applyCoupon(this, callback, CartDetailController.getCurrentCartDetail(this).getEntity_id(), object.toString());
        showProgressDialog(false);
    }

    private void removeCoupon(String coupon_code) {
        UICallback callback = new UICallback() {
            @Override
            public void onResult(boolean success, String data) {
                if (success) {
                    try {
                        JSONObject jData = new JSONObject(data);
                        JSONObject messages = jData.getJSONObject("messages");
                        JSONArray jSuccess = messages.getJSONArray("success");
                        if (jSuccess.length() > 0) {
                            JSONObject object = jSuccess.getJSONObject(0);
                            String message = object.getString("message");
                            showToastOk(message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    getCart(CustomerController.getCurrentCustomer(OrderSummaryActivity.this));
                    dialogDiscountCodeOrEVoucher.dismiss();
                } else {
                    showToastError(data);
                    hideProgressDialog();
                }
            }
        };
        CartExecute.removeCoupon(this, callback, CartDetailController.getCurrentCartDetail(this).getEntity_id(), coupon_code);
        showProgressDialog(false);
    }

    private void applyEVoucher(VoucherItem voucherItem) {
        JSONObject object = new JSONObject();
        try {
            object.put("customer_id", CustomerController.getCurrentCustomer(this).getEntity_id());
            object.put("evoucher_amount", voucherItem.getAmount());
            JSONArray voucher_list_array = new JSONArray();
            JSONObject voucher_list = new JSONObject();
            voucher_list.put("name", voucherItem.getVoucherName());
            voucher_list.put("code", voucherItem.getVoucherCode());
            voucher_list.put("amount", voucherItem.getAmount());
            voucher_list_array.put(voucher_list);
            object.put("voucher_list", voucher_list_array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        UICallback callback = new UICallback() {
            @Override
            public void onResult(boolean success, String data) {
                if (success) {
                    showToastOk("Apply coupon successfully!");
                    getCart(CustomerController.getCurrentCustomer(OrderSummaryActivity.this));
                    dialogDiscountCodeOrEVoucher.dismiss();
                } else {
                    showToastError(data);
                    hideProgressDialog();
                }
            }
        };
        CartExecute.applyEVoucher(this, callback, CartDetailController.getCurrentCartDetail(this).getEntity_id(), object.toString());
    }

    private void removeEVoucher(final String voucher_code) {
        UICallback callback = new UICallback() {
            @Override
            public void onResult(boolean success, String data) {
                if (success) {
                    try {
                        JSONObject jData = new JSONObject(data);
                        JSONObject messages = jData.getJSONObject("messages");
                        JSONArray jSuccess = messages.getJSONArray("success");
                        if (jSuccess.length() > 0) {
                            JSONObject object = jSuccess.getJSONObject(0);
                            String message = object.getString("message");
                            showToastOk(message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    getCartAfterRemoveEVoucher(voucher_code);
//                    getCart(CustomerController.getCurrentCustomer(OrderSummaryActivity.this));
//                    dialogDiscountCodeOrEVoucher.dismiss();
                } else {
                    showToastError(data);
                    hideProgressDialog();
                }
            }
        };
        CartExecute.removeEVoucher(this, callback, CartDetailController.getCurrentCartDetail(this).getEntity_id(), voucher_code);
        showProgressDialog(false);
    }

    private void getCart(Customer customer) {
        UICallback callback = new UICallback() {
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

                            CartDetailController.clearAll(OrderSummaryActivity.this);
                            CartDetailController.insertOrUpdate(OrderSummaryActivity.this, new CartDetail(entity_id, items_count, items_qty, subtotal, grand_total, coupon_code, customer_id, created_at, updated_at, f_tax_amount_cart, f_shipping_amount, f_discount_amount_b, f_use_reward_points, discount_description, voucher_list));

                            JSONArray array = cart.getJSONArray("cart_items");
                            CartItemController.clearAll(OrderSummaryActivity.this);
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

                                CartItemController.insertOrUpdate(OrderSummaryActivity.this, new CartItem(item_id, product_id, name, sku, manufacturer, price, image_url, qty, row_total, options, discount_amount, tax_amount, type_id));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showToastError(e.getMessage());
                    }
                } else {
                    showToastError(data);
                }
                listCart.clear();
                listCart.addAll(CartItemController.getAll(OrderSummaryActivity.this));
                adapter.setListData(listCart);
                initSubTotalPrice();
                hideProgressDialog();
            }
        };
        CartExecute.getCart(this, callback, customer.getEntity_id());
        showProgressDialog(false);
    }

    private void getCartAfterRemoveEVoucher(final String evoucher_code) {
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

                            CartDetailController.insertOrUpdate(OrderSummaryActivity.this, new CartDetail(entity_id, items_count, items_qty, subtotal, grand_total, coupon_code, customer_id, created_at, updated_at, f_tax_amount_cart, f_shipping_amount, f_discount_amount_b, f_use_reward_points, discount_description, voucher_list));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showToastError(e.getMessage());
                    }
                } else {
                    showToastError(data);
                }
                for (VoucherItem voucherItem : voucherItemList) {
                    if (voucherItem.getVoucherCode().equals(evoucher_code)) {
                        voucherItem.setIsCheck(false);
                        voucherItem.setIsUsed(false);
                        break;
                    }
                }
                eVoucherAdapter.setListData(voucherItemList);
                initSubTotalPrice();
                hideProgressDialog();
            }
        };
        CartExecute.getCart(this, callback, CustomerController.getCurrentCustomer(this).getEntity_id());
    }

    private void createOrder() {
        JSONObject order = new JSONObject();
        try {
            order.put("quote_id", CartDetailController.getCurrentCartDetail(this).getEntity_id());
            order.put("customer_id", CustomerController.getCurrentCustomer(this).getEntity_id());
            order.put("payment_method", OrderSummary.payment_method);
            order.put("shipping_method", OrderSummary.delivery_code);
            order.put("customer_note", edtRemark.getText().toString().trim());
            if (OrderSummary.payment_method.equalsIgnoreCase("paypalmobile")) {
                order.put("paypal_trans_id", PAY_ID);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        UICallback callback = new UICallback() {
            @Override
            public void onResult(boolean success, String data) {
                if (success) {
                    try {
                        JSONObject object = new JSONObject(data);
                        long increment_id = object.getLong("increment_id");
                        showToastOk("Your order has been successfully processed!");
                        CartDetailController.clearAll(OrderSummaryActivity.this);
                        CartItemController.clearAll(OrderSummaryActivity.this);
                        Customer customer = CustomerController.getCurrentCustomer(OrderSummaryActivity.this);
                        customer.setCart_items_count(0);
                        CustomerController.insertOrUpdate(OrderSummaryActivity.this, customer);
                        Intent intent = new Intent(OrderSummaryActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        Intent intentTransactionHistory = new Intent(OrderSummaryActivity.this, TransactionHistoryActivity.class);
                        startActivity(intentTransactionHistory);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showToastError(e.getMessage());
                    }
                    hideProgressDialog();
                    CARDENQUIRY();
                } else {
                    showToastError(data);
                    createOrder();
                }
            }
        };
        OrderExecute.createOrder(this, callback, order.toString());
        showProgressDialog(false);
    }

    private void CARDENQUIRY(final ProgressBar progressBar, final TextView txtEVoucherTab, final TextView txtEVoucherValue) {
        final List<String> eVoucherUsed = new ArrayList<>();
        try {
            JSONArray voucher_list = new JSONArray(CartDetailController.getCurrentCartDetail(this).getVoucher_list());
            for (int i = 0; i < voucher_list.length(); i++) {
                JSONObject voucher = voucher_list.getJSONObject(i);
                String voucher_code = voucher.getString("voucher_code");
                eVoucherUsed.add(voucher_code);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
                            JSONArray VoucherListArray = CardList.getJSONArray("VoucherList");
                            voucherItemList.clear();
                            double eVoucherTotal = 0;
                            for (int i = 0; i < VoucherListArray.length(); i++) {
                                JSONObject VoucherList = VoucherListArray.getJSONObject(i);
                                String ConversionLogID = VoucherList.getString("ConversionLogID");
                                if (ConversionLogID.equals("null")) ConversionLogID = "";
                                String VoucherCode = VoucherList.getString("VoucherCode");
                                int Type = VoucherList.getInt("Type");
                                String VoucherName = VoucherList.getString("VoucherName");
                                String ValidFrom = VoucherList.getString("ValidFrom");
                                String ValidTo = VoucherList.getString("ValidTo");
                                double Amount = VoucherList.getDouble("Amount");
                                eVoucherTotal += Amount;
                                double ValidSpendAmountFrom = VoucherList.getDouble("ValidSpendAmountFrom");
                                double ValidSpendAmountTo = VoucherList.getDouble("ValidSpendAmountTo");
                                int ValidQuantityFrom = VoucherList.getInt("ValidQuantityFrom");
                                int ValidQuantityTo = VoucherList.getInt("ValidQuantityTo");

                                boolean isUsed = false;
                                for (String voucher_code : eVoucherUsed) {
                                    if (voucher_code.equals(VoucherCode)) {
                                        isUsed = true;
                                        break;
                                    }
                                }

                                voucherItemList.add(new VoucherItem(ConversionLogID, VoucherCode, Type, VoucherName, ValidFrom, ValidTo, Amount, ValidSpendAmountFrom, ValidSpendAmountTo, ValidQuantityFrom, ValidQuantityTo, false, isUsed));
                            }
                            eVoucherAdapter.setListData(voucherItemList);
                            txtEVoucherTab.setText("eVOUCHER" + " (" + voucherItemList.size() + ")");
                            BigDecimal b_amount = new BigDecimal(eVoucherTotal).setScale(2, BigDecimal.ROUND_HALF_UP);
                            txtEVoucherValue.setText("$" + b_amount);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showToastError(e.getMessage());
                    }
                } else {
                    showToastError(data);
                }
                progressBar.setVisibility(View.GONE);
            }
        };
        CRMExecute.CARDENQUIRY(this, callback, object.toString());
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

                            Customer customer = CustomerController.getCurrentCustomer(OrderSummaryActivity.this);
                            customer.setReward_points(reward_points);
                            customer.setEvoucher_amount(eVoucherTotal);
                            CustomerController.insertOrUpdate(OrderSummaryActivity.this, customer);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
//                        showToastError(e.getMessage());
                    }
                } else {
//                    showToastError(data);
                }
            }
        };
        CRMExecute.CARDENQUIRY(this, callback, object.toString());
    }

    private void VoucherRedeem(final VoucherItem voucherItem) {
        JSONObject object = new JSONObject();
        try {
            JSONObject Shared = new JSONObject();
            JSONObject Data = new JSONObject();

            Shared.put("Type", "VoucherRedeem");
            Shared.put("AppCode", "shangcart");
            Shared.put("UserName", "app");
            Shared.put("Password", "123456");

            Data.put("Online", "1");
            Data.put("SalesDate", voucherItem.getValidFrom());
            Data.put("ReceiptNo", CartDetailController.getCurrentCartDetail(this).getEntity_id());
            Data.put("CardCode", CustomerController.getCurrentCustomer(this).getCard_code());
            Data.put("VerificationCode", "XXXX");

            JSONArray VoucherRedeemList = new JSONArray();
            JSONObject VoucherRedeemListItem = new JSONObject();
            VoucherRedeemListItem.put("VoucherCode", voucherItem.getVoucherCode());
            VoucherRedeemListItem.put("Remarks", "");
            VoucherRedeemList.put(VoucherRedeemListItem);

            Data.put("VoucherRedeemList", VoucherRedeemList);

            object.put("Shared", Shared);
            object.put("Data", Data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        UICallback callback = new UICallback() {
            @Override
            public void onResult(boolean success, String data) {
                if (success) {
                    applyEVoucher(voucherItem);
                    CARDENQUIRY();
                } else {
                    showToastError(data);
                    hideProgressDialog();
                }
            }
        };
        CRMExecute.VoucherRedeem(this, callback, object.toString());
    }

    //////////////// setup paypal
    private void initPaypal() {
        config = new PayPalConfiguration()
                .rememberUser(true)
                .acceptCreditCards(true)
                .environment(CONFIG_ENVIRONMENT)
                .clientId(CONFIG_CLIENT_ID)
//                        // The following are only used in PayPalFuturePaymentActivity.
                .merchantName("Example Merchant")
                .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
                .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));
    }

    private void checkoutByPaypal(float pay_price) {
        PayPalPayment thingToBuy = new PayPalPayment(new BigDecimal(pay_price), "SGD", "Shang", PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, com.paypal.android.sdk.payments.PaymentActivity.class);
        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_PAYMENT, thingToBuy);
        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm =
                        data.getParcelableExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                boolean isSuccess = false;
                if (confirm != null) {
                    try {
                        JSONObject jsonObject = confirm.toJSONObject();
                        JSONObject response = jsonObject.getJSONObject("response");
                        PAY_ID = response.getString("id");
                        isSuccess = true;
                        createOrder();
                    } catch (JSONException e) {
                        isSuccess = false;
                    }
                }
                if (isSuccess) {
//                    showToastOk("Check out successfully!" + "\n" + PAY_ID);
                } else {
                    showToastError("Check out unsuccessfully!");
                }
            } else {
                showToastError("Check out unsuccessfully!");
            }
        }
    }


    //////////////// setup paypal
}
