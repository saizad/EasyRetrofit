package sa.zad.easyretrofit.lib.adapter;

import android.support.annotation.NonNull;

import java.io.File;

import io.reactivex.Observable;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Response;
import sa.zad.easyretrofit.ProgressListener;
import sa.zad.easyretrofit.base.call.CallFileDownloadEnqueue;
import sa.zad.easyretrofit.lib.FileDownloadObservable;

public class FileDownloadCallAdapter extends DownloadApiCallAdapter<File> {

  @NonNull
  @Override
  protected Observable<Response<ProgressListener.Progress<File>>> call(Call<ProgressListener.Progress<File>> call) {
    return new CallFileDownloadEnqueue(call);
  }

  @Override
  protected FileDownloadObservable get(Observable<Response<ProgressListener.Progress<File>>> callMade, Request request) {
    return new FileDownloadObservable(callMade);
  }
}
