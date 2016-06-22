package digimatic.shangcommerce.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.adapter.ProductTransactionHistoryAdapter;
import digimatic.shangcommerce.connection.callback.UICallback;
import digimatic.shangcommerce.connection.threadconnection.execute.OrderExecute;
import digimatic.shangcommerce.model.OrderHistoryItem;
import digimatic.shangcommerce.staticfunction.Font;
import digimatic.shangcommerce.staticfunction.StaticFunction;

/**
 * Created by USER on 3/8/2016.
 */
public class TransactionDetailActivity extends BaseActivity implements View.OnClickListener {

    private ScrollView scrollView;
    private ListView listView;
    private ProductTransactionHistoryAdapter productTransactionHistoryAdapter;
    private List<OrderHistoryItem> listProduct;

    private RelativeLayout rltBack;
    private TextView txtTitle;

    private TextView txtGrandTotal;
    private TextView txtStoreName;
    private TextView txtCreatedAt;
    private TextView txtPaymentTypeLabel;
    private TextView txtPaymentType;
    private TextView txtDeliveryFeeLabel;
    private TextView txtDeliveryFee;
    private TextView txtGSTLabel;
    private TextView txtGST;
    private TextView txtEVoucherLabel;
    private TextView txtEVoucher;
    private TextView txtDiscountLabel;
    private TextView txtDiscount;
    private TextView txtBillingLabel;
    private TextView txtBilling;
    private TextView txtShippingLabel;
    private TextView txtShipping;
    private TextView txtRemark;

    private ImageView imvPending;
    private TextView txtPending;
    private ImageView imvProcessing;
    private TextView txtProcessing;
    private ImageView imvComplete;
    private TextView txtComplete;

    private long entity_id;
//    private String increment_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);

        if (getIntent().hasExtra("entity_id")) {
            entity_id = getIntent().getLongExtra("entity_id", 0);
//            increment_id = getIntent().getStringExtra("increment_id");
            if (entity_id == 0) {
                finish();
            }
        } else {
            finish();
        }

        initView();
        initData();
    }

    private void initView() {
        rltBack = (RelativeLayout) findViewById(R.id.rltBack);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        listView = (ListView) findViewById(R.id.listView);

        txtGrandTotal = (TextView) findViewById(R.id.txtGrandTotal);
        txtStoreName = (TextView) findViewById(R.id.txtStoreName);
        txtCreatedAt = (TextView) findViewById(R.id.txtCreatedAt);
        txtPaymentTypeLabel = (TextView) findViewById(R.id.txtPaymentTypeLabel);
        txtPaymentType = (TextView) findViewById(R.id.txtPaymentType);
        txtDeliveryFeeLabel = (TextView) findViewById(R.id.txtDeliveryFeeLabel);
        txtDeliveryFee = (TextView) findViewById(R.id.txtDeliveryFee);
        txtGSTLabel = (TextView) findViewById(R.id.txtGSTLabel);
        txtGST = (TextView) findViewById(R.id.txtGST);
        txtEVoucherLabel = (TextView) findViewById(R.id.txtEVoucherLabel);
        txtEVoucher = (TextView) findViewById(R.id.txtEVoucher);
        txtDiscountLabel = (TextView) findViewById(R.id.txtDiscountLabel);
        txtDiscount = (TextView) findViewById(R.id.txtDiscount);
        txtBillingLabel = (TextView) findViewById(R.id.txtBillingLabel);
        txtBilling = (TextView) findViewById(R.id.txtBilling);
        txtShippingLabel = (TextView) findViewById(R.id.txtShippingLabel);
        txtShipping = (TextView) findViewById(R.id.txtShipping);
        txtRemark = (TextView) findViewById(R.id.txtRemark);

        imvPending = (ImageView) findViewById(R.id.imvPending);
        txtPending = (TextView) findViewById(R.id.txtPending);
        imvProcessing = (ImageView) findViewById(R.id.imvProcessing);
        txtProcessing = (TextView) findViewById(R.id.txtProcessing);
        imvComplete = (ImageView) findViewById(R.id.imvComplete);
        txtComplete = (TextView) findViewById(R.id.txtComplete);

        rltBack.setOnClickListener(this);

        Font font = new Font(this);
        font.overrideFontsLight(findViewById(R.id.root));
        font.overrideFontsBold(txtTitle);
        font.overrideFontsBold(txtPaymentTypeLabel);
        font.overrideFontsBold(txtPaymentType);
        font.overrideFontsBold(txtDeliveryFeeLabel);
        font.overrideFontsBold(txtDeliveryFee);
        font.overrideFontsBold(txtGSTLabel);
        font.overrideFontsBold(txtGST);
        font.overrideFontsBold(txtEVoucherLabel);
        font.overrideFontsBold(txtEVoucher);
        font.overrideFontsBold(txtDiscountLabel);
        font.overrideFontsBold(txtDiscount);
        font.overrideFontsBold(txtBillingLabel);
        font.overrideFontsBold(txtShippingLabel);
        font.overrideFontsBold(findViewById(R.id.txtRemarkLabel));

//        txtTitle.setText("#" + increment_id);
        txtTitle.setText("");

        txtProcessing.setVisibility(View.INVISIBLE);
        txtComplete.setVisibility(View.INVISIBLE);
    }

    private void initData() {
        listProduct = new ArrayList<>();

        productTransactionHistoryAdapter = new ProductTransactionHistoryAdapter(this, listProduct);
        listView.setAdapter(productTransactionHistoryAdapter);
        setListViewHeightBasedOnChildren(listView);

        getOrderDetail();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltBack:
                finish();
                break;
        }
    }

    private void getOrderDetail() {
        UICallback callback = new UICallback() {
            @Override
            public void onResult(boolean success, String data) {
                if (success) {
                    try {
                        JSONObject order_detail = new JSONObject(data);

                        float grand_total = Float.parseFloat(order_detail.optString("grand_total", "0"));
                        String store_name = order_detail.getString("store_name");
                        String created_at = order_detail.getString("created_at");
                        String coupon_code = order_detail.getString("coupon_code");
                        String increment_id = order_detail.getString("increment_id");
                        float shipping_amount = Float.parseFloat(order_detail.optString("shipping_amount", "0"));
                        if (coupon_code.equalsIgnoreCase("null")) coupon_code = "";
                        float tax_amount = Float.parseFloat(order_detail.optString("tax_amount", "0"));
                        String payment_method = order_detail.getString("payment_method");

                        txtTitle.setText("#" + increment_id);
                        txtPaymentType.setText(payment_method);
                        BigDecimal b_delivery_fee = new BigDecimal(shipping_amount).setScale(2, BigDecimal.ROUND_HALF_UP);
                        txtDeliveryFee.setText("$" + StaticFunction.formatPrice(b_delivery_fee));
                        BigDecimal b_tax_amount = new BigDecimal(tax_amount).setScale(2, BigDecimal.ROUND_HALF_UP);
                        txtGST.setText("$" + StaticFunction.formatPrice(b_tax_amount));
                        BigDecimal b_total = new BigDecimal(grand_total).setScale(2, BigDecimal.ROUND_HALF_UP);
                        txtGrandTotal.setText("$" + StaticFunction.formatPrice(b_total));
                        txtStoreName.setText(store_name);
                        txtCreatedAt.setText(StaticFunction.convertYYYYMMDDtoAppFormat(created_at));

                        String discount_amount = order_detail.getString("discount_amount");
                        if (discount_amount.equalsIgnoreCase("null")) discount_amount = "0";
                        float f_discount_amount = Math.abs(Float.parseFloat(discount_amount));

                        String reward_salesrule_points = order_detail.getString("reward_salesrule_points");
                        if (reward_salesrule_points.equalsIgnoreCase("null")) reward_salesrule_points = "0";
                        float f_reward_salesrule_points = Math.abs(Float.parseFloat(reward_salesrule_points));

                        String discount_description = order_detail.getString("discount_description");

                        if (discount_description.contains("eVoucher")) {
                            BigDecimal b_evoucher = new BigDecimal(f_reward_salesrule_points).setScale(2, BigDecimal.ROUND_HALF_UP);
                            txtEVoucher.setText("-$" + StaticFunction.formatPrice(b_evoucher));
                        } else {
                            txtEVoucher.setText("$0.00");
                        }

                        if (coupon_code.length() > 0) {
                            BigDecimal b_coupon = new BigDecimal(f_discount_amount - f_reward_salesrule_points).setScale(2, BigDecimal.ROUND_HALF_UP);
                            txtDiscount.setText(coupon_code + " (" + "-$" + StaticFunction.formatPrice(b_coupon) + ")");
                        } else {
                            txtDiscount.setText("$0.00");
                        }

                        String customer_note = order_detail.getString("customer_note");
                        if (customer_note.equalsIgnoreCase("null") || customer_note.length() == 0) {
                            txtRemark.setText("$0.00");
                        } else {
                            txtRemark.setText(customer_note);
                        }

                        JSONObject addresses = order_detail.getJSONObject("addresses");

                        JSONObject billing = addresses.getJSONObject("billing");
                        JSONArray billing_street = billing.getJSONArray("street");
                        String billing_address = "";
                        for (int a = 0; a < billing_street.length(); a++) {
                            billing_address += billing_street.getString(a) + " ";
                        }
                        billing_address += billing.getString("city") + " ";
                        billing_address += billing.getString("postcode");
                        txtBilling.setText(billing_address);

                        JSONObject shipping = addresses.getJSONObject("shipping");
                        JSONArray shipping_street = shipping.getJSONArray("street");
                        String shipping_address = "";
                        for (int a = 0; a < shipping_street.length(); a++) {
                            shipping_address += shipping_street.getString(a) + " ";
                        }
                        shipping_address += shipping.getString("city") + " ";
                        shipping_address += shipping.getString("postcode");
                        txtShipping.setText(shipping_address);

                        JSONArray array = order_detail.getJSONArray("order_items");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            long item_id = object.getLong("item_id");
                            long product_id = object.getLong("product_id");
                            String type_id = object.getString("type_id");
                            String name = object.getString("name");
                            String sku = object.getString("sku");
                            String manufacturer = object.optString("manufacturer", "");
                            float price = Float.parseFloat(object.optString("price", "0"));
                            String image_url = object.getString("image_url");
                            int qty_ordered = object.optInt("qty_ordered", 0);
                            float row_total = Float.parseFloat(object.optString("row_total", "0"));
                            String options = object.getString("options");

                            OrderHistoryItem orderHistoryItem = new OrderHistoryItem(item_id, product_id, type_id, name, sku, manufacturer, price, image_url, qty_ordered, row_total, created_at, options);

                            listProduct.add(orderHistoryItem);
                        }
                        productTransactionHistoryAdapter.setListData(listProduct);
                        setListViewHeightBasedOnChildren(listView);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showToastError(e.getMessage());
                    }
                } else

                {
                    showToastError(data);
                }

                hideProgressDialog();
            }
        };
        OrderExecute.getOrderDetail(this, callback, entity_id);

        showProgressDialog(false);

    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });
    }
}
