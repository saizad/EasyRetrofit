package sa.zad.easyretrofit.base;

import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.CallAdapter;
import sa.zad.easyretrofit.lib.FileDownloadObservable;
import sa.zad.easyretrofit.lib.NeverErrorObservable;
import sa.zad.easyretrofit.lib.ResultObservable;
import sa.zad.easyretrofit.lib.UploadObservable;
import sa.zad.easyretrofit.lib.adapter.DefaultCallAdapter;
import sa.zad.easyretrofit.lib.adapter.FileDownloadCallAdapter;
import sa.zad.easyretrofit.lib.adapter.NeverErrorCallAdapter;
import sa.zad.easyretrofit.lib.adapter.RetrofitResultApiCallAdapter;
import sa.zad.easyretrofit.lib.adapter.UploadCallAdapter;

public class EasyRetrofitCallAdapterFactory extends BaseEasyRetrofitCallAdapterFactory {

  @Nullable
  @Override
  @CallSuper
  protected CallAdapter<?, ?> getCallAdapter(Class<?> rawType, @Nullable  Type responseType,
                                             @Nullable Class<?> parameterizedType) {

    if(rawType == FileDownloadObservable.class)
      return new FileDownloadCallAdapter();

    if(rawType == ResultObservable.class)
      return new RetrofitResultApiCallAdapter<>(responseType);

    if(rawType == UploadObservable.class)
      return new UploadCallAdapter<>(responseType);

    if(rawType == NeverErrorObservable.class)
      return new NeverErrorCallAdapter<>(responseType);

    if(rawType == Call.class)
      return  null;

    return new DefaultCallAdapter<>(responseType, rawType, parameterizedType);
  }
}
