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

import android.support.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.functions.Action1;

public class CallEnqueueObservable<T> extends CallObservable<T> {

  public CallEnqueueObservable(Call<T> originalCall) {
    super(originalCall);
  }

  @Override
  protected void init(Action1<? super Response<T>> responseAction, Action1<Throwable> throwableAction)   {
    call.enqueue(new Callback<T>() {
      @Override
      public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
        responseAction.call(response);
      }

      @Override
      public void onFailure(@NonNull retrofit2.Call<T> call, @NonNull Throwable t) {
        if (call.isCanceled()) return;
        throwableAction.call(t);
      }
    });
  }
}
