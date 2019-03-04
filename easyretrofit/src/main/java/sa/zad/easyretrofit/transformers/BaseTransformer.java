package sa.zad.easyretrofit.transformers;

import android.support.annotation.Nullable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import rx.functions.Action1;
import sa.zad.easyretrofit.utils.Utils;

public abstract class BaseTransformer<T> implements ObservableTransformer<T, T> {
  protected final @Nullable
   Action1<T> action;

  BaseTransformer() {
    this(null);
  }

  BaseTransformer(final @Nullable Action1<T> action) {
    this.action = action;
  }

  @Override
  public abstract ObservableSource<T> apply(Observable<T> upstream);

  void callAction(T call) {
    if (Utils.isNotNull(action) && Utils.isNotNull(call)) {
      action.call(call);
    }
  }
}
