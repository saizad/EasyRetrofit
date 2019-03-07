package sa.zad.easyretrofitexample;

import android.app.Application;

public class Sample extends Application {

  private static Sample INSTANCE;
  private Service service;
  private SampleEasyRetrofit sampleEasyRetrofit;

  public Sample(){
    INSTANCE = this;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    sampleEasyRetrofit = new SampleEasyRetrofit(this);
    service = sampleEasyRetrofit.provideRetrofit().create(Service.class);
  }

  public SampleEasyRetrofit getSampleEasyRetrofit(){
    return sampleEasyRetrofit;
  }

  public static Sample getInstance(){
    if(INSTANCE != null){
      return INSTANCE;
    }
    return new Sample();
  }

  public Service getService() {
    return service;
  }
}
