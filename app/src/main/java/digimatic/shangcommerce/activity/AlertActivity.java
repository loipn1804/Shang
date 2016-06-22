package digimatic.shangcommerce.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import digimatic.shangcommerce.adapter.AlertAdapter;
import digimatic.shangcommerce.callback.ItemClickCallback;
import digimatic.shangcommerce.connection.callback.UICallback;
import digimatic.shangcommerce.connection.threadconnection.execute.CustomerExecute;
import digimatic.shangcommerce.connection.threadconnection.execute.NotificationExecute;
import digimatic.shangcommerce.daocontroller.CartDetailController;
import digimatic.shangcommerce.daocontroller.CustomerController;
import digimatic.shangcommerce.daocontroller.NotificationController;
import digimatic.shangcommerce.staticfunction.Font;
import greendao.CartDetail;
import greendao.Customer;
import greendao.Notification;

/**
 * Created by USER on 3/9/2016.
 */
public class AlertActivity extends BaseActivity implements View.OnClickListener, ItemClickCallback {

    private RelativeLayout rltBack;
    private RelativeLayout rltSearch;
    private RelativeLayout rltCart;
    private TextView txtCart;
    private TextView txtTitle;

    private SwipeMenuListView swipeMenuListView;
    private AlertAdapter alertAdapter;
    private List<Notification> listAlert;

    private int page;
    private boolean isLoading;
    private boolean isFull;
    private List<Long> previous_page_id;

    private LinearLayout lnlEmptyData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        sendEvent(getString(R.string.analytic_alert), getString(R.string.analytic_in));

        page = 1;
        isLoading = true;
        isFull = false;
        previous_page_id = new ArrayList<>();

        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sendEvent(getString(R.string.analytic_alert), getString(R.string.analytic_out));
    }

    private void initView() {
        rltBack = (RelativeLayout) findViewById(R.id.rltBack);
        rltSearch = (RelativeLayout) findViewById(R.id.rltSearch);
        rltCart = (RelativeLayout) findViewById(R.id.rltCart);
        txtCart = (TextView) findViewById(R.id.txtCart);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        swipeMenuListView = (SwipeMenuListView) findViewById(R.id.swipeMenuListView);

        lnlEmptyData = (LinearLayout) findViewById(R.id.lnlEmptyData);
        lnlEmptyData.setVisibility(View.GONE);

        rltBack.setOnClickListener(this);
        rltSearch.setOnClickListener(this);
        rltCart.setOnClickListener(this);

        Font font = new Font(this);
        font.overrideFontsLight(findViewById(R.id.root));
        font.overrideFontsBold(txtTitle);
    }

    private void initData() {
        listAlert = new ArrayList<>();
        alertAdapter = new AlertAdapter(this, listAlert, this);
        swipeMenuListView.setAdapter(alertAdapter);
        swipeMenuListView.setOnScrollListener(new EndScrollListener());

        initSwipeMenuListView();

        getNotifications();
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
                        deleteById(listAlert.get(position).getId());
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
                showToastInfo(position + " long click");
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltBack:
                finish();
                break;
            case R.id.rltSearch:
                Intent intentSearch = new Intent(this, SearchActivity.class);
                startActivity(intentSearch);
                break;
            case R.id.rltCart:
                Intent intentCart = new Intent(this, CartActivity.class);
                startActivity(intentCart);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupCart();
    }

    private void setupCart() {
        if (CustomerController.isLogin(this)) {
            txtCart.setVisibility(View.VISIBLE);
            Customer customer = CustomerController.getCurrentCustomer(this);
            txtCart.setText(customer.getCart_items_count() + "");
            if (customer.getCart_items_count() > 99) {
                txtCart.setText("99+");
            }
        } else {
            txtCart.setVisibility(View.GONE);
        }
    }

    private class EndScrollListener implements AbsListView.OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (totalItemCount >= visibleItemCount + 2) {
                if (firstVisibleItem + 3 >= totalItemCount - visibleItemCount + 2) {
                    if (!isLoading && !isFull) {
                        isLoading = true;
                        page++;
                        getNotifications();
                    }
                }
            }
        }
    }

    private void getNotifications() {
        Customer customer = CustomerController.getCurrentCustomer(this);
        UICallback callback = new UICallback() {
            @Override
            public void onResult(boolean success, String data) {
                if (success) {
                    try {
                        JSONArray array = new JSONArray(data);
                        if (page == 1) {
                            NotificationController.clearAll(AlertActivity.this);
                        }
                        List<Boolean> booleans = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            booleans.add(false);
                        }
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            long id = object.getLong("id");
                            long account_id = object.getLong("account_id");
                            long type_id = object.getLong("type_id");
                            String type = object.getString("type");
                            String title = object.getString("title");
                            if (title.equalsIgnoreCase("null")) title = "";
                            String message = object.getString("message");
                            if (message.equalsIgnoreCase("null")) message = "";
                            int is_read = object.getInt("is_read");
                            int is_remove = object.getInt("is_remove");
                            String created_at = object.getString("created_at");
                            String updated_at = object.getString("updated_at");

                            booleans.set(i, NotificationController.isExist(AlertActivity.this, id));

                            NotificationController.insertOrUpdate(AlertActivity.this, new Notification(id, account_id, type_id, type, title, message, is_read, is_remove, created_at, updated_at));
                        }
                        boolean isALlTrue = true;
                        for (Boolean aBoolean : booleans) {
                            if (!aBoolean) {
                                isALlTrue = false;
                                break;
                            }
                        }
                        if (isALlTrue) {
                            isFull = true;
                        }
                        listAlert.clear();
                        listAlert.addAll(NotificationController.getAll(AlertActivity.this));
                        alertAdapter.setListData(listAlert);
                        if (listAlert.size() == 0) {
                            lnlEmptyData.setVisibility(View.VISIBLE);
                        } else {
                            lnlEmptyData.setVisibility(View.GONE);
                        }
                        getCustomerDetailToGetCountNumbers();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showToastError(e.getMessage());
                    }
                } else {
                    showToastError(data);
                }
                isLoading = false;
                if (page == 1) {
                    hideProgressDialog();
                }
            }
        };
        NotificationExecute.getAll(this, callback, customer.getEntity_id(), page);
        if (page == 1) {
            showProgressDialog(false);
        }
    }

    private void deleteById(final long id) {
        UICallback callback = new UICallback() {
            @Override
            public void onResult(boolean success, String data) {
                if (success) {
                    NotificationController.deleteById(AlertActivity.this, id);
                    listAlert.clear();
                    listAlert.addAll(NotificationController.getAll(AlertActivity.this));
                    alertAdapter.setListData(listAlert);
                    getCustomerDetailToGetCountNumbers();
                    showToastOk("Removed your notification!");
                } else {
                    showToastError(data);
                }
                hideProgressDialog();
            }
        };
        NotificationExecute.deleteById(this, callback, id);
        showProgressDialog(false);
    }

    private void updateById(final long id) {
        JSONObject object = new JSONObject();
        try {
            object.put("is_read", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        UICallback callback = new UICallback() {
            @Override
            public void onResult(boolean success, String data) {
                if (success) {
                    NotificationController.setReadAlready(AlertActivity.this, id);
                    listAlert.clear();
                    listAlert.addAll(NotificationController.getAll(AlertActivity.this));
                    alertAdapter.setListData(listAlert);
                    getCustomerDetailToGetCountNumbers();
                } else {
//                    showToastError(data);
                }
            }
        };
        NotificationExecute.updateById(this, callback, id, object.toString());
    }

    @Override
    public void onItemLick(int position) {
        Intent intent = new Intent(this, AlertDetailActivity.class);
        intent.putExtra("type_id", listAlert.get(position).getType_id());
        intent.putExtra("isFromNotification", false);
        intent.putExtra("title", listAlert.get(position).getTitle());
        startActivity(intent);
        updateById(listAlert.get(position).getId());
    }

    private void getCustomerDetailToGetCountNumbers() {
        UICallback callback = new UICallback() {
            @Override
            public void onResult(boolean success, String data) {
                if (success) {
                    try {
                        JSONObject object = new JSONObject(data);
                        int cart_items_count = object.getInt("cart_items_count");
                        int unread_alerts_count = object.getInt("unread_alerts_count");
                        if (CustomerController.isLogin(AlertActivity.this)) {
                            Customer customer = CustomerController.getCurrentCustomer(AlertActivity.this);
                            customer.setCart_items_count(cart_items_count);
                            customer.setUnread_alerts_count(unread_alerts_count);
                            CustomerController.insertOrUpdate(AlertActivity.this, customer);
                        }
                        setupCart();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {

                }
            }
        };
        CustomerExecute.getCustomerCounter(this, callback, CustomerController.getCurrentCustomer(this).getEntity_id());
    }
}
