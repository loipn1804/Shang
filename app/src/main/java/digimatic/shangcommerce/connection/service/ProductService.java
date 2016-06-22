package digimatic.shangcommerce.connection.service;

import android.app.IntentService;
import android.content.Intent;

import digimatic.shangcommerce.connection.api.ProductApi;
import digimatic.shangcommerce.connection.callback.ApiCallback;

/**
 * Created by USER on 3/11/2016.
 */
public class ProductService extends IntentService {

    // action
    public static final String ACTION_GET_BY_CATEGORY_ID = "ACTION_GET_BY_CATEGORY_ID";

    // receiver
    public static final String RECEIVER_GET_BY_CATEGORY_ID = "RECEIVER_GET_BY_CATEGORY_ID";

    // extra code and result value
    public static final String EXTRA_RESULT = "EXTRA_RESULT";
    public static final String RESULT_OK = "RESULT_OK";
    public static final String RESULT_FAIL = "RESULT_FAIL";

    public static final String EXTRA_RESULT_DATA = "EXTRA_RESULT_DATA";

    // extra
    public static final String EXTRA_BODY = "EXTRA_BODY";

    public static final String EXTRA_PAGE = "EXTRA_PAGE";
    public static final String EXTRA_CATEGORY_ID = "EXTRA_CATEGORY_ID";
    public static final String EXTRA_FRAGMENT_TYPE = "EXTRA_FRAGMENT_TYPE";

    public long CATEGORY_ID = 0;

    public ProductService() {
        super(ProductService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        if (action.equals(ACTION_GET_BY_CATEGORY_ID)) {
            long category_id = intent.getLongExtra(EXTRA_CATEGORY_ID, 0);
            int page = intent.getIntExtra(EXTRA_PAGE, 0);
            if (page > 0 && category_id > 0) {
                CATEGORY_ID = category_id;
                getByCategoryId(RECEIVER_GET_BY_CATEGORY_ID, category_id, page);
            }
        }
    }

    private void sendBroadCast(String receiver, String result, String message) {
        Intent intent = new Intent();
        intent.setAction(receiver);
        intent.putExtra(EXTRA_RESULT, result);
        intent.putExtra(EXTRA_RESULT_DATA, message);
        if (receiver.equalsIgnoreCase(RECEIVER_GET_BY_CATEGORY_ID)) {
            intent.putExtra(EXTRA_CATEGORY_ID, CATEGORY_ID);
        }
        sendBroadcast(intent);
    }

    private void getByCategoryId(final String receiver, long category_id, int page) {
        ApiCallback callback = new ApiCallback() {
            @Override
            public void onResult(boolean success, String data) {
                if (success) {
                    sendBroadCast(receiver, RESULT_OK, data);
                } else {
                    sendBroadCast(receiver, RESULT_FAIL, data);
                }
            }
        };
        ProductApi api = new ProductApi();
        api.getByCategoryId(callback, category_id, page);
    }
}
