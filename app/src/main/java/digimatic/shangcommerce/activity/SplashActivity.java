package digimatic.shangcommerce.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.connection.callback.UICallback;
import digimatic.shangcommerce.connection.threadconnection.execute.CustomerExecute;
import digimatic.shangcommerce.connection.threadconnection.execute.WishlistExecute;
import digimatic.shangcommerce.daocontroller.CountryController;
import digimatic.shangcommerce.daocontroller.CustomerController;
import digimatic.shangcommerce.daocontroller.WishlistController;
import greendao.Country;
import greendao.Customer;
import greendao.Wishlist;

/**
 * Created by USER on 3/4/2016.
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (CustomerController.isLogin(SplashActivity.this)) {
            Customer customer = CustomerController.getCurrentCustomer(this);
            getWishlist(customer);
        } else {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isFirstRun()) {
                        Intent intent = new Intent(SplashActivity.this, IntroP3Activity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }, 500);
        }
    }

    private void getWishlist(final Customer customer) {
        UICallback callback = new UICallback() {
            @Override
            public void onResult(boolean success, String data) {
                if (success) {
                    try {
                        JSONArray array = new JSONArray(data);
                        WishlistController.clearAll(SplashActivity.this);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            long wishlist_item_id = object.getLong("wishlist_item_id");
                            long product_id = object.getLong("product_id");
                            String name = object.getString("name");
                            String sku = object.getString("sku");
                            String manufacturer = object.optString("manufacturer", "");
                            String price = object.getString("price");
                            if (price.equalsIgnoreCase("null")) price = "";
                            float price_f = 0;
                            if (price.length() > 0) {
                                price_f = Float.parseFloat(price);
                            }
                            String image_url = object.getString("image_url");
                            String added_at = object.getString("added_at");
                            String type_id = object.getString("type_id");

                            WishlistController.insertOrUpdate(SplashActivity.this, new Wishlist(wishlist_item_id, product_id, name, sku, manufacturer, price_f, image_url, added_at, type_id));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showToastError(e.getMessage());
                    }
                } else {
                    showToastError(data);
                }
                if (isFirstRun()) {
                    Intent intent = new Intent(SplashActivity.this, IntroP3Activity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        WishlistExecute.getByCustomerId(this, callback, customer.getEntity_id());
    }

    private boolean isFirstRun() {
        SharedPreferences preferences = getSharedPreferences("firstRun", MODE_PRIVATE);
        boolean isFirst = preferences.getBoolean("isFirst", true);
        if (isFirst) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isFirst", false);
            editor.commit();
            return true;
        } else {
            return false;
        }
    }
}
