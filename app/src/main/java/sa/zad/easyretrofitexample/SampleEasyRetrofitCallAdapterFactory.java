package sa.zad.easyretrofitexample;

import androidx.annotation.Nullable;

import java.lang.reflect.Type;

import retrofit2.CallAdapter;
import sa.zad.easyretrofit.base.EasyRetrofitCallAdapterFactory;

public class SampleEasyRetrofitCallAdapterFactory extends EasyRetrofitCallAdapterFactory {
  @Nullable
  @Override
  protected CallAdapter<?, ?> getCallAdapter(Class<?> rawType, @Nullable Type responseType, @Nullable Class<?> parameterizedType) {

    if(rawType == MediaStoreObservable.class){
      return new MediaStoreApiCallAdapter();
    }
    return super.getCallAdapter(rawType, responseType, parameterizedType);
  }
}
