package sa.zad.easyretrofitexample;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/***
 * EXPERIMENTAL Network interceptor for OkHttp that rewrites the mime types in the response headers
 * Turns any text/plain responses to application/json, which enables Stetho network preview
 * to display json responses in a tree rather than a string
 *
 * DO NOT use this interceptor in production builds. Only meant for ease in debugging
 *
 */
public class TextToJsonInterceptor implements Interceptor {

  @Override public Response intercept(Chain chain) throws IOException {
    Response originalResponse = chain.proceed(chain.request());
    if (!BuildConfig.DEBUG) {
      return originalResponse;
    }

    boolean intercept = originalResponse.header("Content-Type", "").contains("text/plain");
    if (intercept) {
      return originalResponse.newBuilder()
          .header("Content-Type", "application/json; charset=utf-8")
          .build();
    }

    return originalResponse;
  }
}
