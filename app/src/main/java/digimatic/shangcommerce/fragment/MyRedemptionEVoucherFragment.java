package digimatic.shangcommerce.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.activity.BaseActivity;
import digimatic.shangcommerce.adapter.MyRedemptionEVoucherAdapter;
import digimatic.shangcommerce.connection.callback.UICallback;
import digimatic.shangcommerce.connection.threadconnection.execute.CRMExecute;
import digimatic.shangcommerce.daocontroller.CustomerController;
import digimatic.shangcommerce.model.VoucherItem;

/**
 * Created by USER on 05/04/2016.
 */
public class MyRedemptionEVoucherFragment extends Fragment {

    private ListView listView;
    private MyRedemptionEVoucherAdapter adapter;
    private List<VoucherItem> listData;

    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_redemption_evoucher, container, false);

        initView(view);
        initData();

        return view;
    }

    private void initView(View view) {
        listView = (ListView) view.findViewById(R.id.listView);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
    }

    private void initData() {
        listData = new ArrayList<>();
        adapter = new MyRedemptionEVoucherAdapter(getActivity(), listData);
        listView.setAdapter(adapter);

        CARDENQUIRY();
        progressBar.setVisibility(View.VISIBLE);
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

            Data.put("CardCode", CustomerController.getCurrentCustomer(getActivity()).getCard_code());
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
                                double ValidSpendAmountFrom = VoucherList.getDouble("ValidSpendAmountFrom");
                                double ValidSpendAmountTo = VoucherList.getDouble("ValidSpendAmountTo");
                                int ValidQuantityFrom = VoucherList.getInt("ValidQuantityFrom");
                                int ValidQuantityTo = VoucherList.getInt("ValidQuantityTo");

                                listData.add(new VoucherItem(ConversionLogID, VoucherCode, Type, VoucherName, ValidFrom, ValidTo, Amount, ValidSpendAmountFrom, ValidSpendAmountTo, ValidQuantityFrom, ValidQuantityTo, false, false));
                            }
                        }
                        adapter.setListData(listData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        ((BaseActivity) getActivity()).showToastError(e.getMessage());
                    }
                } else {
                    ((BaseActivity) getActivity()).showToastError(data);
                }
                progressBar.setVisibility(View.GONE);
            }
        };
        CRMExecute.CARDENQUIRY(getActivity(), callback, object.toString());
    }
}
