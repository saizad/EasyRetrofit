package sa.zad.easyretrofit.base;

import androidx.annotation.NonNull;

import java.lang.reflect.Type;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.CallAdapter;
import sa.zad.easyretrofit.call.CallObservable;

public abstract class BaseRetrofitApiCallAdapter<R, T, C> implements CallAdapter<R, T> {

  private final Type responseType;

  protected BaseRetrofitApiCallAdapter(Type responseType) {
    this.responseType = responseType;
  }

  @Override
  public Type responseType() {
    return responseType;
  }

  @Override
  public final T adapt(@NonNull Call<R> call) {
    C callMade = call(call);
    return get(callMade, call.request());
  }

  @NonNull
  protected C call(Call<R> call){
    return (C) new CallObservable<>(call);
  }

  protected abstract T get(C callMade, Request request);
}
