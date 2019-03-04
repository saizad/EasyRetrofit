package sa.zad.easyretrofit.lib.adapter;

import android.support.annotation.NonNull;

import java.io.File;

import io.reactivex.Observable;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import sa.zad.easyretrofit.ProgressListener;
import sa.zad.easyretrofit.base.BaseRetrofitApiCallAdapter;
import sa.zad.easyretrofit.base.FileDownloadEnqueue;
import sa.zad.easyretrofit.lib.FileDownloadObservable;

public class DownloadApiCallAdapter extends BaseRetrofitApiCallAdapter<ProgressListener.Progress<File>, FileDownloadObservable, Observable<Response<ProgressListener.Progress<File>>>> {

  public DownloadApiCallAdapter() {
    super(ResponseBody.class);
  }

  @NonNull
  @Override
  protected Observable<Response<ProgressListener.Progress<File>>> call(Call<ProgressListener.Progress<File>> call) {
    return new FileDownloadEnqueue(call);
  }

  @Override
  protected FileDownloadObservable get(Observable<Response<ProgressListener.Progress<File>>> callMade, Request request) {
    return new FileDownloadObservable(callMade);
  }
}
