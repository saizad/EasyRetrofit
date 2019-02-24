package sa.zad.easyretrofit.transformers;

import android.support.annotation.CallSuper;

import io.reactivex.ObservableOperator;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class BaseOperator<T>
    implements ObservableOperator<T, T>, Observer<T> {

  protected Observer<? super T> observer;

  @Override
  @CallSuper
  public final Observer<? super T> apply(Observer<? super T> observer) {
    this.observer = observer;
    return this;
  }

  @Override
  public void onSubscribe(Disposable d) {
    observer.onSubscribe(d);
  }

  @Override
  public void onNext(T value) {
    observer.onNext(value);
  }

  @Override
  public void onError(Throwable e) {
    observer.onError(e);
  }

  @Override
  public void onComplete() {
    observer.onComplete();
  }
}
