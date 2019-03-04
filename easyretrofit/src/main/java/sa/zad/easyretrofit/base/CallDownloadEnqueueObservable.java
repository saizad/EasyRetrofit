package sa.zad.easyretrofit.base;

import android.support.annotation.Nullable;

import okhttp3.HttpUrl;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import rx.functions.Action1;
import sa.zad.easyretrofit.ProgressListener;
import sa.zad.easyretrofit.base.call.CallEnqueueObservable;

public abstract class CallDownloadEnqueueObservable<T> extends CallEnqueueObservable<ProgressListener.Progress<T>> {

  public CallDownloadEnqueueObservable(Call<ProgressListener.Progress<T>> originalCall) {
    super(originalCall);
  }

  @Override
  protected final void success(Response<ProgressListener.Progress<T>> response) throws Exception {
      if (!response.isSuccessful()) {
        observer.onNext(Response.error(response.code(), response.errorBody()));
        return;
      }
      Response<Object> responseBodyResponse = Response.success(response.body());
      final ResponseBody responseBody = (ResponseBody) responseBodyResponse.body();
      final long startTime = System.currentTimeMillis();
      final T t = responseBodyReady(responseBody, response.raw().request().url(), written -> {
        ProgressListener.Progress<T> update = new ProgressListener.Progress<>(responseBody.contentLength(), startTime);
        update.setWritten(written);
        observer.onNext(Response.success(update));
      });
      ProgressListener.Progress<T> progress = new ProgressListener.Progress<>(responseBody.contentLength(), startTime);
      progress.setWritten(progress.size);
      progress.setValue(t);
      observer.onNext(Response.success(progress));
      observer.onComplete();
  }

  @Nullable
  protected abstract T responseBodyReady(ResponseBody responseBody, HttpUrl url, Action1<Integer> writtenCallback) throws Exception;
}
