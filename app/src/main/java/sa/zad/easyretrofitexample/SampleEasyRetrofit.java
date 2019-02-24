package sa.zad.easyretrofitexample;

import android.app.Application;
import android.support.annotation.NonNull;

import retrofit2.Retrofit;
import sa.zad.easyretrofit.EasyRetrofit;
import sa.zad.easyretrofit.EasyRetrofitClient;
import sa.zad.easyretrofit.base.BaseEasyRetrofitCallAdapterFactory;

public class SampleEasyRetrofit extends EasyRetrofit {

  public SampleEasyRetrofit(Application application) {
    super(application);
  }

  @NonNull
  @Override
  public Retrofit.Builder retrofitBuilderReady(@NonNull Retrofit.Builder retrofitBuilder) {
    return retrofitBuilder
        .baseUrl("https://reqres.in/");
  }

  @NonNull
  @Override
  protected EasyRetrofitClient easyRetrofitClient() {
    return new SampleEasyRetrofitClient(provideApplication());
  }

  @NonNull
  @Override
  protected BaseEasyRetrofitCallAdapterFactory addCallAdapterFactory() {
    return new SampleEasyRetrofitCallAdapterFactory();
  }
}
