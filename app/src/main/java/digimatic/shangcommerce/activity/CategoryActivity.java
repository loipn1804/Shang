package digimatic.shangcommerce.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.adapter.CategoryAdapter;
import digimatic.shangcommerce.callback.ItemClickCallback;
import digimatic.shangcommerce.connection.callback.UICallback;
import digimatic.shangcommerce.connection.threadconnection.execute.CategoryExecute;
import digimatic.shangcommerce.connection.threadconnection.execute.FollowingExecute;
import digimatic.shangcommerce.daocontroller.CustomerController;
import digimatic.shangcommerce.model.Category;
import digimatic.shangcommerce.model.Following;
import digimatic.shangcommerce.staticfunction.Font;

/**
 * Created by USER on 4/5/2016.
 */
public class CategoryActivity extends BaseActivity implements View.OnClickListener, ItemClickCallback {

    private ListView listView;
    private CategoryAdapter adapter;
    private List<Category> listData;

    private RelativeLayout rltBack;
    private TextView txtTitle;
    private TextView txtContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        initView();
        initData();
    }

    private void initView() {
        rltBack = (RelativeLayout) findViewById(R.id.rltBack);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtContinue = (TextView) findViewById(R.id.txtContinue);
        listView = (ListView) findViewById(R.id.listView);

        rltBack.setOnClickListener(this);
        txtContinue.setOnClickListener(this);

        Font font = new Font(this);
        font.overrideFontsLight(findViewById(R.id.root));
        font.overrideFontsBold(txtTitle);
    }

    private void initData() {
        listData = new ArrayList<>();

        adapter = new CategoryAdapter(this, listData, this);
        listView.setAdapter(adapter);

        getCategories();
        showProgressDialog(false);
    }

    private void setListData(List<Following> followings) {
        for (Category category : listData) {
            category.setIsChosen(false);
            for (Following following : followings) {
                if (following.isCategory()) {
                    if (following.getType_id() == category.getEntity_id()) {
                        category.setIsChosen(true);
                        category.setId(following.getId());
                        break;
                    }
                }
            }
        }
        adapter.setListData(listData);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltBack:
                finish();
                break;
            case R.id.txtContinue:
                Intent intent = new Intent(this, BrandActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void getCategories() {
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
                            String image_url = object.getString("image_url");
                            if (image_url.equalsIgnoreCase("null")) image_url = "";
                            String thumbnail_url = object.getString("thumbnail_url");
                            if (thumbnail_url.equalsIgnoreCase("null")) thumbnail_url = "";

                            listData.add(new Category(entity_id, name, image_url, thumbnail_url, false, 0));
                            adapter.setListData(listData);

                            getListFollowing();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showToastError(e.getMessage());
                        hideProgressDialog();
                    }
                } else {
                    showToastError(data);
                    hideProgressDialog();
                }
            }
        };
        CategoryExecute.getCategories(this, callback);
    }

    @Override
    public void onItemLick(int position) {
        Category category = listData.get(position);
        if (category.isChosen()) {
            // unfollow
            unfollow(category);
            showProgressDialog(false);
        } else {
            // follow
            follow(category);
            showProgressDialog(false);
        }
    }

    private void getListFollowing() {
        UICallback callback = new UICallback() {
            @Override
            public void onResult(boolean success, String data) {
                List<Following> followings = new ArrayList<>();
                if (success) {
                    try {
                        JSONArray array = new JSONArray(data);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            long id = object.getLong("id");
                            String type = object.getString("type");
                            long type_id = object.getLong("type_id");

                            boolean isCategory = false;
                            if (type.equalsIgnoreCase("category")) {
                                isCategory = true;
                            }

                            followings.add(new Following(id, type, type_id, isCategory));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showToastError(e.getMessage());
                    }
                } else {
                    showToastError(data);
                }
                hideProgressDialog();

                setListData(followings);
            }
        };
        FollowingExecute.getListFollowing(this, callback, CustomerController.getCurrentCustomer(this).getEntity_id());
    }

    private void follow(Category category) {
        JSONObject object = new JSONObject();
        try {
            object.put("customer_id", CustomerController.getCurrentCustomer(this).getEntity_id());
            object.put("type", "category");
            object.put("type_id", category.getEntity_id());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        UICallback callback = new UICallback() {
            @Override
            public void onResult(boolean success, String data) {
                if (success) {
                    getListFollowing();
                } else {
                    showToastError(data);
                    hideProgressDialog();
                }
            }
        };
        FollowingExecute.follow(this, callback, object.toString());
    }

    private void unfollow(Category category) {
        JSONObject object = new JSONObject();
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Calendar calendar = Calendar.getInstance();
            Date date = calendar.getTime();
            object.put("date_left", format.format(date));
        } catch (Exception e) {
            e.printStackTrace();
        }
        UICallback callback = new UICallback() {
            @Override
            public void onResult(boolean success, String data) {
                if (success) {
                    getListFollowing();
                } else {
                    showToastError(data);
                    hideProgressDialog();
                }
            }
        };
        FollowingExecute.unfollow(this, callback, category.getId(), object.toString());
    }
}
