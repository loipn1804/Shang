package digimatic.shangcommerce.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;
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
 * Created by USER on 3/8/2016.
 */
public class FeedbackActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rltBack;
    private TextView txtTitle;

    private LinearLayout lnlAddress;
    private LinearLayout lnlPhone;
    private LinearLayout lnlEmail;
    private TextView txtPhone;
    private TextView txtEmail;

    private TextView edtName;
    private TextView edtEmail;
    private TextView edtMessage;
    private TextView txtSubmit;
    private TextView txtCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        initView();
        initData();
    }

    private void initView() {
        rltBack = (RelativeLayout) findViewById(R.id.rltBack);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        lnlAddress = (LinearLayout) findViewById(R.id.lnlAddress);
        lnlPhone = (LinearLayout) findViewById(R.id.lnlPhone);
        lnlEmail = (LinearLayout) findViewById(R.id.lnlEmail);
        txtPhone = (TextView) findViewById(R.id.txtPhone);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        edtName = (TextView) findViewById(R.id.edtName);
        edtEmail = (TextView) findViewById(R.id.edtEmail);
        edtMessage = (TextView) findViewById(R.id.edtMessage);
        txtSubmit = (TextView) findViewById(R.id.txtSubmit);
        txtCounter = (TextView) findViewById(R.id.txtCounter);

        rltBack.setOnClickListener(this);
        lnlAddress.setOnClickListener(this);
        lnlPhone.setOnClickListener(this);
        lnlEmail.setOnClickListener(this);
        txtSubmit.setOnClickListener(this);

        Font font = new Font(this);
        font.overrideFontsLight(findViewById(R.id.root));
        font.overrideFontsBold(txtTitle);
        font.overrideFontsBold(findViewById(R.id.txtCompanyName));
        font.overrideFontsBold(findViewById(R.id.txtTalkToUs));
    }

    private void initData() {
        edtMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtCounter.setText(s.length() + " char" + (s.length() == 1 ? "" : "s"));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltBack:
                finish();
                break;
            case R.id.lnlAddress:
                String query = getString(R.string.address);
                Uri gmmIntentUri = Uri.parse("geo:1.332904,103.892208?q=" + Uri.encode(query));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
                break;
            case R.id.txtSubmit:
                sendFeedBack();
                break;
            case R.id.lnlPhone:
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + txtPhone.getText().toString()));
                startActivity(callIntent);
                break;
            case R.id.lnlEmail:
                Intent intentEmail = new Intent(Intent.ACTION_SENDTO);
                intentEmail.setData(Uri.parse("mailto:" + txtEmail.getText().toString())); // only email apps should handle this
                if (intentEmail.resolveActivity(getPackageManager()) != null) {
                    startActivity(intentEmail);
                }
                break;
        }
    }

    private void sendFeedBack() {
        String name = edtName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String message = edtMessage.getText().toString().trim();
        if (name.length() == 0) {
            showToastError("Please enter your name");
        } else if (email.length() == 0) {
            showToastError("Please enter your email address");
        } else if (message.length() == 0) {
            showToastError("Please write your message");
        } else if (message.length() < 30) {
            showToastError("message length at least 30 chars");
        } else {
            JSONObject object = new JSONObject();
            try {
                object.put("name", name);
                object.put("email", email);
                object.put("message", message);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            UICallback callback = new UICallback() {
                @Override
                public void onResult(boolean success, String data) {
                    if (success) {
                        showToastOk("Thank you for sending us your feedback, we will get back to you as soon as we can.");
                        edtName.setText("");
                        edtEmail.setText("");
                        edtMessage.setText("");
                        edtName.clearFocus();
                        edtEmail.clearFocus();
                        edtMessage.clearFocus();
                        StaticFunction.hideKeyboard(FeedbackActivity.this);
                    } else {
                        showToastError(data);
                    }
                    hideProgressDialog();
                }
            };
            CustomerExecute.sendFeedBack(this, callback, object.toString());
            showProgressDialog(false);
        }
    }
}
