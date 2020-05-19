package sa.zad.easyretrofitexample;

import androidx.annotation.NonNull;

import java.io.File;

import io.reactivex.Observable;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Response;
import sa.zad.easyretrofit.ProgressListener;
import sa.zad.easyretrofit.call.adapter.FileDownloadCallAdapter;
import sa.zad.easyretrofit.observables.FileDownloadObservable;

public class MediaStoreApiCallAdapter extends FileDownloadCallAdapter {

  @NonNull
  @Override
  protected Observable<Response<ProgressListener.Progress<File>>> call(Call<ProgressListener.Progress<File>> call) {
    return new MediaStoreFileDownloadEnqueue(call);
  }

  @Override
  protected FileDownloadObservable get(Observable<Response<ProgressListener.Progress<File>>> callMade, Request request) {
    return new MediaStoreObservable(callMade);
  }
}
