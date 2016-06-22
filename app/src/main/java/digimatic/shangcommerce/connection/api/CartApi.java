package digimatic.shangcommerce.connection.api;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;

import java.io.IOException;

import digimatic.shangcommerce.connection.Constant;
import digimatic.shangcommerce.connection.callback.ApiCallback;

/**
 * Created by USER on 3/14/2016.
 */
public class CartApi {

    private String URL = Constant.BASE_URL + "api/rest/extend/checkout/cart";
    private HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private int TIME_OUT = 60 * 1000;

    public void getCart(ApiCallback callback, long customer_id) {
        try {
            String custom_url = URL + "?";
            custom_url += "customer_id=" + customer_id;
            GenericUrl requestUrl = new GenericUrl(custom_url);

            HttpRequestFactory requestFactory = HTTP_TRANSPORT
                    .createRequestFactory(new HttpRequestInitializer() {
                        @Override
                        public void initialize(HttpRequest request) {

                            request.getHeaders().setAccept("application/xml");
                        }
                    });

            HttpRequest request = requestFactory.buildGetRequest(requestUrl);
            request.setReadTimeout(TIME_OUT);

            Constant.getOAuthParam().initialize(request);

            HttpResponse response = request.execute();
            if (response.isSuccessStatusCode()) {
                callback.onResult(true, response.parseAsString());
            } else {
                callback.onResult(false, response.getStatusCode() + ":" + response.getStatusMessage());
            }

        } catch (IOException e) {
            callback.onResult(false, e.getMessage());
        }
    }

    public void addProductToCart(ApiCallback callback, String body) {
        try {
            GenericUrl requestUrl = new GenericUrl(URL);

            HttpRequestFactory requestFactory = HTTP_TRANSPORT
                    .createRequestFactory(new HttpRequestInitializer() {
                        @Override
                        public void initialize(HttpRequest request) {

                            request.getHeaders().setAccept("application/json");
                        }
                    });

//                HttpRequest request = requestFactory.buildGetRequest(requestUrl);
            HttpRequest request = requestFactory.buildPostRequest(requestUrl, ByteArrayContent.fromString("application/json", body));
            request.setReadTimeout(TIME_OUT);

            Constant.getOAuthParam().initialize(request);

            HttpResponse response = request.execute();
            if (response.isSuccessStatusCode()) {
                callback.onResult(true, response.parseAsString());
            } else {
                callback.onResult(false, response.getStatusCode() + ":" + response.getStatusMessage());
            }

        } catch (IOException e) {
            callback.onResult(false, e.getMessage());
        }
    }

    public void deleteProductFromCart(ApiCallback callback, long item_id, long customer_id) {
        try {
            String custom_url = URL + "/" + item_id + "?";
            custom_url += "customer_id=" + customer_id;
            GenericUrl requestUrl = new GenericUrl(custom_url);

            HttpRequestFactory requestFactory = HTTP_TRANSPORT
                    .createRequestFactory(new HttpRequestInitializer() {
                        @Override
                        public void initialize(HttpRequest request) {

                            request.getHeaders().setAccept("application/xml");
                        }
                    });

            HttpRequest request = requestFactory.buildDeleteRequest(requestUrl);
            request.setReadTimeout(TIME_OUT);

            Constant.getOAuthParam().initialize(request);

            HttpResponse response = request.execute();
            if (response.isSuccessStatusCode()) {
                callback.onResult(true, response.parseAsString());
            } else {
                callback.onResult(false, response.getStatusCode() + ":" + response.getStatusMessage());
            }

        } catch (IOException e) {
            callback.onResult(false, e.getMessage());
        }
    }

    public void updateProductInCart(ApiCallback callback, long item_id, String body) {
        try {
            GenericUrl requestUrl = new GenericUrl(URL + "/" + item_id);

            HttpRequestFactory requestFactory = HTTP_TRANSPORT
                    .createRequestFactory(new HttpRequestInitializer() {
                        @Override
                        public void initialize(HttpRequest request) {

                            request.getHeaders().setAccept("application/xml");
                        }
                    });

            HttpRequest request = requestFactory.buildPutRequest(requestUrl, ByteArrayContent.fromString("application/json", body));
            request.setReadTimeout(TIME_OUT);

            Constant.getOAuthParam().initialize(request);

            HttpResponse response = request.execute();
            if (response.isSuccessStatusCode()) {
                callback.onResult(true, response.parseAsString());
            } else {
                callback.onResult(false, response.getStatusCode() + ":" + response.getStatusMessage());
            }

        } catch (IOException e) {
            callback.onResult(false, e.getMessage());
        }
    }

    public void getDeliveryType(ApiCallback callback, long cart_id) {
        try {
            String custom_url = Constant.BASE_URL + "api/rest/extend/checkout/cart/" + cart_id + "/shipping";
            GenericUrl requestUrl = new GenericUrl(custom_url);

            HttpRequestFactory requestFactory = HTTP_TRANSPORT
                    .createRequestFactory(new HttpRequestInitializer() {
                        @Override
                        public void initialize(HttpRequest request) {

                            request.getHeaders().setAccept("application/xml");
                        }
                    });

            HttpRequest request = requestFactory.buildGetRequest(requestUrl);
            request.setReadTimeout(TIME_OUT);

            Constant.getOAuthParam().initialize(request);

            HttpResponse response = request.execute();
            if (response.isSuccessStatusCode()) {
                callback.onResult(true, response.parseAsString());
            } else {
                callback.onResult(false, response.getStatusCode() + ":" + response.getStatusMessage());
            }

        } catch (IOException e) {
            callback.onResult(false, e.getMessage());
        }
    }

    public void getPaymentMethod(ApiCallback callback) {
        try {
            String custom_url = Constant.BASE_URL + "api/rest/extend/payment/methods";
            GenericUrl requestUrl = new GenericUrl(custom_url);

            HttpRequestFactory requestFactory = HTTP_TRANSPORT
                    .createRequestFactory(new HttpRequestInitializer() {
                        @Override
                        public void initialize(HttpRequest request) {

                            request.getHeaders().setAccept("application/xml");
                        }
                    });

            HttpRequest request = requestFactory.buildGetRequest(requestUrl);
            request.setReadTimeout(TIME_OUT);

            Constant.getOAuthParam().initialize(request);

            HttpResponse response = request.execute();
            if (response.isSuccessStatusCode()) {
                callback.onResult(true, response.parseAsString());
            } else {
                callback.onResult(false, response.getStatusCode() + ":" + response.getStatusMessage());
            }

        } catch (IOException e) {
            callback.onResult(false, e.getMessage());
        }
    }

    public void applyCoupon(ApiCallback callback, long cart_id, String body) {
        try {
            GenericUrl requestUrl = new GenericUrl(URL + "/" + cart_id + "/coupon");

            HttpRequestFactory requestFactory = HTTP_TRANSPORT
                    .createRequestFactory(new HttpRequestInitializer() {
                        @Override
                        public void initialize(HttpRequest request) {

                            request.getHeaders().setAccept("application/json");
                        }
                    });

            HttpRequest request = requestFactory.buildPostRequest(requestUrl, ByteArrayContent.fromString("application/json", body));
            request.setReadTimeout(TIME_OUT);

            Constant.getOAuthParam().initialize(request);

            HttpResponse response = request.execute();
            if (response.isSuccessStatusCode()) {
                callback.onResult(true, response.parseAsString());
            } else {
                callback.onResult(false, response.getStatusCode() + ":" + response.getStatusMessage());
            }

        } catch (IOException e) {
            callback.onResult(false, e.getMessage());
        }
    }

    public void removeCoupon(ApiCallback callback, long cart_id, String coupon_code) {
        try {
            GenericUrl requestUrl = new GenericUrl(URL + "/" + cart_id + "/coupon/" + coupon_code);

            HttpRequestFactory requestFactory = HTTP_TRANSPORT
                    .createRequestFactory(new HttpRequestInitializer() {
                        @Override
                        public void initialize(HttpRequest request) {

                            request.getHeaders().setAccept("application/json");
                        }
                    });

            HttpRequest request = requestFactory.buildDeleteRequest(requestUrl);
            request.setReadTimeout(TIME_OUT);

            Constant.getOAuthParam().initialize(request);

            HttpResponse response = request.execute();
            if (response.isSuccessStatusCode()) {
                callback.onResult(true, response.parseAsString());
            } else {
                callback.onResult(false, response.getStatusCode() + ":" + response.getStatusMessage());
            }

        } catch (IOException e) {
            callback.onResult(false, e.getMessage());
        }
    }

    public void updateShippingAddress(ApiCallback callback, long cart_id, String body) {
        try {
            GenericUrl requestUrl = new GenericUrl(URL + "/" + cart_id + "/shipping");

            HttpRequestFactory requestFactory = HTTP_TRANSPORT
                    .createRequestFactory(new HttpRequestInitializer() {
                        @Override
                        public void initialize(HttpRequest request) {

                            request.getHeaders().setAccept("application/json");
                        }
                    });

            HttpRequest request = requestFactory.buildPostRequest(requestUrl, ByteArrayContent.fromString("application/json", body));
            request.setReadTimeout(TIME_OUT);

            Constant.getOAuthParam().initialize(request);

            HttpResponse response = request.execute();
            if (response.isSuccessStatusCode()) {
                callback.onResult(true, response.parseAsString());
            } else {
                callback.onResult(false, response.getStatusCode() + ":" + response.getStatusMessage());
            }

        } catch (IOException e) {
            callback.onResult(false, e.getMessage());
        }
    }

    public void updateBillingAddress(ApiCallback callback, long cart_id, String body) {
        try {
            GenericUrl requestUrl = new GenericUrl(URL + "/" + cart_id + "/billing");

            HttpRequestFactory requestFactory = HTTP_TRANSPORT
                    .createRequestFactory(new HttpRequestInitializer() {
                        @Override
                        public void initialize(HttpRequest request) {

                            request.getHeaders().setAccept("application/json");
                        }
                    });

            HttpRequest request = requestFactory.buildPostRequest(requestUrl, ByteArrayContent.fromString("application/json", body));
            request.setReadTimeout(TIME_OUT);

            Constant.getOAuthParam().initialize(request);

            HttpResponse response = request.execute();
            if (response.isSuccessStatusCode()) {
                callback.onResult(true, response.parseAsString());
            } else {
                callback.onResult(false, response.getStatusCode() + ":" + response.getStatusMessage());
            }

        } catch (IOException e) {
            callback.onResult(false, e.getMessage());
        }
    }

    public void updateShippingMethod(ApiCallback callback, long cart_id, String body) {
        try {
            GenericUrl requestUrl = new GenericUrl(URL + "/" + cart_id + "/shippingMethod");

            HttpRequestFactory requestFactory = HTTP_TRANSPORT
                    .createRequestFactory(new HttpRequestInitializer() {
                        @Override
                        public void initialize(HttpRequest request) {

                            request.getHeaders().setAccept("application/json");
                        }
                    });

            HttpRequest request = requestFactory.buildPostRequest(requestUrl, ByteArrayContent.fromString("application/json", body));
            request.setReadTimeout(TIME_OUT);

            Constant.getOAuthParam().initialize(request);

            HttpResponse response = request.execute();
            if (response.isSuccessStatusCode()) {
                callback.onResult(true, response.parseAsString());
            } else {
                callback.onResult(false, response.getStatusCode() + ":" + response.getStatusMessage());
            }

        } catch (IOException e) {
            callback.onResult(false, e.getMessage());
        }
    }

    public void applyEVoucher(ApiCallback callback, long cart_id, String body) {
        try {
            GenericUrl requestUrl = new GenericUrl(URL + "/" + cart_id + "/evoucher");

            HttpRequestFactory requestFactory = HTTP_TRANSPORT
                    .createRequestFactory(new HttpRequestInitializer() {
                        @Override
                        public void initialize(HttpRequest request) {

                            request.getHeaders().setAccept("application/json");
                        }
                    });

            HttpRequest request = requestFactory.buildPostRequest(requestUrl, ByteArrayContent.fromString("application/json", body));
            request.setReadTimeout(TIME_OUT);

            Constant.getOAuthParam().initialize(request);

            HttpResponse response = request.execute();
            if (response.isSuccessStatusCode()) {
                callback.onResult(true, response.parseAsString());
            } else {
                callback.onResult(false, response.getStatusCode() + ":" + response.getStatusMessage());
            }

        } catch (IOException e) {
            callback.onResult(false, e.getMessage());
        }
    }

    public void removeEVoucher(ApiCallback callback, long cart_id, String voucher_code) {
        try {
            GenericUrl requestUrl = new GenericUrl(URL + "/" + cart_id + "/evoucher/" + voucher_code);

            HttpRequestFactory requestFactory = HTTP_TRANSPORT
                    .createRequestFactory(new HttpRequestInitializer() {
                        @Override
                        public void initialize(HttpRequest request) {

                            request.getHeaders().setAccept("application/json");
                        }
                    });

            HttpRequest request = requestFactory.buildDeleteRequest(requestUrl);
            request.setReadTimeout(TIME_OUT);

            Constant.getOAuthParam().initialize(request);

            HttpResponse response = request.execute();
            if (response.isSuccessStatusCode()) {
                callback.onResult(true, response.parseAsString());
            } else {
                callback.onResult(false, response.getStatusCode() + ":" + response.getStatusMessage());
            }

        } catch (IOException e) {
            callback.onResult(false, e.getMessage());
        }
    }
}
