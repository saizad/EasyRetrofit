package sa.zad.easyretrofitexample;

import java.io.File;

import io.reactivex.Observable;
import retrofit2.Response;
import sa.zad.easyretrofit.ProgressListener;
import sa.zad.easyretrofit.observables.FileDownloadObservable;

public class MediaStoreObservable extends FileDownloadObservable {

  public MediaStoreObservable(Observable<Response<ProgressListener.Progress<File>>> upstream) {
    super(upstream);
  }

}
