package digimatic.shangcommerce.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.adapter.AddressAdapter;
import digimatic.shangcommerce.callback.AddressItemCallback;
import digimatic.shangcommerce.connection.callback.UICallback;
import digimatic.shangcommerce.connection.threadconnection.execute.CustomerExecute;
import digimatic.shangcommerce.daocontroller.CountryController;
import digimatic.shangcommerce.daocontroller.CustomerController;
import digimatic.shangcommerce.model.Address;
import digimatic.shangcommerce.staticfunction.Font;
import greendao.Country;
import greendao.Customer;

/**
 * Created by USER on 3/9/2016.
 */
public class SettingAddressActivity extends BaseActivity implements View.OnClickListener, AddressItemCallback {

    private int REQUEST_UPDATE = 111;

    private SwipeMenuListView swipeMenuListView;
    private AddressAdapter addressAdapter;
    private List<Address> listAddress;

    private RelativeLayout rltBack;
    private TextView txtTitle;
    private TextView txtAdd;

    private LinearLayout lnlAddressShippingDefault;
    private TextView txtDefaultShippingName;
    private TextView txtDefaultShippingAddress;

    private LinearLayout lnlAddressBillingDefault;
    private TextView txtDefaultBillingName;
    private TextView txtDefaultBillingAddress;

    private LinearLayout lnlEmptyData;

    private boolean is_updated = false;

    private LayoutInflater layoutInflater;
    private View viewHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_address);

        initView();
        initData();
    }

    private void initView() {
        rltBack = (RelativeLayout) findViewById(R.id.rltBack);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtAdd = (TextView) findViewById(R.id.txtAdd);
        swipeMenuListView = (SwipeMenuListView) findViewById(R.id.swipeMenuListView);

        layoutInflater = LayoutInflater.from(this);
        viewHeader = layoutInflater.inflate(R.layout.view_header_list_address, null);

        lnlAddressShippingDefault = (LinearLayout) viewHeader.findViewById(R.id.lnlAddressShippingDefault);
        txtDefaultShippingName = (TextView) viewHeader.findViewById(R.id.txtDefaultShippingName);
        txtDefaultShippingAddress = (TextView) viewHeader.findViewById(R.id.txtDefaultShippingAddress);

        lnlAddressBillingDefault = (LinearLayout) viewHeader.findViewById(R.id.lnlAddressBillingDefault);
        txtDefaultBillingName = (TextView) viewHeader.findViewById(R.id.txtDefaultBillingName);
        txtDefaultBillingAddress = (TextView) viewHeader.findViewById(R.id.txtDefaultBillingAddress);

        rltBack.setOnClickListener(this);
        lnlAddressShippingDefault.setOnClickListener(this);
        lnlAddressBillingDefault.setOnClickListener(this);
        txtAdd.setOnClickListener(this);

        lnlEmptyData = (LinearLayout) findViewById(R.id.lnlEmptyData);
        lnlEmptyData.setVisibility(View.GONE);

        Font font = new Font(this);
        font.overrideFontsLight(findViewById(R.id.root));
        font.overrideFontsBold(txtTitle);

        font.overrideFontsLight(viewHeader);
        font.overrideFontsBold(txtDefaultShippingName);
        font.overrideFontsBold(txtDefaultBillingName);
        font.overrideFontsBold(viewHeader.findViewById(R.id.txtDefaultShippingLabel));
        font.overrideFontsBold(viewHeader.findViewById(R.id.txtDefaultBillingLabel));
        font.overrideFontsBold(viewHeader.findViewById(R.id.txtYourAddressLabel));
        font.overrideFontsBold(viewHeader.findViewById(R.id.txtSetShippingDefault));
        font.overrideFontsBold(viewHeader.findViewById(R.id.txtSetBillingDefault));

        lnlAddressShippingDefault.setVisibility(View.GONE);
        lnlAddressBillingDefault.setVisibility(View.GONE);
    }

    private void initData() {
        listAddress = new ArrayList<>();

        addressAdapter = new AddressAdapter(this, listAddress, this);
        swipeMenuListView.addHeaderView(viewHeader);
        swipeMenuListView.setAdapter(addressAdapter);

        initSwipeMenuListView();

        getCountries();
        showProgressDialog(false);
    }

    private void getCountries() {
        UICallback callback = new UICallback() {
            @Override
            public void onResult(boolean success, String data) {
                if (success) {
                    try {
                        JSONArray array = new JSONArray(data);
                        CountryController.clearAll(SettingAddressActivity.this);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            String value = object.getString("value");
                            String label = object.getString("label");
                            int set_default = object.getInt("default");

                            Country country = new Country(value, label, set_default);
                            CountryController.insertOrUpdate(SettingAddressActivity.this, country);
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

    public static void setListViewHeightBasedOnChildren(ListView listView) {
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
    }

    private void setDefaultAddress() {
        Address defaultShippingAddress = getDefaultShippingAddress();
        if (defaultShippingAddress != null) {
            lnlAddressShippingDefault.setVisibility(View.VISIBLE);
            String name = defaultShippingAddress.getFirstname() + " " + (defaultShippingAddress.getMiddlename().length() > 0 ? defaultShippingAddress.getMiddlename() + " " : "") + defaultShippingAddress.getLastname();
            txtDefaultShippingName.setText(name);
            String address = defaultShippingAddress.getStreet() + (defaultShippingAddress.getStreet_2().length() > 0 ? " " + defaultShippingAddress.getStreet_2() : "");
            address += "\n" + defaultShippingAddress.getCity() + " " + defaultShippingAddress.getPostcode();
            address += "\n" + CountryController.getCountryLabel(this, defaultShippingAddress.getCountry_id());
            address += "\n" + "Phone: " + defaultShippingAddress.getTelephone();
            txtDefaultShippingAddress.setText(address);
        } else {
            lnlAddressShippingDefault.setVisibility(View.GONE);
        }

        Address defaultBillingAddress = getDefaultBillingAddress();
        if (defaultBillingAddress != null) {
            lnlAddressBillingDefault.setVisibility(View.VISIBLE);
            String name = defaultBillingAddress.getFirstname() + " " + (defaultBillingAddress.getMiddlename().length() > 0 ? defaultBillingAddress.getMiddlename() + " " : "") + defaultBillingAddress.getLastname();
            txtDefaultBillingName.setText(name);
            String address = defaultBillingAddress.getStreet() + (defaultBillingAddress.getStreet_2().length() > 0 ? " " + defaultBillingAddress.getStreet_2() : "");
            address += "\n" + defaultBillingAddress.getCity() + " " + defaultBillingAddress.getPostcode();
            address += "\n" + CountryController.getCountryLabel(this, defaultBillingAddress.getCountry_id());
            address += "\n" + "Phone: " + defaultBillingAddress.getTelephone();
            txtDefaultBillingAddress.setText(address);
        } else {
            lnlAddressBillingDefault.setVisibility(View.GONE);
        }
    }

    private Address getDefaultShippingAddress() {
        for (Address address : listAddress) {
            if (address.getIs_default_shipping() == 1) {
                return address;
            }
        }
        return null;
    }

    private Address getDefaultBillingAddress() {
        for (Address address : listAddress) {
            if (address.getIs_default_billing() == 1) {
                return address;
            }
        }
        return null;
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
                        Address address = listAddress.get(position);
                        if (address.is_default_shipping == 1 || address.is_default_billing == 1) {
                            showToastError(getString(R.string.can_not_delete_default_address));
                        } else {
                            deleteAddress(listAddress.get(position).getEntity_id());
                        }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltBack:
                if (is_updated) {
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    finish();
                }
                break;
//            case R.id.lnlAddressDefault:
//                Intent intentDetail = new Intent(this, AddressDetailActivity.class);
//                String data = new Gson().toJson(defaultAddress);
//                intentDetail.putExtra("data", data);
//                startActivityForResult(intentDetail, REQUEST_UPDATE);
//                break;
            case R.id.txtAdd:
                int set_default = 0;
                if (listAddress.size() > 0) {
                    set_default = 0;
                } else {
                    set_default = 1;
                }
                Intent intentCreate = new Intent(this, CreateAddressActivity.class);
                intentCreate.putExtra("set_default", set_default);
                startActivityForResult(intentCreate, REQUEST_UPDATE);
                break;
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (is_updated) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        } else {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_UPDATE && resultCode == RESULT_OK) {
            getAddressList();
            is_updated = true;
            showProgressDialog(false);
        }
    }

    private void getAddressList() {
        Customer customer = CustomerController.getCurrentCustomer(this);
        UICallback callback = new UICallback() {
            @Override
            public void onResult(boolean success, String data) {
                if (success) {
                    try {
                        List<Address> addressList = new ArrayList<>();
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
                            addressList.add(address);
                        }
                        listAddress.clear();
                        listAddress.addAll(addressList);
                        addressAdapter.setListData(listAddress);
                        if (listAddress.size() == 0) {
                            lnlEmptyData.setVisibility(View.VISIBLE);
                            swipeMenuListView.setVisibility(View.GONE);
                        } else {
                            lnlEmptyData.setVisibility(View.GONE);
                            swipeMenuListView.setVisibility(View.VISIBLE);
                        }
                        setDefaultAddress();
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
//        showProgressDialog(false);
    }

    @Override
    public void setDefault(int position) {
        showPopupMakeDefaultAddress(position);
    }

    public void showPopupMakeDefaultAddress(final int position) {
        // custom dialog
        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_make_default_address);

        TextView txtMessage = (TextView) dialog.findViewById(R.id.txtMessage);
        ImageView imvShipping = (ImageView) dialog.findViewById(R.id.imvShipping);
        ImageView imvBilling = (ImageView) dialog.findViewById(R.id.imvBilling);

        Font font = new Font(this);
        font.overrideFontsLight(txtMessage);

        final Address address = listAddress.get(position);
        if (address.getIs_default_shipping() == 1) {
            imvShipping.setImageResource(R.drawable.popup_shipping_choose);
        } else {
            imvShipping.setImageResource(R.drawable.popup_shipping_unchoose);
        }
        if (address.getIs_default_billing() == 1) {
            imvBilling.setImageResource(R.drawable.popup_billing_choose);
        } else {
            imvBilling.setImageResource(R.drawable.popup_billing_unchoose);
        }

        imvShipping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAddress(address.getEntity_id(), true, 1);
                dialog.dismiss();
            }
        });

        imvBilling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAddress(address.getEntity_id(), false, 1);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void updateAddress(long address_id, boolean isShipping, int def) {
        String body = "<?xml version=\"1.0\"?>";
        body += "<magento_api>";
        if (isShipping) {
            body += "<is_default_shipping>" + def + "</is_default_shipping>";
        } else {
            body += "<is_default_billing>" + def + "</is_default_billing>";
        }
        body += "</magento_api>";
        UICallback callback = new UICallback() {
            @Override
            public void onResult(boolean success, String data) {
                if (success) {
                    getAddressList();
                    is_updated = true;
                    showToastOk("Update address successfully!");
                } else {
                    showToastError("Update address fail!");
                    hideProgressDialog();
                }
            }
        };
        CustomerExecute.updateAddress(this, callback, address_id, body);
        showProgressDialog(false);
    }

    private void deleteAddress(long address_id) {
        UICallback callback = new UICallback() {
            @Override
            public void onResult(boolean success, String data) {
                if (success) {
                    getAddressList();
                    is_updated = true;
//                    showToastOk("Update address successfully!");
                } else {
//                    showToastError("Update address fail!");
                    hideProgressDialog();
                }
            }
        };
        CustomerExecute.deleteAddress(this, callback, address_id);
        showProgressDialog(false);
    }
}
