package sa.zad.easyretrofit.transformers;

import android.support.annotation.Nullable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import rx.functions.Action1;

public class NeverErrorTransformer<T> extends BaseErrorTransformer<T, Throwable> {

  public NeverErrorTransformer() {
  }

  public NeverErrorTransformer(@Nullable Action1<Throwable> errorAction) {
    super(errorAction);
  }

  @Override
  public ObservableSource<T> apply(Observable<T> upstream) {
    return upstream.doOnError(this::callAction).onErrorResumeNext(Observable.empty());
  }

}
