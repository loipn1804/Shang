package digimatic.shangcommerce.connection.api;

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
 * Created by USER on 3/11/2016.
 */
public class ProductApi {

    private String URL = Constant.BASE_URL + "api/rest/extend/products";
    private HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private int TIME_OUT = 60 * 1000;

    public void getByCategoryId(ApiCallback callback, long category_id, int page) {
        try {
            String custom_url = URL + "?";
            custom_url += "category_id=" + category_id;
            custom_url += "&page=" + page;
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

    public void getBySearchKey(ApiCallback callback, String search_key, int page) {
        try {
            String custom_url = URL + "/search/" + search_key + "?";
            custom_url += "page=" + page;
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

        } catch (Exception e) {
            callback.onResult(false, e.getMessage());
        }
    }

    public void getProductById(ApiCallback callback, long product_id) {
        try {
            String custom_url = URL + "/" + product_id;
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

    public void getProductOption(ApiCallback callback, long product_id) {
        try {
            String custom_url = URL + "/" + product_id + "/options";
            GenericUrl requestUrl = new GenericUrl(custom_url);

            HttpRequestFactory requestFactory = HTTP_TRANSPORT
                    .createRequestFactory(new HttpRequestInitializer() {
                        @Override
                        public void initialize(HttpRequest request) {

                            request.getHeaders().setAccept("application/json");
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

    public void getProductConfigurable(ApiCallback callback, long product_id) {
        try {
            String custom_url = Constant.BASE_URL + "api/rest/extend/configurable-products/" + product_id + "/options";
            GenericUrl requestUrl = new GenericUrl(custom_url);

            HttpRequestFactory requestFactory = HTTP_TRANSPORT
                    .createRequestFactory(new HttpRequestInitializer() {
                        @Override
                        public void initialize(HttpRequest request) {

                            request.getHeaders().setAccept("application/json");
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
}
