package digimatic.shangcommerce.connection.api;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.UrlEncodedContent;
import com.google.api.client.http.javanet.NetHttpTransport;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import digimatic.shangcommerce.connection.Constant;
import digimatic.shangcommerce.connection.callback.ApiCallback;

/**
 * Created by USER on 04/29/2016.
 */
public class CRMApi {

    private String URL = "http://asiatic.ticp.net:7002/api/Trading/CP/";
    private HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private int TIME_OUT = 60 * 1000 * 2;

    public void CARDENQUIRY(ApiCallback callback, String body) {
        try {
            String url = URL + "CARDENQUIRY";
            GenericUrl requestUrl = new GenericUrl(url);

            HttpRequestFactory requestFactory = HTTP_TRANSPORT
                    .createRequestFactory(new HttpRequestInitializer() {
                        @Override
                        public void initialize(HttpRequest request) {

                            request.getHeaders().setAccept("application/x-www-form-urlencoded");
                        }
                    });

            Map<String, String> json = new HashMap<String, String>();
            json.put("request", body);
            HttpContent content = new UrlEncodedContent(json);
            HttpRequest request = requestFactory.buildPostRequest(requestUrl, content);
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

    public void GetItemList(ApiCallback callback, String body) {
        try {
            String url = URL + "GetItemList";
            GenericUrl requestUrl = new GenericUrl(url);

            HttpRequestFactory requestFactory = HTTP_TRANSPORT
                    .createRequestFactory(new HttpRequestInitializer() {
                        @Override
                        public void initialize(HttpRequest request) {

                            request.getHeaders().setAccept("application/x-www-form-urlencoded");
                        }
                    });

            Map<String, String> json = new HashMap<String, String>();
            json.put("request", body);
            HttpContent content = new UrlEncodedContent(json);
            HttpRequest request = requestFactory.buildPostRequest(requestUrl, content);
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

    public void GetRedeemableVoucherList(ApiCallback callback, String body) {
        try {
            String url = URL + "GetRedeemableVoucherList";
            GenericUrl requestUrl = new GenericUrl(url);

            HttpRequestFactory requestFactory = HTTP_TRANSPORT
                    .createRequestFactory(new HttpRequestInitializer() {
                        @Override
                        public void initialize(HttpRequest request) {

                            request.getHeaders().setAccept("application/x-www-form-urlencoded");
                        }
                    });

            Map<String, String> json = new HashMap<String, String>();
            json.put("request", body);
            HttpContent content = new UrlEncodedContent(json);
            HttpRequest request = requestFactory.buildPostRequest(requestUrl, content);
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

    public void VoucherConversion(ApiCallback callback, String body) {
        try {
            String url = URL + "VoucherConversion";
            GenericUrl requestUrl = new GenericUrl(url);

            HttpRequestFactory requestFactory = HTTP_TRANSPORT
                    .createRequestFactory(new HttpRequestInitializer() {
                        @Override
                        public void initialize(HttpRequest request) {

                            request.getHeaders().setAccept("application/x-www-form-urlencoded");
                        }
                    });

            Map<String, String> json = new HashMap<String, String>();
            json.put("request", body);
            HttpContent content = new UrlEncodedContent(json);
            HttpRequest request = requestFactory.buildPostRequest(requestUrl, content);
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

    public void GetRedeemHistory(ApiCallback callback, String body) {
        try {
            String url = URL + "GetRedeemHistory";
            GenericUrl requestUrl = new GenericUrl(url);

            HttpRequestFactory requestFactory = HTTP_TRANSPORT
                    .createRequestFactory(new HttpRequestInitializer() {
                        @Override
                        public void initialize(HttpRequest request) {

                            request.getHeaders().setAccept("application/x-www-form-urlencoded");
                        }
                    });

            Map<String, String> json = new HashMap<String, String>();
            json.put("request", body);
            HttpContent content = new UrlEncodedContent(json);
            HttpRequest request = requestFactory.buildPostRequest(requestUrl, content);
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

    public void VoucherRedeem(ApiCallback callback, String body) {
        try {
            String url = URL + "VoucherRedeem";
            GenericUrl requestUrl = new GenericUrl(url);

            HttpRequestFactory requestFactory = HTTP_TRANSPORT
                    .createRequestFactory(new HttpRequestInitializer() {
                        @Override
                        public void initialize(HttpRequest request) {

                            request.getHeaders().setAccept("application/x-www-form-urlencoded");
                        }
                    });

            Map<String, String> json = new HashMap<String, String>();
            json.put("request", body);
            HttpContent content = new UrlEncodedContent(json);
            HttpRequest request = requestFactory.buildPostRequest(requestUrl, content);
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

    public void GiftRedeem(ApiCallback callback, String body) {
        try {
            String url = URL + "GiftRedeem";
            GenericUrl requestUrl = new GenericUrl(url);

            HttpRequestFactory requestFactory = HTTP_TRANSPORT
                    .createRequestFactory(new HttpRequestInitializer() {
                        @Override
                        public void initialize(HttpRequest request) {

                            request.getHeaders().setAccept("application/x-www-form-urlencoded");
                        }
                    });

            Map<String, String> json = new HashMap<String, String>();
            json.put("request", body);
            HttpContent content = new UrlEncodedContent(json);
            HttpRequest request = requestFactory.buildPostRequest(requestUrl, content);
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
