package digimatic.shangcommerce.connection.threadconnection.execute;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.connection.api.NotificationApi;
import digimatic.shangcommerce.connection.callback.ApiCallback;
import digimatic.shangcommerce.connection.callback.UICallback;
import digimatic.shangcommerce.connection.threadconnection.thread.BackgroundThreadExecutor;
import digimatic.shangcommerce.connection.threadconnection.thread.UIThreadExecutor;
import digimatic.shangcommerce.staticfunction.StaticFunction;

/**
 * Created by USER on 3/22/2016.
 */
public class NotificationExecute {

    public static void getAll(final Context context, final UICallback callback, final long customer_id, final int page) {
        if (!StaticFunction.isNetworkAvailable(context)) {
            callback.onResult(false, context.getString(R.string.no_internet));
            return;
        }
        BackgroundThreadExecutor.getInstance().runOnBackground(new Runnable() {
            @Override
            public void run() {
                ApiCallback apiCallback = new ApiCallback() {
                    @Override
                    public void onResult(final boolean success, final String data) {
                        UIThreadExecutor.getInstance().runOnUIThread(new Runnable() {
                            @Override
                            public void run() {
                                if (data == null) {
                                    callback.onResult(false, context.getString(R.string.some_thing_wrong));
                                } else if (success) {
                                    callback.onResult(success, data);
                                } else {
                                    try {
                                        if (data.contains("\n")) {
                                            int i = data.indexOf("\n");
                                            String s = data.substring(i + 1, data.length());
                                            JSONObject object = new JSONObject(s);
                                            JSONObject messages = object.getJSONObject("messages");
                                            JSONArray error = messages.getJSONArray("error");
                                            JSONObject errorObj = error.getJSONObject(0);
                                            String message = errorObj.getString("message");
                                            callback.onResult(success, message);
                                        } else {
                                            callback.onResult(success, data);
                                        }
                                    } catch (Exception ex) {
                                        callback.onResult(success, ex.getMessage());
                                    }
                                }
                            }
                        });
                    }
                };
                NotificationApi api = new NotificationApi();
                api.getAll(apiCallback, customer_id, page);
            }
        });
    }

    public static void deleteById(final Context context, final UICallback callback, final long id) {
        if (!StaticFunction.isNetworkAvailable(context)) {
            callback.onResult(false, context.getString(R.string.no_internet));
            return;
        }
        BackgroundThreadExecutor.getInstance().runOnBackground(new Runnable() {
            @Override
            public void run() {
                ApiCallback apiCallback = new ApiCallback() {
                    @Override
                    public void onResult(final boolean success, final String data) {
                        UIThreadExecutor.getInstance().runOnUIThread(new Runnable() {
                            @Override
                            public void run() {
                                if (data == null) {
                                    callback.onResult(false, context.getString(R.string.some_thing_wrong));
                                } else if (success) {
                                    callback.onResult(success, data);
                                } else {
                                    try {
                                        if (data.contains("\n")) {
                                            int i = data.indexOf("\n");
                                            String s = data.substring(i + 1, data.length());
                                            JSONObject object = new JSONObject(s);
                                            JSONObject messages = object.getJSONObject("messages");
                                            JSONArray error = messages.getJSONArray("error");
                                            JSONObject errorObj = error.getJSONObject(0);
                                            String message = errorObj.getString("message");
                                            callback.onResult(success, message);
                                        } else {
                                            callback.onResult(success, data);
                                        }
                                    } catch (Exception ex) {
                                        callback.onResult(success, ex.getMessage());
                                    }
                                }
                            }
                        });
                    }
                };
                NotificationApi api = new NotificationApi();
                api.deleteById(apiCallback, id);
            }
        });
    }

    public static void updateById(final Context context, final UICallback callback, final long id, final String body) {
        if (!StaticFunction.isNetworkAvailable(context)) {
            callback.onResult(false, context.getString(R.string.no_internet));
            return;
        }
        BackgroundThreadExecutor.getInstance().runOnBackground(new Runnable() {
            @Override
            public void run() {
                ApiCallback apiCallback = new ApiCallback() {
                    @Override
                    public void onResult(final boolean success, final String data) {
                        UIThreadExecutor.getInstance().runOnUIThread(new Runnable() {
                            @Override
                            public void run() {
                                if (data == null) {
                                    callback.onResult(false, context.getString(R.string.some_thing_wrong));
                                } else if (success) {
                                    callback.onResult(success, data);
                                } else {
                                    try {
                                        if (data.contains("\n")) {
                                            int i = data.indexOf("\n");
                                            String s = data.substring(i + 1, data.length());
                                            JSONObject object = new JSONObject(s);
                                            JSONObject messages = object.getJSONObject("messages");
                                            JSONArray error = messages.getJSONArray("error");
                                            JSONObject errorObj = error.getJSONObject(0);
                                            String message = errorObj.getString("message");
                                            callback.onResult(success, message);
                                        } else {
                                            callback.onResult(success, data);
                                        }
                                    } catch (Exception ex) {
                                        callback.onResult(success, ex.getMessage());
                                    }
                                }
                            }
                        });
                    }
                };
                NotificationApi api = new NotificationApi();
                api.updateById(apiCallback, id, body);
            }
        });
    }
}
