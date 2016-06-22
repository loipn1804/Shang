package digimatic.shangcommerce.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.adapter.ImageBannerShopAdapter;
import digimatic.shangcommerce.connection.callback.UICallback;
import digimatic.shangcommerce.connection.threadconnection.execute.BannerExecute;
import digimatic.shangcommerce.daocontroller.CustomerController;
import digimatic.shangcommerce.fragment.BrandCategoryFragment;
import digimatic.shangcommerce.fragment.ListProductFragment;
import digimatic.shangcommerce.staticfunction.Font;

/**
 * Created by USER on 04/08/2016.
 */
public class FollowingDetailActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rltBack;
    private RelativeLayout rltSearch;
    private RelativeLayout rltCart;
    private TextView txtCart;
    private TextView txtTitle;

    // banner
    private ViewPager pager;
    private CirclePageIndicator indicator;
    private ImageBannerShopAdapter imageBannerShopAdapter;
    private List<String> listBanner;
    private CountDownTimer timer;

    private Font font;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following_detail);

//        sendEvent(getString(R.string.analytic_shipping), getString(R.string.analytic_in));

        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer = null;
        }
//        sendEvent(getString(R.string.analytic_shipping), getString(R.string.analytic_out));
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (timer != null) {
            timer.cancel();
        }
    }

    private void initView() {
        rltBack = (RelativeLayout) findViewById(R.id.rltBack);
        rltSearch = (RelativeLayout) findViewById(R.id.rltSearch);
        rltCart = (RelativeLayout) findViewById(R.id.rltCart);
        txtCart = (TextView) findViewById(R.id.txtCart);
        txtTitle = (TextView) findViewById(R.id.txtTitle);

        pager = (ViewPager) findViewById(R.id.pager);
        indicator = (CirclePageIndicator) findViewById(R.id.indicator);

        rltBack.setOnClickListener(this);

        font = new Font(this);
        font.overrideFontsLight(findViewById(R.id.root));
        font.overrideFontsBold(txtTitle);
    }

    private void initData() {
        txtTitle.setText(getIntent().getStringExtra("title"));

        listBanner = new ArrayList<>();
        getBanners();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        BrandCategoryFragment brandCategoryFragment = new BrandCategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("brand_id", getIntent().getLongExtra("brand_id", 0));
        brandCategoryFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.lnlFragment, brandCategoryFragment, "brandCategoryFragment");
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltBack:
                finish();
                break;
        }
    }

    private void setListBanner() {
        imageBannerShopAdapter = new ImageBannerShopAdapter(getSupportFragmentManager(), this, listBanner);
        pager.setAdapter(imageBannerShopAdapter);
        pager.setOffscreenPageLimit(listBanner.size());
        indicator.setViewPager(pager);
        pager.setCurrentItem(0);
        slideBannerPager(1);

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                indicator.setCurrentItem(position);
                if (position == listBanner.size() - 1) {
                    slideBannerPager(0);
                } else {
                    slideBannerPager(position + 1);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void slideBannerPager(final int page) {
        if (timer != null) {
            timer.cancel();
        }
        timer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                pager.setCurrentItem(page, true);
            }
        };
        timer.start();
    }

    private void getBanners() {
        UICallback callback = new UICallback() {
            @Override
            public void onResult(boolean success, String data) {
                if (success) {
                    try {
                        JSONArray array = new JSONArray(data);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            String image_url = object.getString("image_url");

                            listBanner.add(image_url);
                        }
                        setListBanner();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showToastError(e.getMessage());
                    }
                } else {
                    showToastError(data);
                }
            }
        };
        BannerExecute.getAll(this, callback);
    }
}
