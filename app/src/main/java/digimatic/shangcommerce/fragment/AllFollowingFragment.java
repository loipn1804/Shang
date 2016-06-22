package digimatic.shangcommerce.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.activity.BaseActivity;
import digimatic.shangcommerce.activity.FollowingActivity;
import digimatic.shangcommerce.adapter.AllFollowingAdapter;
import digimatic.shangcommerce.callback.ItemClickCallback;
import digimatic.shangcommerce.connection.callback.UICallback;
import digimatic.shangcommerce.connection.threadconnection.execute.FollowingExecute;
import digimatic.shangcommerce.daocontroller.BrandController;
import digimatic.shangcommerce.daocontroller.CategoryController;
import digimatic.shangcommerce.daocontroller.CustomerController;
import digimatic.shangcommerce.model.AllFollowing;
import digimatic.shangcommerce.model.Following;
import digimatic.shangcommerce.staticfunction.Font;
import digimatic.shangcommerce.staticfunction.StaticFunction;
import greendao.Brand;
import greendao.Category;

/**
 * Created by USER on 04/07/2016.
 */
public class AllFollowingFragment extends Fragment implements ItemClickCallback {

    private RecyclerView recyclerView;
    private AllFollowingAdapter adapter;
    private List<AllFollowing> listData;
    private List<AllFollowing> listDataFull;
    private boolean isEdit = false;
    private int positionTab = 0;
    private boolean isDestroy;

    private EditText edtSearch;
    private ImageView imvSearch;

    private LinearLayout lnlEmptyData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_following, container, false);

        isDestroy = false;

        positionTab = getArguments().getInt("position", 0);

        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(FollowingActivity.EDIT);
            intentFilter.addAction(FollowingActivity.DONE);
            intentFilter.addAction(FollowingActivity.NOTIFY);
            getActivity().registerReceiver(activityReceiver, intentFilter);
        }

        initView(view);
        initData();

        return view;
    }

    private void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.twoWayView);
        edtSearch = (EditText) view.findViewById(R.id.edtSearch);
        imvSearch = (ImageView) view.findViewById(R.id.imvSearch);

        lnlEmptyData = (LinearLayout) view.findViewById(R.id.lnlEmptyData);
        lnlEmptyData.setVisibility(View.GONE);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(layoutManager);

        Font font = new Font(getActivity());
        font.overrideFontsLight(edtSearch);

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterSearch();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initData() {
        listData = new ArrayList<>();
        listDataFull = new ArrayList<>();

        adapter = new AllFollowingAdapter(getActivity(), listData, isEdit, this);
        recyclerView.setAdapter(adapter);

        getListFollowing();
        ((BaseActivity) getActivity()).showProgressDialog(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isDestroy = true;
    }

    private void setListALlFollowing(List<Following> followings) {
        listData.clear();
        listDataFull.clear();
        if (positionTab == 0 || positionTab == 2) {
            List<Category> categories = CategoryController.getAll(getActivity());
            for (Category category : categories) {
                AllFollowing allFollowing = new AllFollowing();
                allFollowing.setIsCategory(true);

                allFollowing.setEntity_id(category.getEntity_id());
                allFollowing.setName_category(category.getName());
                allFollowing.setPosition(category.getPosition());
                allFollowing.setImage_url(category.getImage_url());
                allFollowing.setThumbnail_url(category.getThumbnail_url());
                allFollowing.setProduct_count(category.getProduct_count());

                for (Following following : followings) {
                    if (following.isCategory()) {
                        if (following.getType_id() == category.getEntity_id()) {
                            allFollowing.setIsFollow(true);
                            allFollowing.setId(following.getId());
                            allFollowing.setType(following.getType());
                            allFollowing.setType_id(following.getType_id());
                            break;
                        }
                    }
                }

                if (positionTab == 0) {
                    listData.add(allFollowing);
                    listDataFull.add(allFollowing);
                } else {
                    if (allFollowing.isFollow()) {
                        listData.add(allFollowing);
                        listDataFull.add(allFollowing);
                    }
                }
            }
        }

        if (positionTab == 0 || positionTab == 1) {
            List<Brand> brands = BrandController.getAll(getActivity());
            for (Brand brand : brands) {
                AllFollowing allFollowing = new AllFollowing();
                allFollowing.setIsCategory(false);

                allFollowing.setBrand_id(brand.getBrand_id());
                allFollowing.setName_brand(brand.getName());
                allFollowing.setShort_description(brand.getShort_description());
                allFollowing.setImage_url(brand.getImage_url());
                allFollowing.setThumbnail_url(brand.getThumbnail_url());

                for (Following following : followings) {
                    if (!following.isCategory()) {
                        if (following.getType_id() == brand.getBrand_id()) {
                            allFollowing.setIsFollow(true);
                            allFollowing.setId(following.getId());
                            allFollowing.setType(following.getType());
                            allFollowing.setType_id(following.getType_id());
                            break;
                        }
                    }
                }

                if (positionTab == 0) {
                    listData.add(allFollowing);
                    listDataFull.add(allFollowing);
                } else {
                    if (allFollowing.isFollow()) {
                        listData.add(allFollowing);
                        listDataFull.add(allFollowing);
                    }
                }
            }
        }

        filterSearch();
    }

    private void filterSearch() {
        listData.clear();
        String search = edtSearch.getText().toString().toLowerCase();
        if (search.length() > 0) {
            for (AllFollowing allFollowing : listDataFull) {
                if (allFollowing.isCategory()) {
                    if (allFollowing.getName_category().toLowerCase().contains(search)) {
                        listData.add(allFollowing);
                    }
                } else {
                    if (allFollowing.getName_brand().toLowerCase().contains(search)) {
                        listData.add(allFollowing);
                    }
                }
            }
        } else {
            listData.addAll(listDataFull);
        }
        adapter.setListData(listData, isEdit);
        if (listData.size() == 0) {
            lnlEmptyData.setVisibility(View.VISIBLE);
        } else {
            lnlEmptyData.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (activityReceiver != null) {
            getActivity().unregisterReceiver(activityReceiver);
        }
    }

    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(FollowingActivity.EDIT)) {
                isEdit = true;
                adapter.setListData(listData, isEdit);
            } else if (intent.getAction().equalsIgnoreCase(FollowingActivity.DONE)) {
                isEdit = false;
                adapter.setListData(listData, isEdit);
            } else if (intent.getAction().equalsIgnoreCase(FollowingActivity.NOTIFY)) {
                getListFollowing();
                ((BaseActivity) getActivity()).showProgressDialog(false);
            }
        }
    };

    @Override
    public void onItemLick(int position) {
        if (isEdit) {
            AllFollowing allFollowing = listData.get(position);
            if (allFollowing.isFollow()) {
                // unfollow
                unfollow(allFollowing);
                ((BaseActivity) getActivity()).showProgressDialog(false);
            } else {
                // follow
                follow(allFollowing);
                ((BaseActivity) getActivity()).showProgressDialog(false);
            }
        }
    }


    private void getListFollowing() {
        UICallback callback = new UICallback() {
            @Override
            public void onResult(boolean success, String data) {
                if (!isDestroy) {
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
                            ((BaseActivity) getActivity()).showToastError(e.getMessage());
                        }
                    } else {
                        ((BaseActivity) getActivity()).showToastError(data);
                    }
                    ((BaseActivity) getActivity()).hideProgressDialog();

                    setListALlFollowing(followings);
                }
            }
        };
        FollowingExecute.getListFollowing(getActivity(), callback, CustomerController.getCurrentCustomer(getActivity()).getEntity_id());
    }

    private void follow(AllFollowing allFollowing) {
        JSONObject object = new JSONObject();
        try {
            object.put("customer_id", CustomerController.getCurrentCustomer(getActivity()).getEntity_id());
            object.put("type", allFollowing.isCategory() ? "category" : "brand");
            object.put("type_id", allFollowing.isCategory() ? allFollowing.getEntity_id() : allFollowing.getBrand_id());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        UICallback callback = new UICallback() {
            @Override
            public void onResult(boolean success, String data) {
                if (success) {
                    StaticFunction.sendBroadCast(getActivity(), FollowingActivity.NOTIFY);
//                    ((BaseActivity) getActivity()).hideProgressDialog();
                } else {
                    ((BaseActivity) getActivity()).showToastError(data);
                    ((BaseActivity) getActivity()).hideProgressDialog();
                }
            }
        };
        FollowingExecute.follow(getActivity(), callback, object.toString());
    }

    private void unfollow(AllFollowing allFollowing) {
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
                    StaticFunction.sendBroadCast(getActivity(), FollowingActivity.NOTIFY);
//                    ((BaseActivity) getActivity()).hideProgressDialog();
                } else {
                    ((BaseActivity) getActivity()).showToastError(data);
                    ((BaseActivity) getActivity()).hideProgressDialog();
                }
            }
        };
        FollowingExecute.unfollow(getActivity(), callback, allFollowing.getId(), object.toString());
    }
}
