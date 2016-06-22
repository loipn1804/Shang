package digimatic.shangcommerce.connection.threadconnection.execute;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.connection.api.CartApi;
import digimatic.shangcommerce.connection.callback.ApiCallback;
import digimatic.shangcommerce.connection.callback.UICallback;
import digimatic.shangcommerce.connection.threadconnection.thread.BackgroundThreadExecutor;
import digimatic.shangcommerce.connection.threadconnection.thread.UIThreadExecutor;
import digimatic.shangcommerce.staticfunction.StaticFunction;

/**
 * Created by USER on 3/14/2016.
 */
public class CartExecute {

    public static void getCart(final Context context, final UICallback callback, final long customer_id) {
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
                CartApi api = new CartApi();
                api.getCart(apiCallback, customer_id);
            }
        });
    }

    public static void addProductToCart(final Context context, final UICallback callback, final String body) {
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
                CartApi api = new CartApi();
                api.addProductToCart(apiCallback, body);
            }
        });
    }

    public static void deleteProductFromCart(final Context context, final UICallback callback, final long item_id, final long customer_id) {
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
                CartApi api = new CartApi();
                api.deleteProductFromCart(apiCallback, item_id, customer_id);
            }
        });
    }

    public static void updateProductInCart(final Context context, final UICallback callback, final long item_id, final String body) {
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
                CartApi api = new CartApi();
                api.updateProductInCart(apiCallback, item_id, body);
            }
        });
    }

    public static void getDeliveryType(final Context context, final UICallback callback, final long cart_id) {
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
                CartApi api = new CartApi();
                api.getDeliveryType(apiCallback, cart_id);
            }
        });
    }

    public static void getPaymentMethod(final Context context, final UICallback callback) {
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
                CartApi api = new CartApi();
                api.getPaymentMethod(apiCallback);
            }
        });
    }

    public static void applyCoupon(final Context context, final UICallback callback, final long cart_id, final String body) {
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
                CartApi api = new CartApi();
                api.applyCoupon(apiCallback, cart_id, body);
            }
        });
    }

    public static void removeCoupon(final Context context, final UICallback callback, final long cart_id, final String coupon_code) {
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
                CartApi api = new CartApi();
                api.removeCoupon(apiCallback, cart_id, coupon_code);
            }
        });
    }

    public static void updateShippingAddress(final Context context, final UICallback callback, final long cart_id, final String body) {
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
                CartApi api = new CartApi();
                api.updateShippingAddress(apiCallback, cart_id, body);
            }
        });
    }

    public static void updateBillingAddress(final Context context, final UICallback callback, final long cart_id, final String body) {
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
                CartApi api = new CartApi();
                api.updateBillingAddress(apiCallback, cart_id, body);
            }
        });
    }

    public static void updateShippingMethod(final Context context, final UICallback callback, final long cart_id, final String body) {
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
                CartApi api = new CartApi();
                api.updateShippingMethod(apiCallback, cart_id, body);
            }
        });
    }

    public static void applyEVoucher(final Context context, final UICallback callback, final long cart_id, final String body) {
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
                CartApi api = new CartApi();
                api.applyEVoucher(apiCallback, cart_id, body);
            }
        });
    }

    public static void removeEVoucher(final Context context, final UICallback callback, final long cart_id, final String voucher_code) {
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
                CartApi api = new CartApi();
                api.removeEVoucher(apiCallback, cart_id, voucher_code);
            }
        });
    }
}
