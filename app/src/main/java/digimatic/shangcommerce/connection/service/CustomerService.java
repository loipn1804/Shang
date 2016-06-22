package digimatic.shangcommerce.connection.service;

import android.app.IntentService;
import android.content.Intent;

import org.json.JSONException;
import org.json.JSONObject;

import digimatic.shangcommerce.connection.api.CustomerApi;
import digimatic.shangcommerce.connection.callback.ApiCallback;

/**
 * Created by USER on 3/11/2016.
 */
public class CustomerService extends IntentService {

    // action
    public static final String ACTION_LOGIN = "ACTION_LOGIN";

    // receiver
    public static final String RECEIVER_LOGIN = "RECEIVER_LOGIN";

    // extra code and result value
    public static final String EXTRA_RESULT = "EXTRA_RESULT";
    public static final String RESULT_OK = "RESULT_OK";
    public static final String RESULT_FAIL = "RESULT_FAIL";

    public static final String EXTRA_RESULT_DATA = "EXTRA_RESULT_DATA";

    // extra
    public static final String EXTRA_BODY = "EXTRA_BODY";

    public CustomerService() {
        super(CustomerService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        if (action.equals(ACTION_LOGIN)) {
            String body = intent.getStringExtra(EXTRA_BODY);
            login(RECEIVER_LOGIN, body);
        }
    }

    private void sendBroadCast(String receiver, String result, String message) {
        Intent intent = new Intent();
        intent.setAction(receiver);
        intent.putExtra(EXTRA_RESULT, result);
        intent.putExtra(EXTRA_RESULT_DATA, message);
        sendBroadcast(intent);
    }

    private void login(final String receiver, String body) {
        ApiCallback callback = new ApiCallback() {
            @Override
            public void onResult(boolean success, String data) {
                if (success) {
                    try {
                        JSONObject object = new JSONObject(data);
                        String email = object.getString("email");
                        sendBroadCast(receiver, RESULT_OK, email);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        sendBroadCast(receiver, RESULT_FAIL, e.getMessage());
//                        try {
//                            JSONObject object = new JSONObject(data);
//                            JSONObject messages = object.getJSONObject("messages");
//                            JSONArray error = messages.getJSONArray("error");
//                            JSONObject errorObj = error.getJSONObject(0);
//                            String message = errorObj.getString("message");
//                            sendBroadCast(receiver, RESULT_FAIL, message);
//                        } catch (JSONException ex) {
//                            sendBroadCast(receiver, RESULT_FAIL, ex.getMessage());
//                        }
                    }
                } else {
                    sendBroadCast(receiver, RESULT_FAIL, data);
                }
            }
        };
        CustomerApi api = new CustomerApi();
        api.login(callback, body);
    }
}
