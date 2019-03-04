package sa.zad.easyretrofit.base.call;

import android.support.annotation.CallSuper;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.plugins.RxJavaPlugins;
import retrofit2.Call;
import retrofit2.Response;
import rx.functions.Action1;

public class CallObservable<T> extends Observable<Response<T>> implements Disposable {

  private final Call<T> originalCall;
  protected Observer<? super Response<T>> observer;
  protected Call<T> call;
  private boolean terminated = false;

  public CallObservable(Call<T> originalCall) {
    this.originalCall = originalCall;
  }

  @Override
  @CallSuper
  protected final void subscribeActual(Observer<? super Response<T>> observer) {
    this.observer = observer;
    call = originalCall.clone();
    observer.onSubscribe(this);

    try {
      init(response -> {
        try {
          response(response);
        } catch (Throwable throwable) {
          throwable(throwable);
        }
      }, this::throwable);
      if (!call.isExecuted()) {
        response(call.execute());
      }
    } catch (Throwable e) {
      throwable(e);
    }
  }

  protected void init(Action1<? super Response<T>> responseAction, Action1<Throwable> throwableAction) {
  }

  private void response(Response<T> response) throws Throwable {
    success(response);
    if (!call.isCanceled()) {
      terminated = true;
      observer.onComplete();
    }
  }

  private void throwable(Throwable t) {
    if (terminated) {
      RxJavaPlugins.onError(t);
    } else if (!call.isCanceled()) {
      try {
        failed(t);
      } catch (Throwable inner) {
        Exceptions.throwIfFatal(inner);
        RxJavaPlugins.onError(new CompositeException(t, inner));
      }
    }
  }

  protected void success(Response<T> response) throws Exception {
    observer.onNext(response);
  }

  protected void failed(Throwable throwable) {
    observer.onError(throwable);
  }

  @Override
  public final void dispose() {
    call.cancel();
  }

  @Override
  public final boolean isDisposed() {
    return call.isCanceled();
  }
}
