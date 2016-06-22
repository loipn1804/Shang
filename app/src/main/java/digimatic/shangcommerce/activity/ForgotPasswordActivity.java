package digimatic.shangcommerce.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.connection.callback.UICallback;
import digimatic.shangcommerce.connection.threadconnection.execute.CustomerExecute;
import digimatic.shangcommerce.staticfunction.Font;
import digimatic.shangcommerce.staticfunction.StaticFunction;

/**
 * Created by USER on 3/9/2016.
 */
public class ForgotPasswordActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rltBack;
    private TextView txtTitle;
    private TextView txtSend;

    private EditText edtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        sendEvent(getString(R.string.analytic_forgot_pass), getString(R.string.analytic_in));

        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sendEvent(getString(R.string.analytic_forgot_pass), getString(R.string.analytic_out));
    }

    private void initView() {
        rltBack = (RelativeLayout) findViewById(R.id.rltBack);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtSend = (TextView) findViewById(R.id.txtSend);

        edtEmail = (EditText) findViewById(R.id.edtEmail);

        rltBack.setOnClickListener(this);
        txtSend.setOnClickListener(this);

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
            case R.id.txtSend:
                forgotPassword();
                break;
        }
    }

    private void forgotPassword() {
        final String email = edtEmail.getText().toString().trim();
        if (!StaticFunction.isEmailValid(email)) {
            showToastError("Email is invalidate");
        } else {
            JSONObject object = new JSONObject();
            try {
                object.put("email", email);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            UICallback callback = new UICallback() {
                @Override
                public void onResult(boolean success, String data) {
                    if (success) {
                        showToastOk("If there is an account associated with " + email + " you will receive an email with a link to reset your password.");
                        finish();
                    } else {
                        showToastError(data);
                    }
                    hideProgressDialog();
                }
            };
            CustomerExecute.forgotPassword(this, callback, object.toString());
            showProgressDialog(false);
        }
    }
}
