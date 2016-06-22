package digimatic.shangcommerce.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.connection.callback.UICallback;
import digimatic.shangcommerce.connection.threadconnection.execute.CustomerExecute;
import digimatic.shangcommerce.daocontroller.CustomerController;
import digimatic.shangcommerce.model.ChangePass;
import digimatic.shangcommerce.model.User;
import digimatic.shangcommerce.staticfunction.Font;
import digimatic.shangcommerce.staticfunction.StaticFunction;
import greendao.Customer;

/**
 * Created by USER on 3/9/2016.
 */
public class ChangePasswordActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rltBack;
    private TextView txtTitle;
    private TextView txtSave;

    private EditText edtPassword;
    private EditText edtNewPassword;
    private EditText edtConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        initView();
        initData();
    }

    private void initView() {
        rltBack = (RelativeLayout) findViewById(R.id.rltBack);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtSave = (TextView) findViewById(R.id.txtSave);

        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtNewPassword = (EditText) findViewById(R.id.edtNewPassword);
        edtConfirmPassword = (EditText) findViewById(R.id.edtConfirmPassword);

        rltBack.setOnClickListener(this);
        txtSave.setOnClickListener(this);

        Font font = new Font(this);
        font.overrideFontsLight(findViewById(R.id.root));
        font.overrideFontsBold(txtTitle);
    }

    private void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltBack:
                finish();
                break;
            case R.id.txtSave:
                changePass();
                break;
        }
    }

    private void changePass() {
        String password = edtPassword.getText().toString().trim();
        String new_password = edtNewPassword.getText().toString().trim();
        String confirm_password = edtConfirmPassword.getText().toString().trim();
        if (!StaticFunction.isNetworkAvailable(this)) {
            showToastError(getString(R.string.no_internet));
        } else if (password.length() == 0) {
            showToastError(getString(R.string.blank_pass));
        } else if (new_password.length() == 0) {
            showToastError(getString(R.string.blank_new_pass));
        } else if (!new_password.equals(confirm_password)) {
            showToastError(getString(R.string.confirm_pass_invalidate));
        } else {
            ChangePass changePass = new ChangePass(password, new_password);
            String body = new Gson().toJson(changePass);

            UICallback callback = new UICallback() {
                @Override
                public void onResult(boolean success, String data) {
                    if (success) {
                        try {
                            JSONObject object = new JSONObject(data);
                            showToastOk("Change password successfully!");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            showToastError(e.getMessage());

                        }
                    } else {
                        showToastError(data);
                    }
                    hideProgressDialog();
                }
            };
            CustomerExecute.changePass(this, callback, CustomerController.getCurrentCustomer(ChangePasswordActivity.this).getEntity_id() + "", body);
            showProgressDialog(false);
        }
    }
}
