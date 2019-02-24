package sa.zad.easyretrofitexample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import sa.zad.easyretrofit.base.EasyRetrofitCallAdapterFactory;

public class DownloadActivity extends BaseActivity {

  private static final String TAG = "MainActivity";
  private static final String url = "http://mirror.filearena.net/pub/speed/SpeedTest_64MB.dat";
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.layout_download_observable);
    findViewById(R.id.button2).setOnClickListener(v -> {
      service.avatar(url).subscribe();
    });

    findViewById(R.id.test_download).setOnClickListener(v -> downloadZipFile());
  }

  public <T> T createService(Class<T> serviceClass, String baseUrl) {
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(new OkHttpClient.Builder()
            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
            .build())
        .addCallAdapterFactory(new EasyRetrofitCallAdapterFactory()).build();
    return retrofit.create(serviceClass);
  }

  private void downloadZipFile() {
    Service downloadService = createService(Service.class, "https://github.com/");
    downloadService.avatar(url)
        .progress(fileProgress -> {
          Log.d(TAG, fileProgress.getProgress() + "");
        })
        .subscribe();
  }
}
