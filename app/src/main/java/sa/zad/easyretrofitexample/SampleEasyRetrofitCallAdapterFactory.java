package sa.zad.easyretrofitexample;

import android.support.annotation.Nullable;

import java.lang.reflect.Type;

import retrofit2.CallAdapter;
import sa.zad.easyretrofit.base.EasyRetrofitCallAdapterFactory;
import sa.zad.easyretrofit.observables.FileDownloadObservable;

public class SampleEasyRetrofitCallAdapterFactory extends EasyRetrofitCallAdapterFactory {
  @Nullable
  @Override
  protected CallAdapter<?, ?> getCallAdapter(Class<?> rawType, @Nullable Type responseType, @Nullable Class<?> parameterizedType) {
    if(rawType == FileDownloadObservable.class){
      return new SampleDownloadApiCallAdapter();
    }
    return super.getCallAdapter(rawType, responseType, parameterizedType);
  }
}
