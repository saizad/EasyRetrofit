package sa.zad.easyretrofit.call.adapter;

import androidx.annotation.NonNull;

import java.lang.reflect.Type;

import io.reactivex.Observable;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Response;
import sa.zad.easyretrofit.ProgressListener;
import sa.zad.easyretrofit.base.BaseRetrofitApiCallAdapter;
import sa.zad.easyretrofit.call.CallUploadEnqueueObservable;
import sa.zad.easyretrofit.observables.UploadObservable;

public class UploadCallAdapter<R> extends BaseRetrofitApiCallAdapter<ProgressListener.Progress<R>,
    UploadObservable<R>, Observable<Response<ProgressListener.Progress<R>>>> {

  public UploadCallAdapter(Type responseType) {
    super(responseType);
  }

  @NonNull
  @Override
  protected Observable<Response<ProgressListener.Progress<R>>> call(Call<ProgressListener.Progress<R>> call) {
    return new CallUploadEnqueueObservable<>(call);
  }

  @Override
  protected UploadObservable<R> get(Observable<Response<ProgressListener.Progress<R>>> callMade, Request request) {
    return new UploadObservable<>(callMade);
  }
}
