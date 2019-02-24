package sa.zad.easyretrofit.transformers;

import android.support.annotation.Nullable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import retrofit2.adapter.rxjava2.Result;
import rx.functions.Action1;
import sa.zad.easyretrofit.ResponseException;

public class ResultResponseTransformer<T> extends BaseErrorTransformer<Result<T>, ResponseException> {

  public ResultResponseTransformer(@Nullable Action1<ResponseException> action) {
    super(action);
  }

  @Override
  public ObservableSource<Result<T>> apply(Observable<Result<T>> upstream) {
    return upstream
        .flatMap(tResult -> {
          if (!tResult.isError() && !tResult.response().isSuccessful()) {
            callAction(new ResponseException(tResult.response()));
            return Observable.empty();
          }
          return Observable.just(tResult);
        });
  }
}
