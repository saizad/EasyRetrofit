package sa.zad.easyretrofit.lib.adapter;

import android.os.Environment;
import android.support.annotation.NonNull;

import java.io.File;
import java.net.URI;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import sa.zad.easyretrofit.ProgressListener;
import sa.zad.easyretrofit.base.BaseRetrofitApiCallAdapter;
import sa.zad.easyretrofit.base.CallEnqueueObservable;
import sa.zad.easyretrofit.lib.DownloadApiObservable;
import sa.zad.easyretrofit.utils.FileUtils;

public class DownloadApiCallAdapter extends BaseRetrofitApiCallAdapter<ResponseBody,
    DownloadApiObservable, Observable<Response<ProgressListener.Progress<File>>>> {

  public DownloadApiCallAdapter() {
    super(ResponseBody.class);
  }

  @NonNull
  @Override
  protected Observable<Response<ProgressListener.Progress<File>>> call(Call<ResponseBody> call) {
    PublishSubject<Response<ProgressListener.Progress<File>>> progressPublishSubject = PublishSubject.create();
    final CallEnqueueObservable<ResponseBody> downloadEnqueueObservable = new CallEnqueueObservable<>(call);

    downloadEnqueueObservable.
        subscribeOn(Schedulers.io())
        .subscribe(responseBodyResponse -> {

          if (!responseBodyResponse.isSuccessful()) {
            progressPublishSubject.onNext(Response.error(responseBodyResponse.code(), responseBodyResponse.errorBody()));
            return;
          }

          File file = new File(new URI(responseBodyResponse.raw().request().url().toString()).getPath());
          File saveTo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), file.getName());
          final long startTime = System.currentTimeMillis();
          ProgressListener.Progress<File> progress = new ProgressListener.Progress<>(responseBodyResponse.body().contentLength(), startTime);
          FileUtils.writeStreamToFile(responseBodyResponse.body().byteStream(), saveTo, written -> {
            progress.setWritten(written);
            progressPublishSubject.onNext(Response.success(progress));
          });
          progress.setValue(saveTo);
          progressPublishSubject.onNext(Response.success(progress));
          progressPublishSubject.onComplete();
        }, progressPublishSubject::onError);

    return progressPublishSubject;
  }

  @Override
  protected DownloadApiObservable get(Observable<Response<ProgressListener.Progress<File>>> callMade, Request request) {
    return new DownloadApiObservable(callMade);
  }
}
