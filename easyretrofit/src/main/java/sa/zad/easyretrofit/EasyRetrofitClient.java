package sa.zad.easyretrofit;

import android.app.Application;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;

public class EasyRetrofitClient {

  public static String CACHE_POLICY_HEADER = "Cache-Policy";
  private static long DEFAULT_TIMEOUT = 5 * 60 * 1000;
  private final Application mApplication;

  public EasyRetrofitClient(Application application) {
    mApplication = application;
  }

  OkHttpClient.Builder provideOkHttpClientBuilder() {
    OkHttpClient.Builder builder = new OkHttpClient.Builder();
    builder.cache(new Cache(cacheDir(), cacheSize()));
    builder.addInterceptor(getLoggingInterceptor());
    builder.addInterceptor(getCacheInterceptor());
    builder.readTimeout(readTimeoutMilliseconds(), TimeUnit.MILLISECONDS);
    builder.connectTimeout(connectTimeoutMilliseconds(), TimeUnit.MILLISECONDS);
    builder.writeTimeout(writeTimeoutMilliseconds(), TimeUnit.MILLISECONDS);
    return builderReady(builder);
  }

  protected File cacheDir() {
    return new File(mApplication.getFilesDir(), "cache_data");
  }

  protected int cacheSize() {
    return 50 * 1024 * 1024; // 50MB
  }

  private HttpLoggingInterceptor getLoggingInterceptor() {
    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    // set your desired log level
    logging.setLevel(loggingLevel());
    return logging;
  }

  private Interceptor getCacheInterceptor() {
    return chain -> {
      Request request = chain.request();

      String cachePolicyHeader = request.header(CACHE_POLICY_HEADER);

      int cachePolicy =
          cachePolicyHeader == null ? CachePolicy.SERVER_ONLY : Integer.valueOf(cachePolicyHeader);
      long stale = cacheStale(cachePolicy);
      if (CachePolicy.SERVER_ONLY == cachePolicy) {
        request = request.newBuilder()
            .header("Cache-Control", "public, max-age=0, must-revalidate")
            .build();
      } else if (CachePolicy.LOCAL_ONLY == cachePolicy) {
        request = request.newBuilder()
            .header("Cache-Control", "public, only-if-cached, max-stale=" + stale)
            .build();
      } else if (CachePolicy.LOCAL_IF_AVAILABLE == cachePolicy) {
        request = request.newBuilder()
            .header("Cache-Control", "public, max-age=0, max-stale=" + stale)
            .build();
      } else if (CachePolicy.LOCAL_IF_FRESH == cachePolicy) {
        request = request.newBuilder()
            .header("Cache-Control", "public, must-revalidate, max-age=0, max-stale=" + stale)
            .build();
      }
      return chain.proceed(request);
    };
  }

  protected long readTimeoutMilliseconds() {
    return DEFAULT_TIMEOUT;
  }

  protected long connectTimeoutMilliseconds() {
    return DEFAULT_TIMEOUT;
  }

  protected long writeTimeoutMilliseconds() {
    return DEFAULT_TIMEOUT;
  }

  protected OkHttpClient.Builder builderReady(OkHttpClient.Builder builder) {
    return builder;
  }

  protected HttpLoggingInterceptor.Level loggingLevel() {
    return BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.HEADERS : HttpLoggingInterceptor.Level.NONE;
  }

  protected long cacheStale(@CachePolicy int cachePolicy) {
    switch (cachePolicy) {
      case CachePolicy.LOCAL_IF_AVAILABLE:
        return 60 * 60 * 24 * 7;
      case CachePolicy.LOCAL_IF_FRESH:
        return 60 * 60;
      case CachePolicy.LOCAL_ONLY:
        return 60 * 60 * 24 * 7;
      case CachePolicy.SERVER_ONLY:
        break;
    }
    return 0L;
  }
}
