package sa.zad.easyretrofit.rx.transformers;

import androidx.annotation.Nullable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import retrofit2.adapter.rxjava2.Result;
import rx.functions.Action1;

public class FailedResultTransformer<T> extends BaseErrorTransformer<Result<T>, Throwable> {

  public FailedResultTransformer(@Nullable Action1<Throwable> action) {
    super(action);
  }

  @Override
  public ObservableSource<Result<T>> apply(Observable<Result<T>> upstream) {
    return upstream
        .flatMap(tResult -> {
          if (tResult.isError()) {
            callAction(tResult.error());
            return Observable.empty();
          }
          return Observable.just(tResult);
        });
  }
}
