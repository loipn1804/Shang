package digimatic.shangcommerce.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.connection.callback.UICallback;
import digimatic.shangcommerce.connection.threadconnection.execute.CustomerExecute;
import digimatic.shangcommerce.model.Register;
import digimatic.shangcommerce.model.User;
import digimatic.shangcommerce.receiver.KeyboardVisibilityListener;
import digimatic.shangcommerce.receiver.KeyboardVisibilityListener.OnKeyboardVisibilityListener;
import digimatic.shangcommerce.staticfunction.ClickSpan;
import digimatic.shangcommerce.staticfunction.Font;
import digimatic.shangcommerce.staticfunction.StaticFunction;

/**
 * Created by USER on 3/4/2016.
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener, OnKeyboardVisibilityListener {

    private EditText edtFirstName;
    private EditText edtLastName;
    private EditText edtPassword;
    private EditText edtEmail;
    private EditText edtDOB;
    private RelativeLayout rltSignup;
    private TextView txtTerm;
    private TextView txtSignin;

    private KeyboardVisibilityListener keyboardVisibilityListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        sendEvent(getString(R.string.analytic_sign_up), getString(R.string.analytic_in));

        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sendEvent(getString(R.string.analytic_sign_up), getString(R.string.analytic_out));
    }

    private void initView() {
        edtFirstName = (EditText) findViewById(R.id.edtFirstName);
        edtLastName = (EditText) findViewById(R.id.edtLastName);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtDOB = (EditText) findViewById(R.id.edtDOB);
        rltSignup = (RelativeLayout) findViewById(R.id.rltSignup);
        txtTerm = (TextView) findViewById(R.id.txtTerm);
        txtSignin = (TextView) findViewById(R.id.txtSignin);

        rltSignup.setOnClickListener(this);
        txtSignin.setOnClickListener(this);

        Font font = new Font(this);
        font.overrideFontsLight(findViewById(R.id.root));
        font.overrideFontsBold(findViewById(R.id.txtSignup));
        font.overrideFontsBold(txtSignin);

        clickify(txtTerm, getString(R.string.term), new ClickSpan.OnClickListener() {
            @Override
            public void onClick() {
                Intent intentTerm = new Intent(RegisterActivity.this, TermActivity.class);
                startActivity(intentTerm);
            }
        });

        keyboardVisibilityListener = new KeyboardVisibilityListener();
        keyboardVisibilityListener.setKeyboardListener(this, this,
                R.id.lnlRoot);

        edtPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                int result = actionId & EditorInfo.IME_MASK_ACTION;
                switch (result) {
                    case EditorInfo.IME_ACTION_DONE:
                        register();
                        break;
                }
                return false;
            }
        });
    }

    private void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltSignup:
                register();
                break;
            case R.id.txtSignin:
                finish();
                break;
        }
    }

    public void clickify(TextView view, final String clickableText, final ClickSpan.OnClickListener listener) {

        CharSequence text = view.getText();
        String string = text.toString();
        ClickSpan span = new ClickSpan(listener);

        int start = string.indexOf(clickableText);
        int end = start + clickableText.length();
        if (start == -1) return;

        if (text instanceof Spannable) {
            ((Spannable) text).setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ((Spannable) text).setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ((Spannable) text).setSpan(new UnderlineSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            SpannableString s = SpannableString.valueOf(text);
            s.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            s.setSpan(new UnderlineSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            view.setText(s);
        }

        MovementMethod m = view.getMovementMethod();
        if ((m == null) || !(m instanceof LinkMovementMethod)) {
            view.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    @Override
    public void onVisibilityChanged(boolean visible) {
        if (visible) {
            txtTerm.setVisibility(View.GONE);
        } else {
            txtTerm.setVisibility(View.VISIBLE);
        }
    }

    private void register() {
        String firstname = edtFirstName.getText().toString().trim();
        String lastname = edtLastName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        if (!StaticFunction.isNetworkAvailable(this)) {
            showToastError(getString(R.string.no_internet));
        } else if (firstname.length() == 0) {
            showToastError(getString(R.string.blank_firstname));
        } else if (lastname.length() == 0) {
            showToastError(getString(R.string.blank_lastname));
        } else if (email.length() == 0) {
            showToastError(getString(R.string.blank_email));
        } else if (password.length() == 0) {
            showToastError(getString(R.string.blank_pass));
        } else {
            Register register = new Register(firstname, lastname, password, email);
            String body = new Gson().toJson(register);

            UICallback callback = new UICallback() {
                @Override
                public void onResult(boolean success, String data) {
                    if (success) {
                        try {
                            JSONObject object = new JSONObject(data);
                            String email = object.getString("email");
                            Intent intent = new Intent();
                            intent.putExtra("email", email);
                            setResult(RESULT_OK, intent);
                            finish();
                            showToastOk("Sign up successfully!");
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
            CustomerExecute.register(this, callback, body);
            showProgressDialog(false);
        }
    }
}
