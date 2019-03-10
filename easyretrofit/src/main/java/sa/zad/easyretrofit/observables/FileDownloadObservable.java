package sa.zad.easyretrofit.observables;

import java.io.File;

import io.reactivex.Observable;
import retrofit2.Response;
import sa.zad.easyretrofit.ProgressListener;
import sa.zad.easyretrofit.base.ProgressObservable;

public class FileDownloadObservable extends ProgressObservable<File> {

  public FileDownloadObservable(Observable<Response<ProgressListener.Progress<File>>> upstream) {
    super(upstream);
  }

}
