package digimatic.shangcommerce.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.adapter.CountryAdapter;
import digimatic.shangcommerce.adapter.OptionAdapter;
import digimatic.shangcommerce.connection.callback.UICallback;
import digimatic.shangcommerce.connection.threadconnection.execute.CustomerExecute;
import digimatic.shangcommerce.daocontroller.CountryController;
import digimatic.shangcommerce.model.Address;
import digimatic.shangcommerce.model.ProductOptionItem;
import digimatic.shangcommerce.staticfunction.Font;
import greendao.Country;

/**
 * Created by USER on 3/9/2016.
 */
public class AddressDetailActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rltBack;
    private TextView txtTitle;
    private TextView txtSave;

    private EditText edtFirstName;
    private EditText edtMiddleName;
    private EditText edtLastName;
    private EditText edtCompany;
    private EditText edtTelephone;
    private EditText edtFax;
    private EditText edtStreet;
    private EditText edtStreet_2;
    private EditText edtCity;
    private RelativeLayout rltCountry;
    private TextView txtCountry;
    private EditText edtRegion;
    private EditText edtPostcode;

    private String data;
    private long address_id;

    private boolean is_updated = false;

    private String country_id;

    private List<Country> listCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_detail);

        if (getIntent().hasExtra("data")) {
            data = getIntent().getStringExtra("data");
            initView();
            initData();
        } else {
            finish();
        }
    }

    private void initView() {
        rltBack = (RelativeLayout) findViewById(R.id.rltBack);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtSave = (TextView) findViewById(R.id.txtSave);

        edtFirstName = (EditText) findViewById(R.id.edtFirstName);
        edtMiddleName = (EditText) findViewById(R.id.edtMiddleName);
        edtLastName = (EditText) findViewById(R.id.edtLastName);
        edtCompany = (EditText) findViewById(R.id.edtCompany);
        edtTelephone = (EditText) findViewById(R.id.edtTelephone);
        edtFax = (EditText) findViewById(R.id.edtFax);
        edtStreet = (EditText) findViewById(R.id.edtStreet);
        edtStreet_2 = (EditText) findViewById(R.id.edtStreet_2);
        edtCity = (EditText) findViewById(R.id.edtCity);
        rltCountry = (RelativeLayout) findViewById(R.id.rltCountry);
        txtCountry = (TextView) findViewById(R.id.txtCountry);
        edtRegion = (EditText) findViewById(R.id.edtRegion);
        edtPostcode = (EditText) findViewById(R.id.edtPostcode);

        rltBack.setOnClickListener(this);
        txtSave.setOnClickListener(this);
        rltCountry.setOnClickListener(this);

        Font font = new Font(this);
        font.overrideFontsLight(findViewById(R.id.root));
        font.overrideFontsBold(txtTitle);
    }

    private void initData() {
        listCountry = new ArrayList<>();
        getCountries();
    }

    private void setData() {
        try {
            JSONObject object = new JSONObject(data);
            long entity_id = object.getLong("entity_id");
            address_id = entity_id;
            String firstname = object.optString("firstname", "");
            String middlename = object.optString("middlename", "");
            String lastname = object.optString("lastname", "");
            String company = object.optString("company", "");
            String city = object.optString("city", "");
            country_id = object.optString("country_id", "");
            String region = object.optString("region", "");
            String postcode = object.optString("postcode", "");
            String telephone = object.optString("telephone", "");
            String fax = object.optString("fax", "");
            String street = object.optString("street", "");
            String street_2 = object.optString("street_2", "");
            int is_default_billing = object.optInt("is_default_billing", 0);
            int is_default_shipping = object.optInt("is_default_shipping", 0);

            edtFirstName.setText(firstname);
            edtMiddleName.setText(middlename);
            edtLastName.setText(lastname);
            edtCompany.setText(company);
            edtTelephone.setText(telephone);
            edtFax.setText(fax);
            edtStreet.setText(street);
            edtStreet_2.setText(street_2);
            edtCity.setText(city);
            initCountry();
            edtRegion.setText(region);
            edtPostcode.setText(postcode);
        } catch (JSONException e) {
            e.printStackTrace();
            showToastError(e.getMessage());
            finish();
        }
    }

    private void initCountry() {
        for (Country country : listCountry) {
            if (country.getValue().equals(country_id)) {
                txtCountry.setText(country.getLabel());
                break;
            }
        }
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
            case R.id.txtSave:
                updateAddress();
                break;
            case R.id.rltCountry:
                if (listCountry.size() > 0) {
                    showPopupListCountry(listCountry);
                }
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

    private void showPopupListCountry(final List<Country> list) {
        final Dialog dialog = new Dialog(this);

//        dialog.getWindow().clearFlags(
//                WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_list_option);

        ListView listView = (ListView) dialog.findViewById(R.id.listView);
        CountryAdapter adapter = new CountryAdapter(this, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                txtCountry.setText(list.get(position).getLabel());
                country_id = list.get(position).getValue();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void updateAddress() {
        String firstname = edtFirstName.getText().toString().trim();
        String middlename = edtMiddleName.getText().toString().trim();    // no require
        String lastname = edtLastName.getText().toString().trim();
        String company = edtCompany.getText().toString().trim();          // no require
        String telephone = edtTelephone.getText().toString().trim();
        String fax = edtFax.getText().toString().trim();                  // no require
        String street = edtStreet.getText().toString().trim();
        String street_2 = edtStreet_2.getText().toString().trim();        // no require
        String city = edtCity.getText().toString().trim();
        String region = edtRegion.getText().toString().trim();            // no require
        String postcode = edtPostcode.getText().toString().trim();
        if (firstname.length() == 0) {
            showToastError(getString(R.string.blank_firstname));
        } else if (lastname.length() == 0) {
            showToastError(getString(R.string.blank_lastname));
        } else if (telephone.length() == 0) {
            showToastError(getString(R.string.blank_telephone));
        } else if (street.length() == 0) {
            showToastError(getString(R.string.blank_street));
        } else if (city.length() == 0) {
            showToastError(getString(R.string.blank_city));
        } else if (postcode.length() == 0) {
            showToastError(getString(R.string.blank_postcode));
        } else {
            String body = "<?xml version=\"1.0\"?>";
            body += "<magento_api>";
            body += "<firstname>" + firstname + "</firstname>";
            body += "<middlename>" + middlename + "</middlename>";
            body += "<lastname>" + lastname + "</lastname>";
            body += "<company>" + company + "</company>";
            body += "<telephone>" + telephone + "</telephone>";
            body += "<fax>" + fax + "</fax>";
            body += "<street>";
            body += "<data_item>" + street + "</data_item>";
            body += "<data_item>" + street_2 + "</data_item>";
            body += "</street>";
            body += "<city>" + city + "</city>";
            body += "<country_id>" + country_id + "</country_id>";
            body += "<region>" + region + "</region>";
            body += "<postcode>" + postcode + "</postcode>";
            body += "</magento_api>";
            UICallback callback = new UICallback() {
                @Override
                public void onResult(boolean success, String data) {
                    if (success) {
                        is_updated = true;
                        showToastOk("Update address successfully!");
                    } else {
                        showToastError("Update address fail!");
                    }
                    hideProgressDialog();
                }
            };
            CustomerExecute.updateAddress(this, callback, address_id, body);
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
                        listCountry.clear();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            String value = object.getString("value");
                            String label = object.getString("label");
                            int set_default = object.getInt("default");

                            listCountry.add(new Country(value, label, set_default));
                        }
                        setData();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {

                }
                hideProgressDialog();
            }
        };
        CustomerExecute.getCountries(this, callback);
        showProgressDialog(false);
    }
}
