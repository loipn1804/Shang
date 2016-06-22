package digimatic.shangcommerce.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.adapter.ShippingDeliveryAddressAdapter;
import digimatic.shangcommerce.callback.ShippingDeliveryAddressItemCallback;
import digimatic.shangcommerce.connection.callback.UICallback;
import digimatic.shangcommerce.connection.threadconnection.execute.CartExecute;
import digimatic.shangcommerce.connection.threadconnection.execute.CustomerExecute;
import digimatic.shangcommerce.daocontroller.CartDetailController;
import digimatic.shangcommerce.daocontroller.CountryController;
import digimatic.shangcommerce.daocontroller.CustomerController;
import digimatic.shangcommerce.model.Address;
import digimatic.shangcommerce.model.OrderSummary;
import digimatic.shangcommerce.staticfunction.Font;
import greendao.Country;
import greendao.Customer;

/**
 * Created by USER on 3/16/2016.
 */
public class ShippingAddressDeliveryActivity extends BaseActivity implements View.OnClickListener, ShippingDeliveryAddressItemCallback {

    private int REQUEST_ADDRESS = 112;

    private ListView listView;
    private ShippingDeliveryAddressAdapter shippingDeliveryAddressAdapter;
    private List<Address> listAddress;
    private List<Boolean> listChosen;

    private RelativeLayout rltBack;
    private TextView txtTitle;
    private TextView txtContinue;

    private LayoutInflater layoutInflater;
    private View viewManageAddress;
    private LinearLayout lnlManageAddress;
    private TextView txtManageAddress;

    private LinearLayout lnlEmptyData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_address_delivery);

        initView();
        initData();
    }

    private void initView() {
        rltBack = (RelativeLayout) findViewById(R.id.rltBack);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtContinue = (TextView) findViewById(R.id.txtContinue);
        listView = (ListView) findViewById(R.id.listView);

        layoutInflater = LayoutInflater.from(this);
        viewManageAddress = layoutInflater.inflate(R.layout.view_manage_address, null);
        lnlManageAddress = (LinearLayout) viewManageAddress.findViewById(R.id.lnlManageAddress);
        txtManageAddress = (TextView) viewManageAddress.findViewById(R.id.txtManageAddress);

        lnlEmptyData = (LinearLayout) findViewById(R.id.lnlEmptyData);
        lnlEmptyData.setVisibility(View.GONE);

        rltBack.setOnClickListener(this);
        txtContinue.setOnClickListener(this);
        lnlManageAddress.setOnClickListener(this);

        Font font = new Font(this);
        font.overrideFontsLight(findViewById(R.id.root));
        font.overrideFontsBold(txtTitle);
        font.overrideFontsBold(txtManageAddress);
    }

    private void initData() {
        listAddress = new ArrayList<>();
        listChosen = new ArrayList<>();

        shippingDeliveryAddressAdapter = new ShippingDeliveryAddressAdapter(this, listAddress, listChosen, this);
        listView.addFooterView(viewManageAddress);
        listView.setAdapter(shippingDeliveryAddressAdapter);

        if(CountryController.getAll(this).size() == 0) {
            getCountries();
            showProgressDialog(false);
        } else {
            getAddressList();
            showProgressDialog(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltBack:
                finish();
                break;
            case R.id.txtContinue:
                if (listAddress.size() == 0) {
                    showToastInfo(getString(R.string.no_address));
                } else {
                    for (int i = 0; i < listChosen.size(); i++) {
                        if (listChosen.get(i)) {
                            OrderSummary.shipping_address_id = listAddress.get(i).getEntity_id();
                            updateShippingAddress(OrderSummary.shipping_address_id + "");
                            break;
                        }
                    }
                }
                break;
            case R.id.lnlManageAddress:
                Intent intent = new Intent(ShippingAddressDeliveryActivity.this, SettingAddressActivity.class);
                startActivityForResult(intent, REQUEST_ADDRESS);
                break;
        }
    }

    public void showPopupAddAddress(String message) {
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
                Intent intent = new Intent(ShippingAddressDeliveryActivity.this, SettingAddressActivity.class);
                startActivityForResult(intent, REQUEST_ADDRESS);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onChosen(int position) {
        for (int i = 0; i < listChosen.size(); i++) {
            listChosen.set(i, false);
        }
        listChosen.set(position, true);
        shippingDeliveryAddressAdapter.setListData(listAddress, listChosen);
    }

    private void updateShippingAddress(String shipping_address_id) {
        JSONObject object = new JSONObject();
        try {
            object.put("customer_id", CustomerController.getCurrentCustomer(this).getEntity_id());
            object.put("shipping_address_id", shipping_address_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        UICallback callback = new UICallback() {
            @Override
            public void onResult(boolean success, String data) {
                if (success) {
                    Intent intent = new Intent(ShippingAddressDeliveryActivity.this, BillingAddressDeliveryActivity.class);
                    startActivity(intent);
                } else {
                    showToastError(data);
                }
                hideProgressDialog();
            }
        };
        CartExecute.updateShippingAddress(this, callback, CartDetailController.getCurrentCartDetail(this).getEntity_id(), object.toString());
        showProgressDialog(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ADDRESS) {
            getAddressList();
            showProgressDialog(false);
        }
    }

    private void getCountries() {
        UICallback callback = new UICallback() {
            @Override
            public void onResult(boolean success, String data) {
                if (success) {
                    try {
                        JSONArray array = new JSONArray(data);
                        CountryController.clearAll(ShippingAddressDeliveryActivity.this);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            String value = object.getString("value");
                            String label = object.getString("label");
                            int set_default = object.getInt("default");

                            Country country = new Country(value, label, set_default);
                            CountryController.insertOrUpdate(ShippingAddressDeliveryActivity.this, country);
                        }
                        getAddressList();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    hideProgressDialog();
                }
            }
        };
        CustomerExecute.getCountries(this, callback);
    }

    private void getAddressList() {
        Customer customer = CustomerController.getCurrentCustomer(this);
        UICallback callback = new UICallback() {
            @Override
            public void onResult(boolean success, String data) {
                if (success) {
                    try {
                        List<Address> addressList = new ArrayList<>();
                        Address defaultAddress = null;
                        JSONArray array = new JSONArray(data);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            long entity_id = object.getLong("entity_id");
                            String firstname = object.optString("firstname", "");
                            String middlename = object.optString("middlename", "");
                            if (middlename.equalsIgnoreCase("null")) middlename = "";
                            String lastname = object.optString("lastname", "");
                            String company = object.optString("company", "");
                            if (company.equalsIgnoreCase("null")) company = "";
                            String city = object.optString("city", "");
                            String country_id = object.optString("country_id", "");
                            String region = object.optString("region", "");
                            if (region.equalsIgnoreCase("null")) region = "";
                            String postcode = object.optString("postcode", "");
                            String telephone = object.optString("telephone", "");
                            String fax = object.optString("fax", "");
                            if (fax.equalsIgnoreCase("null")) fax = "";
                            JSONArray street_arr = object.getJSONArray("street");
                            String street = "";
                            String street_2 = "";
                            for (int j = 0; j < street_arr.length(); j++) {
                                if (j == 0) {
                                    street = street_arr.getString(j);
                                } else {
                                    street_2 = street_arr.getString(j);
                                }
                            }
                            int is_default_billing = object.optInt("is_default_billing", 0);
                            int is_default_shipping = object.optInt("is_default_shipping", 0);

                            Address address = new Address(entity_id, firstname, middlename, lastname,
                                    company, city, country_id, region, postcode, telephone, fax,
                                    street, street_2, is_default_billing, is_default_shipping);
                            if (is_default_shipping == 1) {
                                defaultAddress = address;
                            } else {
                                addressList.add(address);
                            }
                        }
                        listAddress.clear();
                        listAddress.addAll(addressList);
                        if (defaultAddress != null) {
                            listAddress.add(0, defaultAddress);
                        }
                        listChosen.clear();
                        for (int i = 0; i < listAddress.size(); i++) {
                            if (i == 0) {
                                listChosen.add(true);
                            } else {
                                listChosen.add(false);
                            }
                        }
                        shippingDeliveryAddressAdapter.setListData(listAddress, listChosen);
                        if (listAddress.size() == 0) {
                            lnlEmptyData.setVisibility(View.VISIBLE);
                        } else {
                            lnlEmptyData.setVisibility(View.GONE);
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
        CustomerExecute.getAddressList(this, callback, customer.getEntity_id());
    }
}
