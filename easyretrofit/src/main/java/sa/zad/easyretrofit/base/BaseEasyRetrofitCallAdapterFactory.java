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

  public static Type upperBound(Type returnTypeUpperBound) {
    return getParameterUpperBound(0, (ParameterizedType) returnTypeUpperBound);
  }

  @Override
  public final CallAdapter<?, ?> get(@NonNull Type returnType, @NonNull Annotation[] annotations,
                                     @NonNull Retrofit retrofit) {
    Class<?> rawType = getRawType(returnType);

    if (rawType == Completable.class)
      return getCallAdapter(Completable.class, Void.class, Void.class);

    //if any return type observable is not ParameterizedType
    // i.e. Observable = true and Observable<?> = false
    if (!(returnType instanceof ParameterizedType)) {
      return getCallAdapter(rawType, null, null);
    }

    Type responseType;

    Type returnTypeUpperBound = upperBound(returnType);
    Class<?> parameterizedType = getRawType(returnTypeUpperBound);
    if (parameterizedType == Result.class || parameterizedType == Response.class) {
      if (!(returnTypeUpperBound instanceof ParameterizedType)) {
        throw new IllegalStateException("Result must be parameterized"
            + " as " + returnTypeUpperBound + "<Foo>> or " + returnTypeUpperBound + "<? extends Foo>");
      }
      responseType = upperBound(returnTypeUpperBound);
    } else {
      responseType = returnTypeUpperBound;
    }

    return getCallAdapter(rawType, responseType, parameterizedType);
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
