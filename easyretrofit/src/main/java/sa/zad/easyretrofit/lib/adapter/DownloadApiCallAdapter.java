package sa.zad.easyretrofit.lib.adapter;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import sa.zad.easyretrofit.ProgressListener;
import sa.zad.easyretrofit.base.BaseRetrofitApiCallAdapter;

public abstract class DownloadApiCallAdapter<V> extends BaseRetrofitApiCallAdapter<ProgressListener.Progress<V>, Observable, Observable<Response<ProgressListener.Progress<V>>>> {

  public DownloadApiCallAdapter() {
    super(ResponseBody.class);
  }

}
