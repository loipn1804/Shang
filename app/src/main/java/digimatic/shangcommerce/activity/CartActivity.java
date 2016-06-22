package digimatic.shangcommerce.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.Gson;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.adapter.CartAdapter;
import digimatic.shangcommerce.callback.CartItemCallback;
import digimatic.shangcommerce.connection.callback.UICallback;
import digimatic.shangcommerce.connection.threadconnection.execute.CartExecute;
import digimatic.shangcommerce.daocontroller.CartDetailController;
import digimatic.shangcommerce.daocontroller.CartItemController;
import digimatic.shangcommerce.daocontroller.CustomerController;
import digimatic.shangcommerce.model.UpdateCart;
import digimatic.shangcommerce.staticfunction.Font;
import digimatic.shangcommerce.staticfunction.StaticFunction;
import greendao.CartDetail;
import greendao.CartItem;
import greendao.Customer;

/**
 * Created by USER on 3/7/2016.
 */
public class CartActivity extends BaseActivity implements View.OnClickListener, CartItemCallback {

    private SwipeMenuListView swipeMenuListView;
    private CartAdapter cartAdapter;
    private List<CartItem> listCart;

    private RelativeLayout rltBack;
    private TextView txtTitle;

    private TextView txtSubTotalPrice;

    private RelativeLayout rltBuyNow;

    private LinearLayout lnlEmptyData;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        sendEvent(getString(R.string.analytic_cart), getString(R.string.analytic_in));

        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sendEvent(getString(R.string.analytic_cart), getString(R.string.analytic_out));
    }

    private void initView() {
        rltBack = (RelativeLayout) findViewById(R.id.rltBack);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        swipeMenuListView = (SwipeMenuListView) findViewById(R.id.swipeMenuListView);
        rltBuyNow = (RelativeLayout) findViewById(R.id.rltBuyNow);

        txtSubTotalPrice = (TextView) findViewById(R.id.txtSubTotalPrice);

        lnlEmptyData = (LinearLayout) findViewById(R.id.lnlEmptyData);
        lnlEmptyData.setVisibility(View.GONE);

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

        rltBack.setOnClickListener(this);
        rltBuyNow.setOnClickListener(this);

        Font font = new Font(this);
        font.overrideFontsLight(findViewById(R.id.root));
        font.overrideFontsBold(txtTitle);
        font.overrideFontsBold(findViewById(R.id.txtBuyNow));
        font.overrideFontsBold(txtSubTotalPrice);

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
        font.overrideFontsBold(txtDiscountOrVoucherLabel);
        font.overrideFontsBold(txtEVoucher);
        font.overrideFontsBold(findViewById(R.id.txtEVoucherLabel));
        font.overrideFontsBold(txtCoupon);
        font.overrideFontsBold(findViewById(R.id.txtCouponLabel));

        txtDiscountOrVoucherLabel.setVisibility(View.GONE);
        edtRemark.setVisibility(View.GONE);
    }

    private void initData() {
        listCart = new ArrayList<>();

        cartAdapter = new CartAdapter(this, listCart, this);
        swipeMenuListView.setAdapter(cartAdapter);

        initSwipeMenuListView();

        getCart(CustomerController.getCurrentCustomer(this));
        showProgressDialog(false);
    }

    private void initSubTotalPrice() {
//        CartDetail cartDetail = CartDetailController.getCurrentCartDetail(this);
//        if (cartDetail != null) {
//            BigDecimal b_price = new BigDecimal(cartDetail.getGrand_total()).setScale(2, BigDecimal.ROUND_HALF_UP);
//            txtSubTotalPrice.setText("$" + StaticFunction.formatPrice(b_price));
//        } else {
//            txtSubTotalPrice.setText("");
//        }
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
            txtSubTotalPrice.setText("$" + StaticFunction.formatPrice(b_grandtotal));

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

    private void initSwipeMenuListView() {
        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(getResources().getDrawable(R.drawable.btn_delete));
                // set item width
                deleteItem.setWidth(getResources().getDimensionPixelOffset(R.dimen.dm_80dp));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete_cart_2);
//                deleteItem.set
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        // set creator
        swipeMenuListView.setMenuCreator(creator);

        // step 2. listener item click event
        swipeMenuListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        deleteProductFromCart(listCart.get(position).getItem_id());
                        break;
                }
                return false;
            }
        });

        // set SwipeListener
        swipeMenuListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });

        // set MenuStateChangeListener
        swipeMenuListView.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {
            @Override
            public void onMenuOpen(int position) {
            }

            @Override
            public void onMenuClose(int position) {
            }
        });

        // other setting
//        swipeMenuListView.setOpenInterpolator(new BounceInterpolator());

        // test item long click
        swipeMenuListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                Toast.makeText(getApplicationContext(), position + " long click", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
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
                    Intent intentShippingAddressDelivery = new Intent(this, ShippingAddressDeliveryActivity.class);
                    startActivity(intentShippingAddressDelivery);
                }
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

    @Override
    public void subQuantity(int position) {
        int n = listCart.get(position).getQty();
        if (n > 1) {
            n--;
            updateProductInCart(listCart.get(position), n);
        }
    }

    @Override
    public void addQuantity(int position) {
        int n = listCart.get(position).getQty();
        n++;
        updateProductInCart(listCart.get(position), n);
    }

    @Override
    public void updateQuantity(int quantity, int position) {
        updateProductInCart(listCart.get(position), quantity);
    }

    private void deleteProductFromCart(long item_id) {
        final Customer customer = CustomerController.getCurrentCustomer(this);
        UICallback callback = new UICallback() {
            @Override
            public void onResult(boolean success, String data) {
                getCart(customer);
            }
        };
        CartExecute.deleteProductFromCart(this, callback, item_id, customer.getEntity_id());
        showProgressDialog(false);
    }

    private void updateProductInCart(CartItem cartItem, int qty) {
        final Customer customer = CustomerController.getCurrentCustomer(this);
        UpdateCart updateCart = new UpdateCart(cartItem.getItem_id(), customer.getEntity_id(), cartItem.getProduct_id(), qty);
        String body = new Gson().toJson(updateCart);
        UICallback callback = new UICallback() {
            @Override
            public void onResult(boolean success, String data) {
                if (success) {
                    getCart(customer);
                } else {
                    showToastError(data);
                    hideProgressDialog();
                }
            }
        };
        CartExecute.updateProductInCart(this, callback, cartItem.getItem_id(), body);
        showProgressDialog(false);
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

    private void getCart(final Customer customer) {
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

                            CartDetailController.clearAll(CartActivity.this);
                            CartDetailController.insertOrUpdate(CartActivity.this, new CartDetail(entity_id, items_count, items_qty, subtotal, grand_total, coupon_code, customer_id, created_at, updated_at, f_tax_amount_cart, f_shipping_amount, f_discount_amount_b, f_use_reward_points, discount_description, voucher_list));

                            customer.setCart_items_count(items_qty);
                            CustomerController.insertOrUpdate(CartActivity.this, customer);

                            JSONArray array = cart.getJSONArray("cart_items");
                            CartItemController.clearAll(CartActivity.this);
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

                                CartItemController.insertOrUpdate(CartActivity.this, new CartItem(item_id, product_id, name, sku, manufacturer, price, image_url, qty, row_total, options, discount_amount, tax_amount, type_id));
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
                listCart.addAll(CartItemController.getAll(CartActivity.this));
                cartAdapter.setListData(listCart);
                if (listCart.size() == 0) {
                    lnlEmptyData.setVisibility(View.VISIBLE);
                } else {
                    lnlEmptyData.setVisibility(View.GONE);
                }
                initSubTotalPrice();
                hideProgressDialog();
            }
        };
        CartExecute.getCart(this, callback, customer.getEntity_id());
    }
}
