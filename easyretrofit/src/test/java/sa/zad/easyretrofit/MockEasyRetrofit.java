package sa.zad.easyretrofit;

import android.app.Application;
import android.support.annotation.NonNull;

import com.google.gson.Gson;

import org.mockito.Mockito;

import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Retrofit;
import sa.zad.easyretrofit.base.BaseEasyRetrofitCallAdapterFactory;

public class MockEasyRetrofit extends EasyRetrofit {

  public MockEasyRetrofit(Application application) {
    super(application);
  }

  @Override
  public Gson gson() {
    return super.gson();
  }

  @Override
  public Application provideApplication() {
    return Mockito.mock(Application.class);
  }

  @Override
  public Retrofit provideRetrofit() {
    return super.provideRetrofit();
  }

  @NonNull
  @Override
  public EasyRetrofitClient easyRetrofitClient() {
    return super.easyRetrofitClient();
  }

  @NonNull
  @Override
  public Retrofit.Builder retrofitBuilderReady(@NonNull Retrofit.Builder retrofitBuilder) {
    return super.retrofitBuilderReady(retrofitBuilder);
  }

  @NonNull
  @Override
  public Converter.Factory addConverterFactory() {
    return super.addConverterFactory();
  }

  @NonNull
  @Override
  public BaseEasyRetrofitCallAdapterFactory addCallAdapterFactory() {
    return super.addCallAdapterFactory();
  }

  @Override
  public OkHttpClient client() {
    return super.client();
  }
}
