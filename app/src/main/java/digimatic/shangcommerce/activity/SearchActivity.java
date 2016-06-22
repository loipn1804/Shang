package digimatic.shangcommerce.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
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
import digimatic.shangcommerce.adapter.SearchAdapter;
import digimatic.shangcommerce.connection.callback.UICallback;
import digimatic.shangcommerce.connection.threadconnection.execute.ProductExecute;
import digimatic.shangcommerce.staticfunction.Font;
import digimatic.shangcommerce.staticfunction.StaticFunction;
import greendao.Product;

/**
 * Created by USER on 3/9/2016.
 */
public class SearchActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rltBack;
    private TextView txtTitle;

    private ListView listView;
    private SearchAdapter adapter;
    private List<Product> listData;

    private EditText edtSearch;
    private RelativeLayout rltClearSearch;

    private int page;
    private boolean isLoading;
    private boolean isFull;
    private List<Long> previous_page_id;

    private LinearLayout lnlEmptyData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        page = 1;
        isLoading = true;
        isFull = false;
        previous_page_id = new ArrayList<>();

        initView();
        initData();
    }

    private void initView() {
        rltBack = (RelativeLayout) findViewById(R.id.rltBack);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        listView = (ListView) findViewById(R.id.listView);
        edtSearch = (EditText) findViewById(R.id.edtSearch);
        rltClearSearch = (RelativeLayout) findViewById(R.id.rltClearSearch);

        lnlEmptyData = (LinearLayout) findViewById(R.id.lnlEmptyData);
        lnlEmptyData.setVisibility(View.GONE);

        rltBack.setOnClickListener(this);
        rltClearSearch.setOnClickListener(this);

        Font font = new Font(this);
        font.overrideFontsLight(findViewById(R.id.root));
        font.overrideFontsBold(txtTitle);

        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                int result = actionId & EditorInfo.IME_MASK_ACTION;
                switch (result) {
                    case EditorInfo.IME_ACTION_DONE:
                        listData.clear();
                        adapter.setListData(listData);
                        page = 1;
                        isLoading = false;
                        isFull = false;
                        previous_page_id.clear();
                        String search_key = edtSearch.getText().toString().trim();
                        if (search_key.length() == 0) {
//                            showToastError(getString(R.string.blank_search));
                            listData.clear();
                            adapter.setListData(listData);
                            if (listData.size() == 0) {
                                lnlEmptyData.setVisibility(View.VISIBLE);
                            } else {
                                lnlEmptyData.setVisibility(View.GONE);
                            }
                            edtSearch.requestFocus();
                            StaticFunction.showKeyboard(SearchActivity.this);
                        } else {
                            getProductBySearchKey(search_key, true);
                        }
                        break;
                }
                return false;
            }
        });
    }

    private void initData() {
        listData = new ArrayList<>();

        adapter = new SearchAdapter(this, listData);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(new EndScrollListener());

        edtSearch.requestFocus();
        StaticFunction.showKeyboard(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltBack:
                finish();
                break;
            case R.id.rltClearSearch:
                edtSearch.setText("");
                listData.clear();
                adapter.setListData(listData);
                page = 1;
                isLoading = false;
                isFull = false;
                previous_page_id.clear();
                edtSearch.requestFocus();
                StaticFunction.keepKeyboard(this, edtSearch);
                break;
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
                        String search_key = edtSearch.getText().toString().trim();
                        if (search_key.length() > 0) {
                            getProductBySearchKey(search_key, false);
                        }
                    }
                }
            }
        }
    }

    private void getProductBySearchKey(String search_key, final boolean isShowDialog) {
        UICallback callback = new UICallback() {
            @Override
            public void onResult(boolean success, String data) {
                if (success) {
                    try {
                        List<Long> current_page = new ArrayList<>();
                        List<Product> current_product = new ArrayList<>();
                        JSONArray array = new JSONArray(data);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            long entity_id = object.getLong("entity_id");
                            String type_id = object.getString("type_id");
                            String name = object.getString("name");
                            String sku = object.getString("sku");
                            String manufacturer = object.optString("manufacturer", "");
                            String short_description = object.getString("short_description");
                            String price = object.getString("price");
                            float price_f;
                            if (price.equalsIgnoreCase("null")) {
                                price_f = 0;
                            } else {
                                price_f = Float.parseFloat(price);
                            }
                            String special_price = object.getString("special_price");
                            float special_price_f;
                            if (special_price.equalsIgnoreCase("null")) {
                                special_price_f = 0;
                            } else {
                                special_price_f = Float.parseFloat(special_price);
                            }
                            String small_image_url = object.getString("small_image_url");
                            String thumbnail_url = object.getString("thumbnail_url");
                            String news_from_date = object.getString("news_from_date");
                            String news_to_date = object.getString("news_to_date");
                            String created_at = object.getString("created_at");
                            String updated_at = object.getString("updated_at");
                            int is_product_new = object.getInt("is_product_new");
                            int is_product_sale = object.getInt("is_product_sale");

                            current_product.add(new Product(entity_id, type_id, name, sku, manufacturer,
                                    short_description, price_f, special_price_f, small_image_url,
                                    thumbnail_url, news_from_date, news_to_date, created_at, updated_at, is_product_new, is_product_sale));
                            current_page.add(entity_id);
                        }

                        long last_id = current_page.get(current_page.size() - 1);
                        for (Long aLong : previous_page_id) {
                            if (last_id == aLong) {
                                isFull = true;
                                break;
                            }
                        }
                        if (!isFull) {
                            previous_page_id.clear();
                            previous_page_id.addAll(current_page);
                            if (page == 1) {
                                listData.clear();
                            }
                            listData.addAll(current_product);
                            adapter.setListData(listData);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
//                        showToastError(e.getMessage());
                    }
                } else {
//                    showToastError(data);
                }
                isLoading = false;
                if (isShowDialog) {
                    hideProgressDialog();
                }
//                if (listData.size() == 0) {
//                    showToastInfo(getString(R.string.no_search));
//                }
                if (listData.size() == 0) {
                    lnlEmptyData.setVisibility(View.VISIBLE);
                } else {
                    lnlEmptyData.setVisibility(View.GONE);
                }
            }
        };
        ProductExecute.getBySearchKey(this, callback, search_key, page);
        if (isShowDialog) {
            showProgressDialog(false);
        }
    }
}
