package sa.zad.easyretrofit.lib.adapter;

import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import sa.zad.easyretrofit.ProgressListener;
import sa.zad.easyretrofit.base.BaseRetrofitApiCallAdapter;
import sa.zad.easyretrofit.base.CallEnqueueObservable;
import sa.zad.easyretrofit.lib.DownloadApiObservable;

public class DownloadApiCallAdapter extends BaseRetrofitApiCallAdapter<ResponseBody,
    DownloadApiObservable, Observable<Response<ProgressListener.Progress<File>>>> {

  private static final String TAG = "Download";

  public DownloadApiCallAdapter() {
    super(ResponseBody.class);
  }

  @NonNull
  @Override
  protected Observable<Response<ProgressListener.Progress<File>>> call(Call<ResponseBody> call) {
    PublishSubject<Response<ProgressListener.Progress<File>>> progressPublishSubject = PublishSubject.create();
    final CallEnqueueObservable<ResponseBody> downloadEnqueueObservable = new CallEnqueueObservable<>(call);
    final Disposable subscribe = downloadEnqueueObservable.subscribe(responseBodyResponse -> {
      if (!responseBodyResponse.isSuccessful()) {
        progressPublishSubject.onNext(Response.error(responseBodyResponse.code(), responseBodyResponse.errorBody()));
        return;
      }
      final File file = new File(new URI(responseBodyResponse.raw().request().url().toString()).getPath());
      final File saveTo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), file.getName());
      AsyncTask.execute(() -> saveToDisk(responseBodyResponse.body(),
          saveTo, new ProgressListener<File>() {
            @Override
            public void onProgressStart(@NonNull Progress<File> progress) {

            }

            @Override
            public void onProgressUpdate(@NonNull Progress<File> progress) {
              //TODO overflowing onNext, downstream is not able to keep it up.
              progressPublishSubject.onNext(Response.success(progress));
            }

            @Override
            public void onError(@NonNull Progress<File> progress, Exception exception) {
              progressPublishSubject.onError(exception);
            }

            @Override
            public void onFinish(@NonNull Progress<File> progress) {
              progressPublishSubject.onNext(Response.success(progress));
              progressPublishSubject.onComplete();
            }
          }));
    }, progressPublishSubject::onError);
    return progressPublishSubject;
  }

  @Override
  protected DownloadApiObservable get(Observable<Response<ProgressListener.Progress<File>>> callMade, Request request) {
    return new DownloadApiObservable(callMade);
  }

  @CallSuper
  protected void saveToDisk(ResponseBody body, File destinationFile, ProgressListener<File> progressListener) {
    try {

      InputStream inputStream = null;
      OutputStream outputStream = null;

      try {

        inputStream = body.byteStream();
        outputStream = new FileOutputStream(destinationFile);
        byte data[] = new byte[4096];
        int count;
        int written = 0;
        Long startTime = System.currentTimeMillis();
        Log.d("adsfadf", "Started");
        while ((count = inputStream.read(data)) != -1) {
          outputStream.write(data, 0, count);
          Log.d("adsfadf", "Started " + count);
          written += count;
          ProgressListener.Progress<File> downloadProgress = new ProgressListener.Progress<>(body.contentLength(), startTime);
          downloadProgress.setWritten(written);
          progressListener.onProgressUpdate(downloadProgress);
          Log.d("adsfadf", "Started progress " + downloadProgress.getProgress());
          Log.d("adsfadf", "Started totalDuration " + downloadProgress.totalDuration());
          Log.d("adsfadf", "Started timeRemaining " + downloadProgress.timeRemaining());
        }

        outputStream.flush();
        ProgressListener.Progress<File> downloadProgress = new ProgressListener.Progress<>(body.contentLength(), startTime);
        downloadProgress.setValue(destinationFile);
        progressListener.onFinish(downloadProgress);
        Log.d(TAG, destinationFile.getParent());
      } catch (IOException e) {
        e.printStackTrace();
        progressListener.onError(new ProgressListener.Progress<>(0), e);
      } finally {
        if (inputStream != null) inputStream.close();
        if (outputStream != null) outputStream.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
      progressListener.onError(new ProgressListener.Progress<>(0), e);
    }
  }
}
