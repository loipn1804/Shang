package digimatic.shangcommerce.connection.service;

import android.app.IntentService;
import android.content.Intent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import digimatic.shangcommerce.connection.api.CategoryApi;
import digimatic.shangcommerce.connection.callback.ApiCallback;
import digimatic.shangcommerce.daocontroller.CategoryController;
import greendao.Category;

/**
 * Created by USER on 3/11/2016.
 */
public class CategoryService extends IntentService {

    // action
    public static final String ACTION_GET_ALL = "ACTION_GET_ALL";

    // receiver
    public static final String RECEIVER_GET_ALL = "RECEIVER_GET_ALL";

    // extra code and result value
    public static final String EXTRA_RESULT = "EXTRA_RESULT";
    public static final String RESULT_OK = "RESULT_OK";
    public static final String RESULT_FAIL = "RESULT_FAIL";

    public static final String EXTRA_RESULT_DATA = "EXTRA_RESULT_DATA";

    // extra
    public static final String EXTRA_BODY = "EXTRA_BODY";

    public CategoryService() {
        super(CategoryService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        if (action.equals(ACTION_GET_ALL)) {
            getAll(RECEIVER_GET_ALL);
        }
    }

    private void sendBroadCast(String receiver, String result, String message) {
        Intent intent = new Intent();
        intent.setAction(receiver);
        intent.putExtra(EXTRA_RESULT, result);
        intent.putExtra(EXTRA_RESULT_DATA, message);
        sendBroadcast(intent);
    }

    private void getAll(final String receiver) {
        ApiCallback callback = new ApiCallback() {
            @Override
            public void onResult(boolean success, String data) {
                if (success) {
                    try {
                        JSONArray array = new JSONArray(data);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            long entity_id = object.getLong("entity_id");
                            String name = object.getString("name");
                            int position = object.getInt("position");
                            String image_url = object.getString("image_url");
                            if (image_url.equalsIgnoreCase("null")) image_url = "";
                            String thumbnail_url = object.getString("thumbnail_url");
                            if (thumbnail_url.equalsIgnoreCase("null")) thumbnail_url = "";
                            int product_count = object.getInt("product_count");

                            CategoryController.insertOrUpdate(CategoryService.this, new Category(entity_id, name, position, image_url, thumbnail_url, product_count));
                        }

                        sendBroadCast(receiver, RESULT_OK, data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        sendBroadCast(receiver, RESULT_FAIL, e.getMessage());
                    }
                } else {
                    sendBroadCast(receiver, RESULT_FAIL, data);
                }
            }
        };
        CategoryApi api = new CategoryApi();
        api.getAll(callback);
    }
}
