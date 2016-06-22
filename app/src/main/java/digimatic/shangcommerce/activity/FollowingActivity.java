package digimatic.shangcommerce.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.adapter.FollowingPagerAdapter;
import digimatic.shangcommerce.connection.callback.UICallback;
import digimatic.shangcommerce.connection.threadconnection.execute.BrandExecute;
import digimatic.shangcommerce.daocontroller.BrandController;
import digimatic.shangcommerce.staticfunction.Font;
import digimatic.shangcommerce.staticfunction.StaticFunction;
import greendao.Brand;

/**
 * Created by USER on 04/07/2016.
 */
public class FollowingActivity extends BaseActivity implements View.OnClickListener {

    public static String EDIT = "EDIT";
    public static String DONE = "DONE";
    public static String NOTIFY = "NOTIFY";

    private RelativeLayout rltBack;
    private TextView txtTitle;
    private TextView txtEdit;

    private ViewPager viewPager;
    private PagerSlidingTabStrip tabStrip;
    private FollowingPagerAdapter pagerAdapter;

    private LinearLayout lnlShopping;

    private Font font;

    private boolean isEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);

//        sendEvent(getString(R.string.analytic_shipping), getString(R.string.analytic_in));

        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        sendEvent(getString(R.string.analytic_shipping), getString(R.string.analytic_out));
    }

    private void initView() {
        rltBack = (RelativeLayout) findViewById(R.id.rltBack);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtEdit = (TextView) findViewById(R.id.txtEdit);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);

        lnlShopping = (LinearLayout) findViewById(R.id.lnlShopping);

        rltBack.setOnClickListener(this);
        txtEdit.setOnClickListener(this);
        lnlShopping.setOnClickListener(this);

        font = new Font(this);
        font.overrideFontsLight(findViewById(R.id.root));
        font.overrideFontsBold(txtTitle);
    }

    private void initData() {
        getBrand();
        showProgressDialog(false);
    }

    private void setPagerAdapter() {
        pagerAdapter = new FollowingPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        tabStrip.setViewPager(viewPager);
        tabStrip.setTextColor(getResources().getColor(R.color.txt_black));
        tabStrip.setDividerColor(getResources().getColor(R.color.transparent));
        tabStrip.setIndicatorColor(getResources().getColor(R.color.main_color));
        tabStrip.setUnderlineColor(getResources().getColor(R.color.main_color));
        tabStrip.setUnderlineHeight((int) getResources().getDimension(R.dimen.dm_1dp));
        tabStrip.setTextSize((int) getResources().getDimension(R.dimen.txt_15sp));
        tabStrip.setBackgroundColor(getResources().getColor(R.color.white));
        tabStrip.setTabBackground(R.drawable.background_tab);
        tabStrip.setTypeface(font.light, 0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setTextColorTabStrip(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setTextColorTabStrip(0);
    }

    private void setTextColorTabStrip(int position) {
        LinearLayout lnl = (LinearLayout) tabStrip.getChildAt(0);
        for (int i = 0; i < lnl.getChildCount(); i++) {
            View v = lnl.getChildAt(i);
            try {
                if (v instanceof TextView) {
                    if (position == i) {
                        ((TextView) v).setTextColor(getResources().getColor(R.color.main_color));
                    } else {
                        ((TextView) v).setTextColor(getResources().getColor(R.color.txt_black));
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltBack:
                finish();
                break;
            case R.id.txtEdit:
                isEdit = !isEdit;
                if (isEdit) {
                    StaticFunction.sendBroadCast(this, EDIT);
                    txtEdit.setText("Done");
                } else {
                    StaticFunction.sendBroadCast(this, DONE);
                    txtEdit.setText("Edit");
                }
                break;
            case R.id.lnlShopping:
                finish();
                break;
        }
    }

    private void getBrand() {
        UICallback callback = new UICallback() {
            @Override
            public void onResult(boolean success, String data) {
                if (success) {
                    try {
                        JSONArray array = new JSONArray(data);
                        BrandController.clearAll(FollowingActivity.this);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            long brand_id = object.getLong("brand_id");
                            String name = object.getString("name");
                            String short_description = object.getString("short_description");
                            if (short_description.equalsIgnoreCase("null")) short_description = "";
                            String image_url = object.getString("image_url");
                            if (image_url.equalsIgnoreCase("null")) image_url = "";
                            String thumbnail_url = object.getString("thumbnail_url");
                            if (thumbnail_url.equalsIgnoreCase("null")) thumbnail_url = "";

                            BrandController.insertOrUpdate(FollowingActivity.this, new Brand(brand_id, name, short_description, image_url, thumbnail_url));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showToastError(e.getMessage());
                    }
                } else {
                    showToastError(data);
                }
                hideProgressDialog();
                setPagerAdapter();
            }
        };
        BrandExecute.getBrands(this, callback);
    }
}
