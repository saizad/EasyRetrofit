package sa.zad.easyretrofitexample;

import android.app.Application;

public class Sample extends Application {

  private static Sample INSTANCE;
  private Service service;

  public Sample(){
    INSTANCE = this;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    service = new SampleEasyRetrofit(this).provideRetrofit().create(Service.class);
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
