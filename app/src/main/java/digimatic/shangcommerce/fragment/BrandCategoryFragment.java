package digimatic.shangcommerce.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.activity.BaseActivity;
import digimatic.shangcommerce.adapter.AllCategoryAdapter;
import digimatic.shangcommerce.connection.callback.UICallback;
import digimatic.shangcommerce.connection.threadconnection.execute.CategoryExecute;
import digimatic.shangcommerce.daocontroller.CategoryController;
import greendao.Category;

/**
 * Created by USER on 04/19/2016.
 */
public class BrandCategoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private AllCategoryAdapter allCategoryAdapter;
    private List<Category> listData;

    private long brand_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_category, container, false);

        brand_id = getArguments().getLong("brand_id", 0);

        initView(view);
        initData();

        return view;
    }

    private void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.twoWayView);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void initData() {
        listData = new ArrayList<>();

        getCategoryByBrandId();
        ((BaseActivity) getActivity()).showProgressDialog(false);
    }

    private void getCategoryByBrandId() {
        listData.clear();
        UICallback callback = new UICallback() {
            @Override
            public void onResult(boolean success, String data) {
                if (success) {
                    try {
                        JSONArray array = new JSONArray(data);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            long entity_id = object.getLong("entity_id");
                            String name = object.getString("name");
                            int position = object.getInt("position");
                            String image_url = object.getString("image_url");
                            if (image_url.equalsIgnoreCase("null")) image_url = "";
                            String thumbnail_url = object.getString("thumbnail_url");
                            if (thumbnail_url.equalsIgnoreCase("null")) thumbnail_url = "";
                            int product_count = object.getInt("product_count");

                            listData.add(new Category(entity_id, name, position, image_url, thumbnail_url, product_count));
                            allCategoryAdapter = new AllCategoryAdapter(getActivity(), listData);
                            recyclerView.setAdapter(allCategoryAdapter);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        ((BaseActivity) getActivity()).showToastError(e.getMessage());
                    }
                } else {
                    ((BaseActivity) getActivity()).showToastError(data);
                }
                ((BaseActivity) getActivity()).hideProgressDialog();
            }
        };
        CategoryExecute.getCategoryByBrandId(getActivity(), callback, brand_id);
    }
}
