package digimatic.shangcommerce.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.adapter.BannerViewAdapter;
import digimatic.shangcommerce.adapter.ImageBannerProductAdapter;
import digimatic.shangcommerce.model.Banner;

/**
 * Created by USER on 16/6/2016.
 */
public class BannerViewActivity extends BaseActivity {


    public static final String EXTRA_ARRAY_LIST_PAGE = "extra_array_list_page";
    public static final String EXTRA_POSITION = "extra_position";

    private ArrayList<Banner> mArrayBanner;
    private ViewPager mViewPager;
    private BannerViewAdapter mAdapter;
    private int mPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_view);
        initView();
        mArrayBanner = new ArrayList<>();
        mArrayBanner = getIntent().getParcelableArrayListExtra(EXTRA_ARRAY_LIST_PAGE);
        mPosition = getIntent().getIntExtra(EXTRA_POSITION, 0);
        if (mArrayBanner.isEmpty()) {
            finish();
        }
        initData();
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.banner_view_pager);
    }

    private void initData() {
        mAdapter = new BannerViewAdapter(mArrayBanner, getSupportFragmentManager());
        mAdapter.notifyDataSetChanged();
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mPosition, true);
    }


}
