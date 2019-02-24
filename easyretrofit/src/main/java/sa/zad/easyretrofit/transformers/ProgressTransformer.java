package sa.zad.easyretrofit.transformers;

import android.support.annotation.Nullable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import rx.functions.Action1;
import sa.zad.easyretrofit.ProgressListener;

public class ProgressTransformer<T> extends BaseTransformer<ProgressListener.Progress<T>> {

  public ProgressTransformer(@Nullable Action1<ProgressListener.Progress<T>> action) {
    super(action);
  }

  @Override
  public ObservableSource<ProgressListener.Progress<T>> apply(Observable<ProgressListener.Progress<T>> upstream) {
    return upstream
        .filter(progress -> {
          if (!progress.hasValue()) {
            callAction(progress);
            return false;
          }
          return true;
        });
  }

}
