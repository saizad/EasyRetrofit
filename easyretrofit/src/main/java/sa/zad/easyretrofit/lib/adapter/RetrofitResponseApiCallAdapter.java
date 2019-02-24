package sa.zad.easyretrofit.lib.adapter;

import java.lang.reflect.Type;

import io.reactivex.Observable;
import okhttp3.Request;
import retrofit2.Response;
import sa.zad.easyretrofit.base.BaseRetrofitApiCallAdapter;
import sa.zad.easyretrofit.base.NeverErrorObservable;

public class RetrofitResponseApiCallAdapter<R> extends BaseRetrofitApiCallAdapter<R, NeverErrorObservable<R>, Observable<Response<R>>> {

  public RetrofitResponseApiCallAdapter(Type responseType) {
    super(responseType);
  }

  @Override
  protected NeverErrorObservable<R> get(Observable<Response<R>> observable, Request request) {
    return new NeverErrorObservable<>(observable);
  }
}