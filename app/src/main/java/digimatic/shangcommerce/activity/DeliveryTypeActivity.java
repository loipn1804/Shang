package digimatic.shangcommerce.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.adapter.DeliveryTypeAdapter;
import digimatic.shangcommerce.callback.DeliveryItemCallback;
import digimatic.shangcommerce.connection.callback.UICallback;
import digimatic.shangcommerce.connection.threadconnection.execute.CartExecute;
import digimatic.shangcommerce.daocontroller.CartDetailController;
import digimatic.shangcommerce.daocontroller.CustomerController;
import digimatic.shangcommerce.model.DeliveryType;
import digimatic.shangcommerce.model.OrderSummary;
import digimatic.shangcommerce.staticfunction.Font;

/**
 * Created by USER on 3/7/2016.
 */
public class DeliveryTypeActivity extends BaseActivity implements View.OnClickListener, DeliveryItemCallback {

    private RelativeLayout rltBack;
    private TextView txtTitle;
    private TextView txtContinue;

    private ListView listView;
    private DeliveryTypeAdapter deliveryTypeAdapter;
    private List<DeliveryType> listDelivery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

        sendEvent(getString(R.string.analytic_delivery_type), getString(R.string.analytic_in));

        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sendEvent(getString(R.string.analytic_delivery_type), getString(R.string.analytic_out));
    }

    private void initView() {
        rltBack = (RelativeLayout) findViewById(R.id.rltBack);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtContinue = (TextView) findViewById(R.id.txtContinue);
        listView = (ListView) findViewById(R.id.listView);

        rltBack.setOnClickListener(this);
        txtContinue.setOnClickListener(this);

        Font font = new Font(this);
        font.overrideFontsLight(findViewById(R.id.root));
        font.overrideFontsBold(txtTitle);
    }

    private void initData() {
        listDelivery = new ArrayList<>();

        deliveryTypeAdapter = new DeliveryTypeAdapter(this, listDelivery, this);
        listView.setAdapter(deliveryTypeAdapter);

        getDeliveryType();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltBack:
                finish();
                break;
            case R.id.txtContinue:
                boolean isChosen = false;
                for (DeliveryType deliveryType : listDelivery) {
                    if (deliveryType.isChosen) {
                        isChosen = true;
                        OrderSummary.delivery_code = deliveryType.getCode();
                        OrderSummary.delivery_fee = deliveryType.getPrice();
                        break;
                    }
                }
                if (isChosen) {
                    updateShippingMethod(OrderSummary.delivery_code);
                } else {
                    showToastError(getString(R.string.choose_delivery_type));
                }
                break;
        }
    }

    @Override
    public void onItemClick(int position) {
        for (int i = 0; i < listDelivery.size(); i++) {
            listDelivery.get(i).setIsChosen(false);
        }
        listDelivery.get(position).setIsChosen(true);
        deliveryTypeAdapter.setListData(listDelivery);
    }

    private void getDeliveryType() {
        UICallback callback = new UICallback() {
            @Override
            public void onResult(boolean success, String data) {
                if (success) {
                    try {
                        List<DeliveryType> list = new ArrayList<>();
                        JSONArray array = new JSONArray(data);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            String title = object.getString("title");
                            String code = object.getString("code");
                            String price = object.getString("price");
                            String logo = object.getString("logo");
                            float f_price = 0;
                            try {
                                f_price = Float.parseFloat(price);
                            } catch (Exception e) {

                            }
                            if (i == 0) {
                                list.add(new DeliveryType(title, code, f_price, logo, true));
                            } else {
                                list.add(new DeliveryType(title, code, f_price, logo, false));
                            }
                        }
                        listDelivery.clear();
                        listDelivery.addAll(list);
                        deliveryTypeAdapter.setListData(listDelivery);
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
        CartExecute.getDeliveryType(this, callback, CartDetailController.getCurrentCartDetail(this).getEntity_id());
        showProgressDialog(false);
    }

    private void updateShippingMethod(String shipping_method) {
        JSONObject object = new JSONObject();
        try {
            object.put("customer_id", CustomerController.getCurrentCustomer(this).getEntity_id());
            object.put("shipping_method", shipping_method);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        UICallback callback = new UICallback() {
            @Override
            public void onResult(boolean success, String data) {
                if (success) {
                    Intent intent = new Intent(DeliveryTypeActivity.this, OrderSummaryActivity.class);
                    startActivity(intent);
                } else {
                    showToastError(data);
                }
                hideProgressDialog();
            }
        };
        CartExecute.updateShippingMethod(this, callback, CartDetailController.getCurrentCartDetail(this).getEntity_id(), object.toString());
        showProgressDialog(false);
    }
}
