package digimatic.shangcommerce.connection;

import com.google.api.client.auth.oauth.OAuthHmacSigner;
import com.google.api.client.auth.oauth.OAuthParameters;

/**
 * Created by USER on 3/11/2016.
 */
public class Constant {

    public static String BASE_URL = "http://shang-dev.techub.io/";
//    public static String BASE_URL = "http://shang.southeastasia.cloudapp.azure.com/";

    public static String ABOUT_PAGE = BASE_URL + "webapp/page/about";
    public static String TERMS_PAGE = BASE_URL + "webapp/page/terms";
    public static String FAQ_PAGE = BASE_URL + "webapp/page/faq";
    public static String ABOUT_APP = BASE_URL + "webapp/page/platform";

    public static OAuthParameters getOAuthParam() {
        OAuthHmacSigner signer = new OAuthHmacSigner();
        signer.clientSharedSecret = "41255f713848c102d444e2fd13b90b13";
        signer.tokenSharedSecret = "b88b982da1d768221db2ea22b8eb9b9a";
        OAuthParameters authorizer = new OAuthParameters();
        authorizer.consumerKey = "015fcdf594efef1dd72847f5fba1e326";
        authorizer.signer = signer;
        authorizer.token = "ad9581707d661f81acc379aa763e4491";

        return authorizer;
    }
}
