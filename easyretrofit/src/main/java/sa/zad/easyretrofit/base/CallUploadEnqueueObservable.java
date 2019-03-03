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

import okhttp3.MultipartBody;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Response;
import sa.zad.easyretrofit.ProgressListener;
import sa.zad.easyretrofit.UploadRequestBody;

final public class CallUploadEnqueueObservable<T> extends CallEnqueueObservable<ProgressListener.Progress<T>> implements ProgressListener<T> {
  private ProgressListener.Progress<T> upload;

  public CallUploadEnqueueObservable(Call<Progress<T>> originalCall) {
    super(originalCall);
    final Request request = originalCall.request();
    if (request.body() instanceof MultipartBody) {
      MultipartBody multipartBody = ((MultipartBody) request.body());
      for (MultipartBody.Part part : multipartBody.parts()) {
        if (part.body() instanceof UploadRequestBody) {
          ((UploadRequestBody) part.body()).setListener(this);
        }
      }
    }
  }

  @Override
  protected void success(Response<Progress<T>> response) {
    if (response.isSuccessful()) {
      if (!(response.body() instanceof Progress))
        upload.setValue((T) response.body());
      else {
        upload.setValue(response.body().value);
      }
      observer.onNext(Response.success(upload));
    } else {
      observer.onNext(Response.error(response.code(), response.errorBody()));
    }
  }

  @Override
  public void onProgressStart(@NonNull Progress<T> upload) {
    this.upload = upload;
  }

  @Override
  public void onProgressUpdate(@NonNull Progress<T> upload) {
    this.upload = upload;
    observer.onNext(Response.success(upload));
  }

  @Override
  public void onError(@NonNull Progress<T> upload, Exception exception) {
    this.upload = upload;
  }

  @Override
  public void onFinish(@NonNull Progress<T> upload) {
    this.upload = upload;
  }
}
