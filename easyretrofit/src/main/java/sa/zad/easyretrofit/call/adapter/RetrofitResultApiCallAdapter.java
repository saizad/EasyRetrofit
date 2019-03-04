package sa.zad.easyretrofit.call.adapter;

import java.lang.reflect.Type;

import io.reactivex.Observable;
import okhttp3.Request;
import retrofit2.Response;
import sa.zad.easyretrofit.base.BaseRetrofitApiCallAdapter;
import sa.zad.easyretrofit.base.ResultObservableConverter;
import sa.zad.easyretrofit.observables.ResultObservable;

public class RetrofitResultApiCallAdapter<R> extends
    BaseRetrofitApiCallAdapter<R, ResultObservable<R>, Observable<Response<R>>> {

  public RetrofitResultApiCallAdapter(Type responseType) {
    super(responseType);
  }

  @Override
  protected ResultObservable<R> get(Observable<Response<R>> observable, Request request) {
    return new ResultObservable<>(new ResultObservableConverter<>(observable));
  }
}
