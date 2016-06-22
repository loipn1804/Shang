package digimatic.shangcommerce.connection.threadconnection.execute;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.connection.api.CRMApi;
import digimatic.shangcommerce.connection.callback.ApiCallback;
import digimatic.shangcommerce.connection.callback.UICallback;
import digimatic.shangcommerce.connection.threadconnection.thread.BackgroundThreadExecutor;
import digimatic.shangcommerce.connection.threadconnection.thread.UIThreadExecutor;
import digimatic.shangcommerce.staticfunction.StaticFunction;

/**
 * Created by USER on 04/29/2016.
 */
public class CRMExecute {

    public static void CARDENQUIRY(final Context context, final UICallback callback, final String body) {
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
                                } else if (!success) {
                                    callback.onResult(false, data);
                                } else {
                                    try {
                                        JSONObject object = new JSONObject(data);
                                        JSONObject Result = object.getJSONObject("Result");
                                        boolean HasError = Result.getBoolean("HasError");
                                        if (!HasError) {
                                            JSONObject Data = object.getJSONObject("Data");
                                            callback.onResult(true, Data.toString());
                                        } else {
                                            String ErrorMessage = Result.getString("ErrorMessage");
                                            callback.onResult(false, ErrorMessage);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        callback.onResult(false, e.toString());
                                    }
                                }
                            }
                        });
                    }
                };
                CRMApi api = new CRMApi();
                api.CARDENQUIRY(apiCallback, body);
            }
        });
    }

    public static void GetItemList(final Context context, final UICallback callback, final String body) {
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
                                } else if (!success) {
                                    callback.onResult(false, data);
                                } else {
                                    try {
                                        JSONObject object = new JSONObject(data);
                                        JSONObject Result = object.getJSONObject("Result");
                                        boolean HasError = Result.getBoolean("HasError");
                                        if (!HasError) {
                                            JSONObject Data = object.getJSONObject("Data");
                                            callback.onResult(true, Data.toString());
                                        } else {
                                            String ErrorMessage = Result.getString("ErrorMessage");
                                            callback.onResult(false, ErrorMessage);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        callback.onResult(false, e.toString());
                                    }
                                }
                            }
                        });
                    }
                };
                CRMApi api = new CRMApi();
                api.GetItemList(apiCallback, body);
            }
        });
    }

    public static void GetRedeemableVoucherList(final Context context, final UICallback callback, final String body) {
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
                                } else if (!success) {
                                    callback.onResult(false, data);
                                } else {
                                    try {
                                        JSONObject object = new JSONObject(data);
                                        JSONObject Result = object.getJSONObject("Result");
                                        boolean HasError = Result.getBoolean("HasError");
                                        if (!HasError) {
                                            JSONObject Data = object.getJSONObject("Data");
                                            callback.onResult(true, Data.toString());
                                        } else {
                                            String ErrorMessage = Result.getString("ErrorMessage");
                                            callback.onResult(false, ErrorMessage);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        callback.onResult(false, e.toString());
                                    }
                                }
                            }
                        });
                    }
                };
                CRMApi api = new CRMApi();
                api.GetRedeemableVoucherList(apiCallback, body);
            }
        });
    }

    public static void VoucherConversion(final Context context, final UICallback callback, final String body) {
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
                                } else if (!success) {
                                    callback.onResult(false, data);
                                } else {
                                    try {
                                        JSONObject object = new JSONObject(data);
                                        JSONObject Result = object.getJSONObject("Result");
                                        boolean HasError = Result.getBoolean("HasError");
                                        if (!HasError) {
                                            JSONObject Data = object.getJSONObject("Data");
                                            callback.onResult(true, Data.toString());
                                        } else {
                                            String ErrorMessage = Result.getString("ErrorMessage");
                                            callback.onResult(false, ErrorMessage);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        callback.onResult(false, e.toString());
                                    }
                                }
                            }
                        });
                    }
                };
                CRMApi api = new CRMApi();
                api.VoucherConversion(apiCallback, body);
            }
        });
    }

    public static void GetRedeemHistory(final Context context, final UICallback callback, final String body) {
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
                                } else if (!success) {
                                    callback.onResult(false, data);
                                } else {
                                    try {
                                        JSONObject object = new JSONObject(data);
                                        JSONObject Result = object.getJSONObject("Result");
                                        boolean HasError = Result.getBoolean("HasError");
                                        if (!HasError) {
                                            JSONObject Data = object.getJSONObject("Data");
                                            callback.onResult(true, Data.toString());
                                        } else {
                                            String ErrorMessage = Result.getString("ErrorMessage");
                                            callback.onResult(false, ErrorMessage);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        callback.onResult(false, e.toString());
                                    }
                                }
                            }
                        });
                    }
                };
                CRMApi api = new CRMApi();
                api.GetRedeemHistory(apiCallback, body);
            }
        });
    }

    public static void VoucherRedeem(final Context context, final UICallback callback, final String body) {
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
                                } else if (!success) {
                                    callback.onResult(false, data);
                                } else {
                                    try {
                                        JSONObject object = new JSONObject(data);
                                        JSONObject Result = object.getJSONObject("Result");
                                        boolean HasError = Result.getBoolean("HasError");
                                        if (!HasError) {
                                            JSONObject Data = object.getJSONObject("Data");
                                            callback.onResult(true, Data.toString());
                                        } else {
                                            String ErrorMessage = Result.getString("ErrorMessage");
                                            callback.onResult(false, ErrorMessage);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        callback.onResult(false, e.toString());
                                    }
                                }
                            }
                        });
                    }
                };
                CRMApi api = new CRMApi();
                api.VoucherRedeem(apiCallback, body);
            }
        });
    }

    public static void GiftRedeem(final Context context, final UICallback callback, final String body) {
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
                                } else if (!success) {
                                    callback.onResult(false, data);
                                } else {
                                    try {
                                        JSONObject object = new JSONObject(data);
                                        JSONObject Result = object.getJSONObject("Result");
                                        boolean HasError = Result.getBoolean("HasError");
                                        if (!HasError) {
                                            JSONObject Data = object.getJSONObject("Data");
                                            callback.onResult(true, Data.toString());
                                        } else {
                                            String ErrorMessage = Result.getString("ErrorMessage");
                                            callback.onResult(false, ErrorMessage);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        callback.onResult(false, e.toString());
                                    }
                                }
                            }
                        });
                    }
                };
                CRMApi api = new CRMApi();
                api.GiftRedeem(apiCallback, body);
            }
        });
    }
}
