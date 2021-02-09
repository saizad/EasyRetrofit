package sa.zad.easyretrofitexample;

import android.app.Application;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import okhttp3.OkHttpClient;
import sa.zad.easyretrofit.EasyRetrofitClient;

public class SampleEasyRetrofitClient extends EasyRetrofitClient {

  public SampleEasyRetrofitClient(Application application) {
    super(application);
  }

  @Override
  protected OkHttpClient.Builder builderReady(OkHttpClient.Builder builder) {
    if (BuildConfig.DEBUG) {
      builder.addNetworkInterceptor(new StethoInterceptor());
      builder.addNetworkInterceptor(new TextToJsonInterceptor());
    }
    return builder;
  }

  @Override
  protected long connectTimeoutMilliseconds() {
    return 10000;
  }

  @Override
  protected long readTimeoutMilliseconds() {
    return 10000;
  }

  @Override
  protected long writeTimeoutMilliseconds() {
    return 10000;
  }
}
