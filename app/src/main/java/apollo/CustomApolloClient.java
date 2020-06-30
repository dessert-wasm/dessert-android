package apollo;

import com.apollographql.apollo.ApolloClient;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import java.net.CookieManager;
import java.net.CookiePolicy;

public class CustomApolloClient {
    private static CookieManager cookieManager = new CookieManager();
    private static final String BaseUrl = "https://dev.dessert.vodka";

    public static ApolloClient getClient() {
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        OkHttpClient client = new OkHttpClient.Builder().cookieJar(new JavaNetCookieJar(cookieManager)).build();
                /*.addInterceptor(new Interceptor() {
                                    @Override
                                    public Response intercept(@NonNull Chain chain) throws IOException {
                                        return chain.proceed(chain.request().newBuilder().build());
                                    }
                                }
                )
                .build();*/
        return ApolloClient.builder().serverUrl(BaseUrl).okHttpClient(client).build();
    }
}

