package sa.zad.easyretrofit.lib.adapter;

import android.support.annotation.NonNull;

import java.lang.reflect.Type;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.adapter.rxjava2.Result;
import sa.zad.easyretrofit.base.BaseRetrofitApiCallAdapter;
import sa.zad.easyretrofit.base.CallEnqueueObservable;
import sa.zad.easyretrofit.base.ResultObservableConverter;

public class DefaultCallAdapter<R> extends
    BaseRetrofitApiCallAdapter<R, Object, Object> {

  private final Class<?> rawType;
  private final Class<?> parameterizedType;

  public DefaultCallAdapter(Type responseType, Class<?> rawType, Class<?> parameterizedType) {
    super(responseType);
    this.rawType = rawType;
    this.parameterizedType = parameterizedType;
  }

  @NonNull
  @Override
  protected Object call(Call<R> call) {
    Observable<Response<R>> responseObservable = new CallEnqueueObservable<>(call);

    Observable<?> observable;
    if (parameterizedType == Result.class) {
      observable = new ResultObservableConverter<>(responseObservable);
    } else if (parameterizedType == Response.class) {
      observable = responseObservable;
    } else if(rawType == Completable.class) {
      return responseObservable.ignoreElements();
    }else{
      observable = responseObservable.map(Response::body);
    }

    if (rawType == Flowable.class)
      return observable.toFlowable(BackpressureStrategy.LATEST);

    if (rawType == Single.class)
      return observable.singleOrError();

    if (rawType == Maybe.class)
      return observable.singleElement();

    return observable;
  }

  @Override
  protected Object get(Object callMade, Request request) {
    return callMade;
  }
}
