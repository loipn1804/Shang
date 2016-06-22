package digimatic.shangcommerce.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.activity.BaseActivity;
import digimatic.shangcommerce.adapter.TransactionHistoryAdapter;
import digimatic.shangcommerce.connection.callback.UICallback;
import digimatic.shangcommerce.connection.threadconnection.execute.OrderExecute;
import digimatic.shangcommerce.daocontroller.CustomerController;
import digimatic.shangcommerce.model.OrderHistory;
import digimatic.shangcommerce.staticfunction.Font;
import greendao.Customer;
import greendao.Product;

/**
 * Created by USER on 3/7/2016.
 */
public class TransactionHistoryFragment extends Fragment {

    private View viewLine;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private TransactionHistoryAdapter transactionHistoryAdapter;
    private List<OrderHistory> listData;

    private LinearLayout lnlEmptyData;

    private int page;
    private boolean isLoading;
    private boolean isFull;
    private boolean isPull;
    private List<Long> previous_page_id = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction_history, container, false);

        page = 1;
        isLoading = false;
        isFull = false;
        isPull = false;
        previous_page_id.clear();

        initView(view);
        initData();

        return view;
    }

    private void initView(View view) {
        viewLine = view.findViewById(R.id.viewLine);
        listView = (ListView) view.findViewById(R.id.listView);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.main_color);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                isFull = false;
                isPull = true;
                previous_page_id.clear();
                getOrderList();
            }
        });

        lnlEmptyData = (LinearLayout) view.findViewById(R.id.lnlEmptyData);
        lnlEmptyData.setVisibility(View.GONE);

        Font font = new Font(getActivity());
        font.overrideFontsLight(view.findViewById(R.id.root));

    }

    private void initData() {
        listData = new ArrayList<>();

        transactionHistoryAdapter = new TransactionHistoryAdapter(getActivity(), listData);
        listView.setAdapter(transactionHistoryAdapter);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (listData.size() > 0) {
                    if (totalItemCount >= visibleItemCount + 2) {
                        if (firstVisibleItem + 3 >= totalItemCount - visibleItemCount + 2) {
                            if (!isLoading && !isFull && !isPull) {
                                isLoading = true;
                                page++;
                                getOrderList();
                            }
                        }
                    }
                }
            }
        });

        getOrderList();
    }

    private void getOrderList() {
        Customer customer = CustomerController.getCurrentCustomer(getActivity());
        UICallback callback = new UICallback() {
            @Override
            public void onResult(boolean success, String data) {
                if (success) {
                    try {
                        JSONArray array = new JSONArray(data);
                        List<Long> current_page = new ArrayList<>();
                        List<OrderHistory> current_order = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            long entity_id = object.getLong("entity_id");
                            String increment_id = object.getString("increment_id");
                            float subtotal = Float.parseFloat(object.optString("subtotal", "0"));
                            float shipping_amount = Float.parseFloat(object.optString("shipping_amount", "0"));
                            float discount_amount = Float.parseFloat(object.optString("discount_amount", "0"));
                            float tax_amount = Float.parseFloat(object.optString("tax_amount", "0"));
                            float grand_total = Float.parseFloat(object.optString("grand_total", "0"));
                            String created_at = object.getString("created_at");
                            String updated_at = object.getString("updated_at");
                            String status = object.getString("status");
                            String email_sent_str = object.optString("email_sent", "0");
                            if (email_sent_str.equalsIgnoreCase("null")) email_sent_str = "0";
                            int email_sent = Integer.parseInt(email_sent_str);

                            OrderHistory orderHistory = new OrderHistory(entity_id, increment_id, subtotal, shipping_amount,
                                    discount_amount, tax_amount, grand_total, created_at, updated_at, status, email_sent);
                            current_order.add(orderHistory);
                            current_page.add(entity_id);
                        }
                        if (page > 1) {
                            long last_id = current_page.get(current_page.size() - 1);
                            for (Long aLong : previous_page_id) {
                                if (last_id == aLong) {
                                    isFull = true;
                                    break;
                                }
                            }
                        }
                        if (!isFull) {
                            previous_page_id.clear();
                            previous_page_id.addAll(current_page);
                            if (page == 1) {
                                listData.clear();
                            }
                            listData.addAll(current_order);
                            transactionHistoryAdapter.setListData(listData);
                        }
                        if (listData.size() == 0) {
                            lnlEmptyData.setVisibility(View.VISIBLE);
                            viewLine.setVisibility(View.GONE);
                        } else {
                            lnlEmptyData.setVisibility(View.GONE);
                            viewLine.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        ((BaseActivity) getActivity()).showToastError(e.getMessage());
                    }
                } else {
                    ((BaseActivity) getActivity()).showToastError(data);
                }
                if (page == 1) {
                    ((BaseActivity) getActivity()).hideProgressDialog();
                }
                if (isPull) {
                    isPull = false;
                    swipeRefreshLayout.setRefreshing(false);
                }
                isLoading = false;
            }
        };
        OrderExecute.getOrderList(getActivity(), callback, customer.getEntity_id(), page);
        if (page == 1) {
            ((BaseActivity) getActivity()).showProgressDialog(false);
        }
    }
}
