package digimatic.shangcommerce.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.activity.BaseActivity;
import digimatic.shangcommerce.adapter.ListRewardAdapter;
import digimatic.shangcommerce.connection.callback.UICallback;
import digimatic.shangcommerce.connection.threadconnection.execute.CRMExecute;
import digimatic.shangcommerce.daocontroller.CustomerController;
import digimatic.shangcommerce.model.ItemListReward;

/**
 * Created by USER on 4/4/2016.
 */
public class ListRewardFragment extends Fragment {

    //    private TwoWayView recyclerView;
    private RecyclerView recyclerView;
    private ListRewardAdapter adapter;
    private List<ItemListReward> listData;

    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_reward, container, false);

        initView(view);
        initData();

        GetItemList();

        return view;
    }

    private void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.twoWayView);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        progressBar.setVisibility(View.GONE);
    }

    private void initData() {
        listData = new ArrayList<>();
        listData.add(new ItemListReward("", "", "", "", "", "", 0, 0, "", "", 0));

        adapter = new ListRewardAdapter(getActivity(), listData);
        recyclerView.setAdapter(adapter);
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void GetItemList() {
        JSONObject object = new JSONObject();
        try {
            JSONObject Shared = new JSONObject();
            JSONObject Data = new JSONObject();

            Shared.put("Type", "GetItemList");
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
                        JSONArray ItemListArray = Data.getJSONArray("ItemList");
                        for (int i = 0; i < ItemListArray.length(); i++) {
                            JSONObject ItemList = ItemListArray.getJSONObject(i);
                            String RedeemRuleID = ItemList.getString("RedeemRuleID");
                            String ItemCode = ItemList.getString("ItemCode");
                            String ItemName = ItemList.getString("ItemName");
                            String ItemDescription = ItemList.getString("ItemDescription");
                            String ItemURL = ItemList.getString("ItemURL");
                            String ItemImageURL = ItemList.getString("ItemImageURL");
                            int MinRewardtoDeduct = ItemList.getInt("MinRewardtoDeduct");
                            int MaxRewardtoDeduct = ItemList.getInt("MaxRewardtoDeduct");
                            String ActiveFrom = ItemList.getString("ActiveFrom");
                            String ActiveTo = ItemList.getString("ActiveTo");
                            int MaxRedeem = ItemList.getInt("MaxRedeem");

                            listData.add(new ItemListReward(RedeemRuleID, ItemCode, ItemName, ItemDescription, ItemURL, ItemImageURL, MinRewardtoDeduct, MaxRewardtoDeduct, ActiveFrom, ActiveTo, MaxRedeem));
                        }
                        adapter.setListData(listData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        ((BaseActivity) getActivity()).showToastError(e.getMessage());
                    }
                } else {
                    ((BaseActivity) getActivity()).showToastError(data);
                }
            }
        };
        CRMExecute.GetItemList(getActivity(), callback, object.toString());
    }
}
