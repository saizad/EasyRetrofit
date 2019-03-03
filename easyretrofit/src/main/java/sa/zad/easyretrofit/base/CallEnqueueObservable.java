/*
 * Copyright (C) 2016 Jake Wharton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sa.zad.easyretrofit.base;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.plugins.RxJavaPlugins;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallEnqueueObservable<T> extends Observable<Response<T>> implements Disposable, Callback<T>{
  private final Call<T> originalCall;
  private Call<T> call;
  protected Observer<? super Response<T>> observer;
  private boolean terminated = false;

  public CallEnqueueObservable(Call<T> originalCall) {
    this.originalCall = originalCall;
  }

  @Override protected void subscribeActual(Observer<? super Response<T>> observer) {
    // Since Call is a one-shot type, clone it for each new observer.
    this.observer = observer;
    call = originalCall.clone();
    observer.onSubscribe(this);
    call.enqueue(this);
  }


  @Override public final void onResponse(Call<T> call, Response<T> response) {
    if (call.isCanceled()) return;

    try {
      success(response);

      if (!call.isCanceled()) {
        terminated = true;
        observer.onComplete();
      }
    } catch (Throwable t) {
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
  }

  protected void failed(Throwable throwable){
    observer.onError(throwable);
  }

  protected void success(Response<T> response) throws Exception{
    observer.onNext(response);
  }

  @Override public final void onFailure(Call<T> call, Throwable t) {
    if (call.isCanceled()) return;

    try {
      failed(t);
    } catch (Throwable inner) {
      Exceptions.throwIfFatal(inner);
      RxJavaPlugins.onError(new CompositeException(t, inner));
    }
  }

  @Override public final void dispose() {
    call.cancel();
  }

  @Override public final boolean isDisposed() {
    return call.isCanceled();
  }

}
