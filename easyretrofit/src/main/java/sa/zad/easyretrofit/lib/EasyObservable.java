package sa.zad.easyretrofit.lib;


import android.support.annotation.Nullable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import rx.functions.Action1;
import sa.zad.easyretrofit.transformers.ApiErrorTransformer;
import sa.zad.easyretrofit.transformers.NeverErrorTransformer;
import sa.zad.easyretrofit.transformers.RetrofitResponseOperator;
import sa.zad.easyretrofit.utils.ObjectUtils;

public class EasyObservable<T> extends Observable<T> {

  protected Observable<Response<T>> upstream;

  public EasyObservable(Observable<Response<T>> upstream) {
    this.upstream = upstream
        .subscribeOn(Schedulers.io())
        .lift(new RetrofitResponseOperator<>())
        .observeOn(AndroidSchedulers.mainThread());
  }

  @Override
  protected final void subscribeActual(Observer<? super T> observer) {
    upstream.filter(response -> ObjectUtils.isNotNull(response.body())).map(Response::body).subscribe(observer);
  }

  /**
   *
   * If error is encountered it will be delivered to error callback
   * and followed by {@link Observer#onComplete()  onComplete} event.
   *
   * @param error error callback
   * @return {@link #EasyObservable(Observable)}
   */
  public final EasyObservable<T> neverException(@Nullable Action1<Throwable> error) {
    this.upstream = upstream.compose(new NeverErrorTransformer<>(error));
    return this;
  }

  /**
   *
   * <p>In order to listen to Api Error call this method before
   * {@link #neverException(Action1)} if you are chaining it together.</p>
   * If api error is encountered error will be delivered to @param apiError and onComplete will be followed.
   *
   * @param apiError Api Error callback
   * @param eClass Api Error data model class
   * @param <E> Api Error Type
   * @return {@link #EasyObservable(Observable)}
   */
  public <E> EasyObservable<T> apiException(Action1<E> apiError, Class<E> eClass) {
    final ApiErrorTransformer<Response<T>> transformer =
        new ApiErrorTransformer<>(e -> apiError.call(e.getErrorBody(eClass)));
    upstream = upstream.compose(transformer);
    return this;
  }

}
