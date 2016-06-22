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
 * Created by USER on 04/13/2016.
 */
public class RewardApi {

    private String URL = Constant.BASE_URL + "api/rest/extend/reward";
    private HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private int TIME_OUT = 60 * 1000;

    public void getListReward(ApiCallback callback, long customer_id) {
        try {
            GenericUrl requestUrl = new GenericUrl(URL + "?customer_id=" + customer_id);

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
}
