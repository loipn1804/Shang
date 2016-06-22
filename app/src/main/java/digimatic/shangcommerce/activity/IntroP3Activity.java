package digimatic.shangcommerce.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.viewpagerindicator.CirclePageIndicator;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.adapter.IntroP3PagerAdapter;
import digimatic.shangcommerce.staticfunction.Font;

/**
 * Created by USER on 05/10/2016.
 */
public class IntroP3Activity extends BaseActivity implements View.OnClickListener {

    private ViewPager pager;
    private CirclePageIndicator indicator;
    private IntroP3PagerAdapter introP3PagerAdapter;
    private TextView txtOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_p3);

        initView();
        initData();
    }

    private void initView() {
        pager = (ViewPager) findViewById(R.id.pager);
        indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        txtOk = (TextView) findViewById(R.id.txtOk);

        txtOk.setOnClickListener(this);

        txtOk.setText("SKIP");

        Font font = new Font(this);
        font.overrideFontsLight(txtOk);
    }

    private void initData() {
        introP3PagerAdapter = new IntroP3PagerAdapter(getSupportFragmentManager(), this);
        pager.setAdapter(introP3PagerAdapter);
        pager.setOffscreenPageLimit(3);
        indicator.setViewPager(pager);
        pager.setCurrentItem(0);

        indicator.setPageColor(getResources().getColor(R.color.main_color_press));
        indicator.setFillColor(getResources().getColor(R.color.main_color));

        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    txtOk.setText("OK");
                } else {
                    txtOk.setText("SKIP");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtOk:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
