package digimatic.shangcommerce.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.daocontroller.CustomerController;
import digimatic.shangcommerce.staticfunction.Font;

/**
 * Created by USER on 3/9/2016.
 */
public class IntroActivity extends BaseActivity implements View.OnClickListener {

    private TextView txtTitle;
    private TextView txtDesc;
    private TextView txtOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        initView();
        initData();

        if (CustomerController.isLogin(this)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void initView() {
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtDesc = (TextView) findViewById(R.id.txtDesc);
        txtOk = (TextView) findViewById(R.id.txtOk);

        txtOk.setOnClickListener(this);

        Font font = new Font(this);
        font.overrideFontsLight(findViewById(R.id.root));
        font.overrideFontsBold(txtTitle);
    }

    private void initData() {

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
