package sa.zad.easyretrofit.lib;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.adapter.rxjava2.Result;
import rx.functions.Action1;
import sa.zad.easyretrofit.transformers.ResultResponseTransformer;

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

  public <E> ResultObservable<T> failedResponse(Action1<E> error, Class<E> eClass) {
    final ResultResponseTransformer<T> transformer =
        new ResultResponseTransformer<>(e -> error.call(e.getErrorBody(eClass)));
    upstream = upstream.compose(transformer);
    return this;
  }
}
