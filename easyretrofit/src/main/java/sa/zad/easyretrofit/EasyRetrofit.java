package sa.zad.easyretrofit;

import android.app.Application;
import android.support.annotation.NonNull;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sa.zad.easyretrofit.base.BaseEasyRetrofitCallAdapterFactory;
import sa.zad.easyretrofit.base.EasyRetrofitCallAdapterFactory;

public class EasyRetrofit {

  private static EasyRetrofit INSTANCE;
  private final Gson gson = new Gson();
  private Application mApplication;
  private OkHttpClient okHttpClient;

  public EasyRetrofit(Application application) {
    mApplication = application;
    INSTANCE = this;
  }

  public static EasyRetrofit getInstance() throws Exception {
    if (INSTANCE != null) {
      return INSTANCE;
    }
    throw new Exception("Class not instantiated");
  }

  public Gson gson() {
    return gson;
  }

  protected Application provideApplication() {
    return mApplication;
  }

  public Retrofit provideRetrofit() {
    okHttpClient = easyRetrofitClient().provideOkHttpClientBuilder().build();
    return retrofitBuilderReady(provideRetrofitBuilder().baseUrl("http://localhost/")).client(okHttpClient)
        .build();
  }

  @NonNull
  protected EasyRetrofitClient easyRetrofitClient() {
    return new EasyRetrofitClient(provideApplication());
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
  protected Converter.Factory addConverterFactory() {
    return GsonConverterFactory.create();
  }

  @NonNull
  protected BaseEasyRetrofitCallAdapterFactory addCallAdapterFactory() {
    return new EasyRetrofitCallAdapterFactory();
  }

  public OkHttpClient client() {
    return okHttpClient;
  }

}
