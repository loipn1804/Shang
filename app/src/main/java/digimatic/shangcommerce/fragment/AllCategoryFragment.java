package digimatic.shangcommerce.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.adapter.AllCategoryAdapter;
import digimatic.shangcommerce.daocontroller.CategoryController;
import greendao.Category;

/**
 * Created by USER on 3/4/2016.
 */
public class AllCategoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private AllCategoryAdapter allCategoryAdapter;
    private List<Category> listData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_category, container, false);

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
        listData.addAll(CategoryController.getAll(getActivity()));
        allCategoryAdapter = new AllCategoryAdapter(getActivity(), listData);
        recyclerView.setAdapter(allCategoryAdapter);
    }
}
