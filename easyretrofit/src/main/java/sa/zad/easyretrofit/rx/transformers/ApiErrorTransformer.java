package sa.zad.easyretrofit.rx.transformers;

import androidx.annotation.Nullable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import rx.functions.Action1;
import sa.zad.easyretrofit.ResponseException;

public class ApiErrorTransformer<T> extends BaseErrorTransformer<T, ResponseException> {

  public ApiErrorTransformer() {
    this(null);
  }

  public ApiErrorTransformer(final @Nullable Action1<ResponseException> errorAction) {
    super(errorAction);
  }

  @Override
  public ObservableSource<T> apply(Observable<T> upstream) {
    return upstream
        .onErrorResumeNext(e -> {
          if (!(e instanceof ResponseException)) {
            return Observable.error(e);
          } else {
            callAction((ResponseException) e);
            return Observable.empty();
          }
        });
  }
}
