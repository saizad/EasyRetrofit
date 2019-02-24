package sa.zad.easyretrofit.lib.adapter;

import android.support.annotation.NonNull;

import java.lang.reflect.Type;

import io.reactivex.Observable;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Response;
import sa.zad.easyretrofit.ProgressListener;
import sa.zad.easyretrofit.base.BaseRetrofitApiCallAdapter;
import sa.zad.easyretrofit.base.CallUploadEnqueueObservable;
import sa.zad.easyretrofit.lib.UploadApiObservable;

public class UploadApiCallAdapter<R> extends BaseRetrofitApiCallAdapter<ProgressListener.Progress<R>,
    UploadApiObservable<R>, Observable<Response<ProgressListener.Progress<R>>>> {

  public UploadApiCallAdapter(Type responseType) {
    super(responseType);
  }

  @NonNull
  @Override
  protected Observable<Response<ProgressListener.Progress<R>>> call(Call<ProgressListener.Progress<R>> call) {
    return new CallUploadEnqueueObservable<>(call);
  }

  @Override
  protected UploadApiObservable<R> get(Observable<Response<ProgressListener.Progress<R>>> callMade, Request request) {
    return new UploadApiObservable<>(callMade);
  }
}
