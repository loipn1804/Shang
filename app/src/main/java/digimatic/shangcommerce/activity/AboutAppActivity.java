package digimatic.shangcommerce.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.connection.Constant;
import digimatic.shangcommerce.staticfunction.Font;
import digimatic.shangcommerce.staticfunction.StaticFunction;

/**
 * Created by USER on 3/22/2016.
 */
public class AboutAppActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rltBack;
    private TextView txtTitle;

    private TextView txtWebPageError;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        initView();
        initData();
    }

    private void initView() {
        rltBack = (RelativeLayout) findViewById(R.id.rltBack);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtWebPageError = (TextView) findViewById(R.id.txtWebPageError);
        webView = (WebView) findViewById(R.id.webView);

        rltBack.setOnClickListener(this);

        Font font = new Font(this);
        font.overrideFontsLight(findViewById(R.id.root));
        font.overrideFontsBold(txtTitle);

        txtTitle.setText("About this App");
    }

    private void initData() {
        if (StaticFunction.isNetworkAvailable(this)) {
            txtWebPageError.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(Constant.ABOUT_APP);
        } else {
            txtWebPageError.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
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
