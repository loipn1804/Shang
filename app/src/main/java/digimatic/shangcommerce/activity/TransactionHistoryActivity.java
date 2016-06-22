package digimatic.shangcommerce.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.fragment.TransactionHistoryFragment;
import digimatic.shangcommerce.staticfunction.Font;

/**
 * Created by USER on 3/7/2016.
 */
public class TransactionHistoryActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rltBack;
    private TextView txtTitle;
    private LinearLayout lnlFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

        sendEvent(getString(R.string.analytic_my_order), getString(R.string.analytic_in));

        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sendEvent(getString(R.string.analytic_my_order), getString(R.string.analytic_out));
    }

    private void initView() {
        rltBack = (RelativeLayout) findViewById(R.id.rltBack);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        lnlFragment = (LinearLayout) findViewById(R.id.lnlFragment);

        rltBack.setOnClickListener(this);

        Font font = new Font(this);
        font.overrideFontsLight(findViewById(R.id.root));
        font.overrideFontsBold(txtTitle);
    }

    private void initData() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        TransactionHistoryFragment transactionHistoryFragment = new TransactionHistoryFragment();
        fragmentTransaction.add(R.id.lnlFragment, transactionHistoryFragment, "transactionHistoryFragment");
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
}
