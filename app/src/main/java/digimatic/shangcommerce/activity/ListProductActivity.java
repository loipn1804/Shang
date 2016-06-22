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
import digimatic.shangcommerce.daocontroller.CartDetailController;
import digimatic.shangcommerce.daocontroller.CustomerController;
import digimatic.shangcommerce.fragment.ListProductFragment;
import digimatic.shangcommerce.staticfunction.Font;
import greendao.CartDetail;
import greendao.Customer;

/**
 * Created by USER on 3/5/2016.
 */
public class ListProductActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rltBack;
    private RelativeLayout rltSearch;
    private RelativeLayout rltCart;
    private TextView txtCart;
    private TextView txtTitle;
    private LinearLayout lnlFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_product);

        initView();
        initData();
    }

    private void initView() {
        rltBack = (RelativeLayout) findViewById(R.id.rltBack);
        rltSearch = (RelativeLayout) findViewById(R.id.rltSearch);
        rltCart = (RelativeLayout) findViewById(R.id.rltCart);
        txtCart = (TextView) findViewById(R.id.txtCart);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        lnlFragment = (LinearLayout) findViewById(R.id.lnlFragment);

        rltBack.setOnClickListener(this);
        rltSearch.setOnClickListener(this);
        rltCart.setOnClickListener(this);

        Font font = new Font(this);
        font.overrideFontsLight(findViewById(R.id.root));
        font.overrideFontsBold(txtTitle);

        txtTitle.setText(getIntent().getStringExtra("title"));
    }

    private void initData() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ListProductFragment listProductFragment = new ListProductFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("category_id", getIntent().getLongExtra("category_id", 0));
        listProductFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.lnlFragment, listProductFragment, "listProductFragment");
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltBack:
                finish();
                break;
            case R.id.rltSearch:
                Intent intentSearch = new Intent(this, SearchActivity.class);
                startActivity(intentSearch);
                break;
            case R.id.rltCart:
                if (CustomerController.isLogin(this)) {
                    Intent intentCart = new Intent(this, CartActivity.class);
                    startActivity(intentCart);
                } else {
                    Intent intentLogin = new Intent(this, LoginActivity.class);
                    startActivity(intentLogin);
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupCart();
    }

    private void setupCart() {
        if (CustomerController.isLogin(this)) {
            txtCart.setVisibility(View.VISIBLE);
            Customer customer = CustomerController.getCurrentCustomer(this);
            txtCart.setText(customer.getCart_items_count() + "");
            if (customer.getCart_items_count() > 99) {
                txtCart.setText("99+");
            }
        } else {
            txtCart.setVisibility(View.GONE);
        }
    }
}
