package digimatic.shangcommerce.connection.threadconnection.execute;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.connection.api.CustomerApi;
import digimatic.shangcommerce.connection.callback.ApiCallback;
import digimatic.shangcommerce.connection.callback.UICallback;
import digimatic.shangcommerce.connection.threadconnection.thread.BackgroundThreadExecutor;
import digimatic.shangcommerce.connection.threadconnection.thread.UIThreadExecutor;
import digimatic.shangcommerce.staticfunction.StaticFunction;

/**
 * Created by USER on 3/11/2016.
 */
public class CustomerExecute {

    public static void login(final Context context, final UICallback callback, final String body) {
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
//                                    callback.onResult(false, context.getString(R.string.some_thing_wrong));
                                    callback.onResult(false, data + "");
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
                CustomerApi api = new CustomerApi();
                api.login(apiCallback, body);
            }
        });
    }

    public static void loginFacebook(final Context context, final UICallback callback, final String body) {
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
                CustomerApi api = new CustomerApi();
                api.loginFacebook(apiCallback, body);
            }
        });
    }

    public static void getCustomerDetail(final Context context, final UICallback callback, final long customer_id) {
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
                CustomerApi api = new CustomerApi();
                api.getCustomerDetail(apiCallback, customer_id);
            }
        });
    }

    public static void getCustomerCounter(final Context context, final UICallback callback, final long customer_id) {
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
                CustomerApi api = new CustomerApi();
                api.getCustomerCounter(apiCallback, customer_id);
            }
        });
    }

    public static void register(final Context context, final UICallback callback, final String body) {
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
                CustomerApi api = new CustomerApi();
                api.register(apiCallback, body);
            }
        });
    }

    public static void changePass(final Context context, final UICallback callback, final String id, final String body) {
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
                CustomerApi api = new CustomerApi();
                api.changePass(apiCallback, id, body);
            }
        });
    }

    public static void getAddressList(final Context context, final UICallback callback, final long customer_id) {
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
                CustomerApi api = new CustomerApi();
                api.getAddressList(apiCallback, customer_id);
            }
        });
    }

    public static void updateAddress(final Context context, final UICallback callback, final long address_id, final String body) {
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
                CustomerApi api = new CustomerApi();
                api.updateAddress(apiCallback, address_id, body);
            }
        });
    }

    public static void createAddress(final Context context, final UICallback callback, final long customer_id, final String body) {
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
                CustomerApi api = new CustomerApi();
                api.createAddress(apiCallback, customer_id, body);
            }
        });
    }

    public static void deleteAddress(final Context context, final UICallback callback, final long address_id) {
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
                CustomerApi api = new CustomerApi();
                api.deleteAddress(apiCallback, address_id);
            }
        });
    }

    public static void getCountries(final Context context, final UICallback callback) {
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
                CustomerApi api = new CustomerApi();
                api.getCountries(apiCallback);
            }
        });
    }

    public static void uploadAvatar(final Context context, final UICallback callback, final long customer_id, final String body) {
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
                CustomerApi api = new CustomerApi();
                api.uploadAvatar(apiCallback, customer_id, body);
            }
        });
    }

    public static void sendFeedBack(final Context context, final UICallback callback, final String body) {
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
                CustomerApi api = new CustomerApi();
                api.sendFeedBack(apiCallback, body);
            }
        });
    }

    public static void updateProfile(final Context context, final UICallback callback, final long customer_id, final String body) {
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
                CustomerApi api = new CustomerApi();
                api.updateProfile(apiCallback, customer_id, body);
            }
        });
    }

    public static void registerNotification(final Context context, final UICallback callback, final String body) {
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
                                } else {
                                    callback.onResult(success, data);
                                }
                            }
                        });
                    }
                };
                CustomerApi api = new CustomerApi();
                api.registerNotification(apiCallback, body);
            }
        });
    }

    public static void logout(final Context context, final UICallback callback, final String body) {
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
                CustomerApi api = new CustomerApi();
                api.logout(apiCallback, body);
            }
        });
    }

    public static void updateNotification(final Context context, final UICallback callback, final long customer_id, final String body) {
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
                CustomerApi api = new CustomerApi();
                api.updateNotification(apiCallback, customer_id, body);
            }
        });
    }

    public static void forgotPassword(final Context context, final UICallback callback, final String body) {
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
                CustomerApi api = new CustomerApi();
                api.forgotPassword(apiCallback, body);
            }
        });
    }
}
