package digimatic.shangcommerce.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.adapter.MyRedemptionPagerAdapter;
import digimatic.shangcommerce.connection.Constant;
import digimatic.shangcommerce.staticfunction.Font;
import digimatic.shangcommerce.staticfunction.StaticFunction;

/**
 * Created by USER on 05/04/2016.
 */
public class MyRedemptionActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rltBack;
    private TextView txtTitle;

    private ViewPager viewPager;
    private PagerSlidingTabStrip tabStrip;
    private MyRedemptionPagerAdapter myRedemptionPagerAdapter;

    private Font font;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_redemption);

        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        rltBack = (RelativeLayout) findViewById(R.id.rltBack);
        txtTitle = (TextView) findViewById(R.id.txtTitle);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);

        rltBack.setOnClickListener(this);

        font = new Font(this);
        font.overrideFontsLight(findViewById(R.id.root));
        font.overrideFontsBold(txtTitle);
    }

    private void initData() {
        myRedemptionPagerAdapter = new MyRedemptionPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(myRedemptionPagerAdapter);
        viewPager.setOffscreenPageLimit(2);
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
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) v.getLayoutParams();
            params.width = StaticFunction.getScreenWidth(this) / 2;
            v.setLayoutParams(params);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltBack:
                finish();
                break;
        }
    }
}
