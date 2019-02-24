package sa.zad.easyretrofit;

import android.app.Application;
import android.support.annotation.NonNull;

import com.google.gson.Gson;

import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sa.zad.easyretrofit.base.BaseEasyRetrofitCallAdapterFactory;
import sa.zad.easyretrofit.base.EasyRetrofitCallAdapterFactory;

public class EasyRetrofit {

  private static EasyRetrofit INSTANCE;
  private final Gson gson = new Gson();
  private Application mApplication;

  public EasyRetrofit() {
  }

  public EasyRetrofit(Application application) {
    mApplication = application;
    INSTANCE = this;
  }

  public static EasyRetrofit getInstance() {
    if (INSTANCE != null) {
      return INSTANCE;
    }
    return new EasyRetrofit();
  }

  public Gson gson() {
    return gson;
  }

  protected Application provideApplication() {
    return mApplication;
  }

  public Retrofit provideRetrofit() {
    return retrofitBuilderReady(provideRetrofitBuilder()).client(easyRetrofitClient()
        .provideOkHttpClientBuilder().build())
        .build();
  }

  @NonNull
  public Retrofit.Builder retrofitBuilderReady(@NonNull Retrofit.Builder retrofitBuilder) {
    return retrofitBuilder;
  }

  private Retrofit.Builder provideRetrofitBuilder() {
    return new Retrofit.Builder().addConverterFactory(addConverterFactory())
        .addCallAdapterFactory(addCallAdapterFactory());
  }

  @NonNull
  protected EasyRetrofitClient easyRetrofitClient() {
    return new EasyRetrofitClient(mApplication);
  }

  @NonNull
  protected Converter.Factory addConverterFactory() {
    return GsonConverterFactory.create();
  }

  @NonNull
  protected BaseEasyRetrofitCallAdapterFactory addCallAdapterFactory() {
    return new EasyRetrofitCallAdapterFactory();
  }

}
