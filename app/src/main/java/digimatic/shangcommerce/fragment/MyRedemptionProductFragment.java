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
import digimatic.shangcommerce.adapter.MyRedemptionProductAdapter;
import digimatic.shangcommerce.connection.callback.UICallback;
import digimatic.shangcommerce.connection.threadconnection.execute.CRMExecute;
import digimatic.shangcommerce.daocontroller.CustomerController;
import digimatic.shangcommerce.model.RedeemHistoryItem;

/**
 * Created by USER on 05/04/2016.
 */
public class MyRedemptionProductFragment extends Fragment {

    private ListView listView;
    private MyRedemptionProductAdapter adapter;
    private List<RedeemHistoryItem> listData;

    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_redemption_product, container, false);

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
        adapter = new MyRedemptionProductAdapter(getActivity(), listData);
        listView.setAdapter(adapter);

        GetRedeemHistory();
        progressBar.setVisibility(View.VISIBLE);
    }

    private void GetRedeemHistory() {
        JSONObject object = new JSONObject();
        try {
            JSONObject Shared = new JSONObject();
            JSONObject Data = new JSONObject();

            Shared.put("Type", "GetRedeemHistory");
            Shared.put("APPcode", "IOS");
            Shared.put("StoreCode", "Default");
            Shared.put("POSID", "Default");
            Shared.put("CashierCode", "cs001");
            Shared.put("UserName", "app");
            Shared.put("Password", "123456");

            Data.put("CardCode", CustomerController.getCurrentCustomer(getActivity()).getCard_code());

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
                        JSONArray RedeemHistoryListArray = Data.getJSONArray("RedeemHistoryList");
                        for (int i = 0; i < RedeemHistoryListArray.length(); i++) {
                            JSONObject RedeemHistoryList = RedeemHistoryListArray.getJSONObject(i);
                            String RedeemLogID = RedeemHistoryList.getString("RedeemLogID");
                            int RedeemType = RedeemHistoryList.getInt("RedeemType");
                            String RedeemTime = RedeemHistoryList.getString("RedeemTime");
                            String ItemCode = RedeemHistoryList.getString("ItemCode");
                            String ItemName = RedeemHistoryList.getString("ItemName");
                            double ItemValue = RedeemHistoryList.getDouble("ItemValue");
                            String ItemImageURL = RedeemHistoryList.getString("ItemImageURL");
                            int RedeemedQuantity = RedeemHistoryList.getInt("RedeemedQuantity");
                            double RewardDeduct = RedeemHistoryList.getDouble("RewardDeduct");
                            int RewardType = RedeemHistoryList.getInt("RewardType");

                            listData.add(new RedeemHistoryItem(RedeemLogID, RedeemType, RedeemTime, ItemCode, ItemName, ItemValue, ItemImageURL, RedeemedQuantity, RewardDeduct, RewardType));
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
        CRMExecute.GetRedeemHistory(getActivity(), callback, object.toString());
    }
}
