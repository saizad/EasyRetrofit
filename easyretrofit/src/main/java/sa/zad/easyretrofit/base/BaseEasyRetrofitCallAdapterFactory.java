package sa.zad.easyretrofit.base;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.reactivex.Completable;
import retrofit2.CallAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.Result;

public abstract class BaseEasyRetrofitCallAdapterFactory extends CallAdapter.Factory {

  public BaseEasyRetrofitCallAdapterFactory() {
  }

  @Override
  public final CallAdapter<?, ?> get(@NonNull Type returnType, @NonNull Annotation[] annotations,
                                     @NonNull Retrofit retrofit) {
    Class<?> rawType = getRawType(returnType);

    if (rawType == Completable.class)
      return getCallAdapter(Completable.class, Void.class, Void.class);

    if (!(returnType instanceof ParameterizedType)) {
      return getCallAdapter(rawType, null, null);
    }

    Type responseType;

    Type observableType = getParameterUpperBound(0, (ParameterizedType) returnType);
    Class<?> rawObservableType = getRawType(observableType);
    if (rawObservableType == Result.class || rawObservableType == Response.class) {
      if (!(observableType instanceof ParameterizedType)) {
        throw new IllegalStateException("Result must be parameterized"
            + " as Result<Foo> or Result<? extends Foo>");
      }
      responseType = getParameterUpperBound(0, (ParameterizedType) observableType);
    } else {
      responseType = observableType;
    }

    return getCallAdapter(rawType, responseType, rawObservableType);
  }

  /**
   * @param rawType           {@code Observable<String>} <b>{@code Observable.class}</b>.
   * @param responseType      {@code Observable<String>} <b>{@code String}</b>.
   * @param parameterizedType {@code Observable<<Response<String>>} <b>{@code Response.class}</b>.
   */

  @Nullable
  protected abstract CallAdapter<?, ?> getCallAdapter(Class<?> rawType,
                                                      @Nullable Type responseType, @Nullable Class<?> parameterizedType);
}
