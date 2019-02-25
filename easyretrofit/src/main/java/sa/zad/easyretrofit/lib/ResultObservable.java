package sa.zad.easyretrofit.lib;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.adapter.rxjava2.Result;
import rx.functions.Action1;
import sa.zad.easyretrofit.ResponseException;
import sa.zad.easyretrofit.transformers.FailedResponseTransformer;
import sa.zad.easyretrofit.transformers.FailedResultTransformer;
import sa.zad.easyretrofit.transformers.NeverErrorTransformer;

public class ResultObservable<T> extends Observable<Result<T>> {

  private Observable<Result<T>> upstream;

  public ResultObservable(Observable<Result<T>> upstream) {
    this.upstream = upstream
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
  }

  @Override
  protected void subscribeActual(Observer<? super Result<T>> observer) {
    upstream.subscribe(observer);
  }

  /**
   * if not {@link Response#isSuccessful()} callback will be triggered and followed by {@link Observer#onComplete()}.
   *
   * @param error failed response callback
   * @return {@link ResultObservable}
   */

  public ResultObservable<T> failedResponse(Action1<ResponseException> error) {
    final FailedResponseTransformer<T> transformer =
        new FailedResponseTransformer<>(error);
    upstream = upstream.compose(transformer);
    return this;
  }

  /**
   * if it's {@link Result#error()} @param resultError callback will be triggered and {@link Observer#onComplete()} will be followed.
   *
   * @param resultError failed result callback
   * @return {@link ResultObservable}
   */
  public ResultObservable<T> failedResult(Action1<Throwable> resultError) {
    upstream = upstream.compose(new FailedResultTransformer<>(resultError));
    return this;
  }

  public ResultObservable<T> neverError(Action1<Throwable> resultError) {
    upstream = upstream.compose(new NeverErrorTransformer<>(resultError));
    return this;
  }
}
