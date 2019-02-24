package sa.zad.easyretrofit.base;

import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.CallAdapter;
import sa.zad.easyretrofit.lib.DownloadApiObservable;
import sa.zad.easyretrofit.lib.ResultObservable;
import sa.zad.easyretrofit.lib.UploadApiObservable;
import sa.zad.easyretrofit.lib.adapter.DefaultCallAdapter;
import sa.zad.easyretrofit.lib.adapter.DownloadApiCallAdapter;
import sa.zad.easyretrofit.lib.adapter.RetrofitResponseApiCallAdapter;
import sa.zad.easyretrofit.lib.adapter.RetrofitResultApiCallAdapter;
import sa.zad.easyretrofit.lib.adapter.UploadApiCallAdapter;

public class EasyRetrofitCallAdapterFactory extends BaseEasyRetrofitCallAdapterFactory {

  @Nullable
  @Override
  @CallSuper
  protected CallAdapter<?, ?> getCallAdapter(Class<?> rawType, @Nullable  Type responseType,
                                             @Nullable Class<?> parameterizedType) {

    if(rawType == DownloadApiObservable.class)
      return new DownloadApiCallAdapter();

    if(rawType == ResultObservable.class)
      return new RetrofitResultApiCallAdapter<>(responseType);

    if(rawType == UploadApiObservable.class)
      return new UploadApiCallAdapter<>(responseType);

    if(rawType == NeverErrorObservable.class)
      return new RetrofitResponseApiCallAdapter<>(responseType);

    if(rawType == Call.class)
      return  null;

    return new DefaultCallAdapter<>(responseType, rawType, parameterizedType);
  }
}
