package digimatic.shangcommerce.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
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
import digimatic.shangcommerce.adapter.WishlistAdapter;
import digimatic.shangcommerce.connection.callback.UICallback;
import digimatic.shangcommerce.connection.threadconnection.execute.WishlistExecute;
import digimatic.shangcommerce.daocontroller.CustomerController;
import digimatic.shangcommerce.daocontroller.WishlistController;
import digimatic.shangcommerce.staticfunction.Font;
import greendao.Customer;
import greendao.Wishlist;

/**
 * Created by USER on 3/8/2016.
 */
public class WishlistActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rltBack;
    private TextView txtTitle;

    private SwipeMenuListView swipeMenuListView;
    private WishlistAdapter wishlistAdapter;
    private List<Wishlist> listWishlist;

    private LinearLayout lnlEmptyData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        sendEvent(getString(R.string.analytic_wishlist), getString(R.string.analytic_in));

        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sendEvent(getString(R.string.analytic_wishlist), getString(R.string.analytic_out));
    }

    private void initView() {
        rltBack = (RelativeLayout) findViewById(R.id.rltBack);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        swipeMenuListView = (SwipeMenuListView) findViewById(R.id.swipeMenuListView);

        lnlEmptyData = (LinearLayout) findViewById(R.id.lnlEmptyData);
        lnlEmptyData.setVisibility(View.GONE);

        rltBack.setOnClickListener(this);

        Font font = new Font(this);
        font.overrideFontsLight(findViewById(R.id.root));
        font.overrideFontsBold(txtTitle);
    }

    private void initData() {
        listWishlist = new ArrayList<>();

        wishlistAdapter = new WishlistAdapter(this, listWishlist);
        swipeMenuListView.setAdapter(wishlistAdapter);

        initSwipeMenuListView();

        getWishlist();
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
                        // open
//                        Handler handler = new Handler();
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                listWishlist.remove(position);
//                                wishlistAdapter.setListData(listWishlist);
//                                showToastInfo(position + "delete");
//                            }
//                        }, 500);
                        deleteFromWishlist(listWishlist.get(position).getWishlist_item_id());
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
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        listWishlist.clear();
        listWishlist.addAll(WishlistController.getAll(WishlistActivity.this));
        wishlistAdapter.setListData(listWishlist);
    }

    private void getWishlist() {
        Customer customer = CustomerController.getCurrentCustomer(this);
        UICallback callback = new UICallback() {
            @Override
            public void onResult(boolean success, String data) {
                if (success) {
                    try {
                        JSONArray array = new JSONArray(data);
                        WishlistController.clearAll(WishlistActivity.this);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            long wishlist_item_id = object.getLong("wishlist_item_id");
                            long product_id = object.getLong("product_id");
                            String name = object.getString("name");
                            String sku = object.getString("sku");
                            String manufacturer = object.getString("manufacturer");
                            String price = object.getString("price");
                            if (price.equalsIgnoreCase("null")) price = "";
                            float price_f = 0;
                            if (price.length() > 0) {
                                price_f = Float.parseFloat(price);
                            }
                            String image_url = object.getString("image_url");
                            String added_at = object.getString("added_at");
                            String type_id = object.getString("type_id");

                            WishlistController.insertOrUpdate(WishlistActivity.this, new Wishlist(wishlist_item_id, product_id, name, sku, manufacturer, price_f, image_url, added_at, type_id));
                        }
                        listWishlist.clear();
                        listWishlist.addAll(WishlistController.getAll(WishlistActivity.this));
                        wishlistAdapter.setListData(listWishlist);
                        if (listWishlist.size() == 0) {
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
        WishlistExecute.getByCustomerId(this, callback, customer.getEntity_id());
        showProgressDialog(false);
    }

    private void deleteFromWishlist(long wishlist_item_id) {
        UICallback callback = new UICallback() {
            @Override
            public void onResult(boolean success, String data) {
                if (success) {
                    showToastOk(getString(R.string.remove_wishlist));
                    getWishlist();
                } else {
                    showToastError(data);
                }
                hideProgressDialog();
            }
        };
        WishlistExecute.deleteFromWishlist(this, callback, wishlist_item_id);
        showProgressDialog(false);
    }
}
