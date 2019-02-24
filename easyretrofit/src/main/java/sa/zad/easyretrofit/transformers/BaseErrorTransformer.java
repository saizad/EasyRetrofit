package sa.zad.easyretrofit.transformers;

import android.support.annotation.Nullable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import rx.functions.Action1;
import sa.zad.easyretrofit.utils.ObjectUtils;

public abstract class BaseErrorTransformer<T, E extends Throwable> implements ObservableTransformer<T, T> {
  protected final @Nullable
  Action1<E> action;

  BaseErrorTransformer() {
    this(null);
  }

  BaseErrorTransformer(final @Nullable Action1<E> action) {
    this.action = action;
  }

  @Override
  public abstract ObservableSource<T> apply(Observable<T> upstream);

  void callAction(E call) {
    if (ObjectUtils.isNotNull(action) && ObjectUtils.isNotNull(call)) {
      action.call(call);
    }
  }
}
