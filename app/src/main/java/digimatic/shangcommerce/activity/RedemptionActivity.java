package digimatic.shangcommerce.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.adapter.RedeemableAdapter;
import digimatic.shangcommerce.connection.callback.UICallback;
import digimatic.shangcommerce.connection.threadconnection.execute.CRMExecute;
import digimatic.shangcommerce.daocontroller.CartDetailController;
import digimatic.shangcommerce.daocontroller.CustomerController;
import digimatic.shangcommerce.model.RedeemableItem;
import digimatic.shangcommerce.model.VoucherItem;
import digimatic.shangcommerce.staticfunction.Font;
import greendao.Customer;

/**
 * Created by USER on 4/4/2016.
 */
public class RedemptionActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rltBack;
    private TextView txtTitle;
    private TextView txtApply;

    private TextView txtPTS;
    private TextView txtEVoucher;

    private ImageView imvAvatar;
    private TextView txtUsername;

    private ImageView imvQuestion;

    private ListView listView;
    private List<RedeemableItem> listData;
    private RedeemableAdapter adapter;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redemption);

//        sendEvent(getString(R.string.analytic_account), getString(R.string.analytic_in));

        options = new DisplayImageOptions.Builder()
                .displayer(new RoundedBitmapDisplayer(500))
                .showImageForEmptyUri(R.drawable.avatar_default)
                .showImageOnLoading(R.drawable.avatar_default)
                .cacheOnDisk(true).build();

        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        sendEvent(getString(R.string.analytic_account), getString(R.string.analytic_out));
    }

    private void initView() {
        rltBack = (RelativeLayout) findViewById(R.id.rltBack);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtApply = (TextView) findViewById(R.id.txtApply);
        imvAvatar = (ImageView) findViewById(R.id.imvAvatar);
        txtUsername = (TextView) findViewById(R.id.txtUsername);
        txtPTS = (TextView) findViewById(R.id.txtPTS);
        txtEVoucher = (TextView) findViewById(R.id.txtEVoucher);

        imvQuestion = (ImageView) findViewById(R.id.imvQuestion);

        listView = (ListView) findViewById(R.id.listView);

        rltBack.setOnClickListener(this);
        imvQuestion.setOnClickListener(this);
        txtApply.setOnClickListener(this);

        txtApply.setVisibility(View.GONE);

        Font font = new Font(this);
        font.overrideFontsLight(findViewById(R.id.root));
        font.overrideFontsBold(txtTitle);
        font.overrideFontsBold(findViewById(R.id.txtUsername));
        font.overrideFontsBold(txtPTS);
        font.overrideFontsBold(txtEVoucher);
        font.overrideFontsBold(findViewById(R.id.txtPTSLabel));
        font.overrideFontsBold(findViewById(R.id.txtEVoucherLabel));
    }

    private void initData() {
        Customer customer = CustomerController.getCurrentCustomer(this);
        txtUsername.setText(customer.getFirstname() + " " + customer.getLastname());

        listData = new ArrayList<>();
        adapter = new RedeemableAdapter(this, listData, callback);
        listView.setAdapter(adapter);

        GetRedeemableVoucherList();
        showProgressDialog(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltBack:
                finish();
                break;
            case R.id.imvQuestion:
                showPopupRedeemInfo();
                break;
            case R.id.txtApply:

                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (CustomerController.isLogin(this)) {
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

            txtPTS.setText(customer.getReward_points() + "");
            BigDecimal b_eVoucherTotal = new BigDecimal(customer.getEvoucher_amount()).setScale(2, BigDecimal.ROUND_HALF_UP);
            txtEVoucher.setText("$" + b_eVoucherTotal);
        } else {
            finish();
        }
    }

    public void showPopupRedeemInfo() {
        // custom dialog
        final Dialog dialog = new Dialog(this);

//        dialog.getWindow().clearFlags(
//                WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_redeem_info);

        ImageView imvClose = (ImageView) dialog.findViewById(R.id.imvClose);

        Font font = new Font(this);
        font.overrideFontsLight(dialog.findViewById(R.id.root));

        imvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void GetRedeemableVoucherList() {
        JSONObject object = new JSONObject();
        try {
            JSONObject Shared = new JSONObject();
            JSONObject Data = new JSONObject();

            Shared.put("Type", "GetRedeemableVoucherList");
            Shared.put("APPcode", "IOS");
            Shared.put("StoreCode", "Default");
            Shared.put("POSID", "Default");
            Shared.put("CashierCode", "cs001");
            Shared.put("UserName", "app");
            Shared.put("Password", "123456");

            Data.put("CardCode", CustomerController.getCurrentCustomer(this).getCard_code());

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
                        JSONArray RedeemableListArray = Data.getJSONArray("RedeemableList");
                        listData.clear();
                        for (int i = 0; i < RedeemableListArray.length(); i++) {
                            JSONObject RedeemableList = RedeemableListArray.getJSONObject(i);
                            String RedeemRuleDetailID = RedeemableList.getString("RedeemRuleDetailID");
                            double Amount = RedeemableList.getDouble("Amount");
                            String VoucherTypeCode = RedeemableList.getString("VoucherTypeCode");
                            String VoucherName = RedeemableList.getString("VoucherName");
                            double RewardsRequired = RedeemableList.getDouble("RewardsRequired");
                            double RewardsRequiredMax = RedeemableList.getDouble("RewardsRequiredMax");
                            String ActiveFrom = RedeemableList.getString("ActiveFrom");
                            String ActiveTo = RedeemableList.getString("ActiveTo");
                            int MaxRedeems = RedeemableList.getInt("MaxRedeems");

                            listData.add(new RedeemableItem(RedeemRuleDetailID, Amount, VoucherTypeCode, VoucherName, RewardsRequired, RewardsRequiredMax, ActiveFrom, ActiveTo, MaxRedeems, false, 0));
                        }
                        adapter.setListData(listData);
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
        CRMExecute.GetRedeemableVoucherList(this, callback, object.toString());
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
                            Customer customer = CustomerController.getCurrentCustomer(RedemptionActivity.this);
                            customer.setReward_points(reward_points);
                            customer.setEvoucher_amount(eVoucherTotal);
                            CustomerController.insertOrUpdate(RedemptionActivity.this, customer);
                            txtPTS.setText(customer.getReward_points() + "");
                            BigDecimal b_eVoucherTotal = new BigDecimal(customer.getEvoucher_amount()).setScale(2, BigDecimal.ROUND_HALF_UP);
                            txtEVoucher.setText("$" + b_eVoucherTotal);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showToastError(e.getMessage());
                    }
                } else {
                    showToastError(data);
                }
                GetRedeemableVoucherList();
            }
        };
        CRMExecute.CARDENQUIRY(this, callback, object.toString());
    }

    private void VoucherConversion(int position, int qty) {
        JSONObject object = new JSONObject();
        try {
            JSONObject Shared = new JSONObject();
            JSONObject Data = new JSONObject();

            Shared.put("Type", "VoucherConversion");
            Shared.put("APPcode", "IOS");
            Shared.put("StoreCode", "Default");
            Shared.put("POSID", "Default");
            Shared.put("CashierCode", "cs001");
            Shared.put("UserName", "app");
            Shared.put("Password", "123456");

            Data.put("CardCode", CustomerController.getCurrentCustomer(this).getCard_code());

            JSONArray VoucherConversionRequestList = new JSONArray();

            RedeemableItem redeemableItem = listData.get(position);
            JSONObject VoucherConversionRequest = new JSONObject();
            VoucherConversionRequest.put("RedeemRuleDetailID", redeemableItem.getRedeemRuleDetailID());
            VoucherConversionRequest.put("VoucherTypeCode", redeemableItem.getVoucherTypeCode());
            VoucherConversionRequest.put("QuantitytoRedem", qty);

            VoucherConversionRequestList.put(VoucherConversionRequest);

            Data.put("VoucherConversionRequestList", VoucherConversionRequestList);

            object.put("Shared", Shared);
            object.put("Data", Data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        UICallback callback = new UICallback() {
            @Override
            public void onResult(boolean success, String data) {
                if (success) {
                    showToastOk(getString(R.string.convert_evoucher_success));
//                    GetRedeemableVoucherList();
                    CARDENQUIRY();
                } else {
                    showToastError(data);
                    hideProgressDialog();
                }
            }
        };
        CRMExecute.VoucherConversion(this, callback, object.toString());
        showProgressDialog(false);
    }

    private RedeemableAdapter.Callback callback = new RedeemableAdapter.Callback() {
        @Override
        public void enterQuantity(int position) {
            showPopupEnterQuantity(position);
        }

        @Override
        public void unCheck(int position) {
            listData.get(position).setQuantity(0);
            listData.get(position).setIsCheck(false);
            adapter.setListData(listData);
        }
    };

    public void showPopupEnterQuantity(final int position) {
        // custom dialog
        final Dialog dialog = new Dialog(this);

//        dialog.getWindow().clearFlags(
//                WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_enter_quantity_evoucher);

        TextView txtMessage = (TextView) dialog.findViewById(R.id.txtMessage);
        TextView txtQuantityLabel = (TextView) dialog.findViewById(R.id.txtQuantityLabel);
        TextView txtQuantity = (TextView) dialog.findViewById(R.id.txtQuantity);
        final EditText edtQuantity = (EditText) dialog.findViewById(R.id.edtQuantity);
        TextView txtOk = (TextView) dialog.findViewById(R.id.txtOk);
        TextView txtCancel = (TextView) dialog.findViewById(R.id.txtCancel);
        txtQuantity.setText(listData.get(position).getMaxRedeems() + "");
        edtQuantity.setText(listData.get(position).getQuantity() + "");

        Font font = new Font(this);
        font.overrideFontsBold(txtMessage);
        font.overrideFontsLight(txtQuantityLabel);
        font.overrideFontsBold(txtQuantity);
        font.overrideFontsBold(edtQuantity);
        font.overrideFontsLight(txtCancel);
        font.overrideFontsBold(txtOk);

        edtQuantity.requestFocus();
        edtQuantity.setSelection(edtQuantity.getText().length());

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        txtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = edtQuantity.getText().toString().trim();
                if (str.length() == 0) {
                    showToastError(getString(R.string.invalidate_input));
                } else {
                    try {
                        int qty = Integer.parseInt(str);
                        if (qty == 0) {
                            showToastError(getString(R.string.quantity_zero));
                        } else if (qty > listData.get(position).getMaxRedeems()) {
                            showToastError(getString(R.string.quantity_exceed));
                        } else {
                            VoucherConversion(position, qty);
                            dialog.dismiss();
                        }
                    } catch (NumberFormatException e) {
                        showToastError(getString(R.string.invalidate_input));
                    }
                }
            }
        });

        dialog.show();
    }
}
