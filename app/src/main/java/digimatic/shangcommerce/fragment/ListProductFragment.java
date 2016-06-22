package digimatic.shangcommerce.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import digimatic.shangcommerce.adapter.ListProductAdapter;
import digimatic.shangcommerce.connection.callback.UICallback;
import digimatic.shangcommerce.connection.threadconnection.execute.ProductExecute;
import greendao.Product;

/**
 * Created by USER on 3/4/2016.
 */
public class ListProductFragment extends Fragment {

    //    private TwoWayView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private ListProductAdapter listProductAdapter;
    private List<Product> listData;

    private ProgressBar progressBar;

    private long category_id;

    private int page;
    private boolean isLoading;
    private boolean isFull;
    private boolean isPull;
    private List<Long> previous_page_id = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_product, container, false);

        category_id = getArguments().getLong("category_id", 0);
        page = 1;
        isLoading = false;
        isFull = false;
        isPull = false;
        previous_page_id.clear();

        initView(view);
        initData();

        getProductByCategoryId();

        return view;
    }

    private void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.twoWayView);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.main_color);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                isFull = false;
                isPull = true;
                previous_page_id.clear();
                getProductByCategoryId();
            }
        });
    }

    private void initData() {
        listData = new ArrayList<>();
        listProductAdapter = new ListProductAdapter(getActivity(), listData);
        recyclerView.setAdapter(listProductAdapter);
        setupRecyclerView();
//        Drawable divider = getResources().getDrawable(R.drawable.divider_list_product);
//        SimpleDividerItemDecoration itemDecoration = new SimpleDividerItemDecoration(divider);
//        recyclerView.addItemDecoration(itemDecoration);
//        recyclerView.setHasFixedSize(true);
    }

    private void setupRecyclerView() {
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (listData.size() > 0) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int[] first = new int[2];
                    layoutManager.findFirstVisibleItemPositions(first);

                    if (first[0] + 3 >= totalItemCount - visibleItemCount + 2) {
                        if (!isLoading && !isFull && !isPull) {
                            isLoading = true;
                            page++;
                            getProductByCategoryId();
                        }
                    }
                }
            }
        });
    }

    private void getProductByCategoryId() {
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
                            listData.addAll(current_product);
                            listProductAdapter.setListData(listData);
                        }

                        progressBar.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                        ((BaseActivity) getActivity()).showToastError(e.getMessage());
                    }
                } else {
                    ((BaseActivity) getActivity()).showToastError(data);
                }
                if (isPull) {
                    isPull = false;
                    swipeRefreshLayout.setRefreshing(false);
                }
                isLoading = false;
            }
        };
        ProductExecute.getByCategoryId(getActivity(), callback, category_id, page);
    }
}
