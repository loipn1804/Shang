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
 * Created by USER on 3/15/2016.
 */
public class OrderApi {

    private String URL = Constant.BASE_URL + "api/rest/extend/orders";
    private HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private int TIME_OUT = 60 * 1000;

    public void getOrderList(ApiCallback callback, long customer_id, int page) {
        try {
            String custom_url = URL + "?";
            custom_url += "customer_id=" + customer_id + "&page=" + page;
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

    public void getOrderDetail(ApiCallback callback, long entity_id) {
        try {
            String custom_url = URL + "/" + entity_id;
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

    public void createOrder(ApiCallback callback, String body) {
        try {
            String custom_url = URL;
            GenericUrl requestUrl = new GenericUrl(custom_url);

            HttpRequestFactory requestFactory = HTTP_TRANSPORT
                    .createRequestFactory(new HttpRequestInitializer() {
                        @Override
                        public void initialize(HttpRequest request) {

                            request.getHeaders().setAccept("application/xml");
                        }
                    });

            HttpRequest request = requestFactory.buildPostRequest(requestUrl, ByteArrayContent.fromString("application/json", body));
            request.setReadTimeout(TIME_OUT * 5);

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
