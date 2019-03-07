package sa.zad.easyretrofit.observables;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.adapter.rxjava2.Result;
import rx.functions.Action1;
import sa.zad.easyretrofit.ResponseException;
import sa.zad.easyretrofit.rx.transformers.FailedResponseTransformer;
import sa.zad.easyretrofit.rx.transformers.FailedResultTransformer;
import sa.zad.easyretrofit.rx.transformers.NeverErrorTransformer;

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
   * if {@link Response} is not {@link Response#isSuccessful() successful} callback will be notified and followed by {@link Observer#onComplete() onComplete}.
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
   * if it's an {@link Result#error() error}, resultError callback will be notified and {@link Observer#onComplete() onComplete} will be followed.
   *
   * @param resultError failed result callback
   * @return {@link ResultObservable}
   */
  public ResultObservable<T> failedResult(Action1<Throwable> resultError) {
    upstream = upstream.compose(new FailedResultTransformer<>(resultError));
    return this;
  }

  /**
   * If source observable encounters any error, resultError callback will be notified and {@link Observer#onComplete() onComplete} will be followed
   * @param resultError result error callback
   * @return {@link ResultObservable}
   */

  public ResultObservable<T> neverError(Action1<Throwable> resultError) {
    upstream = upstream.compose(new NeverErrorTransformer<>(resultError));
    return this;
  }
}
