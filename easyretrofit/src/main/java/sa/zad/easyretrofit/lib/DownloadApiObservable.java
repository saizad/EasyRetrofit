package sa.zad.easyretrofit.lib;

import java.io.File;

import io.reactivex.Observable;
import retrofit2.Response;
import sa.zad.easyretrofit.ProgressListener;
import sa.zad.easyretrofit.base.ProgressObservable;

public class DownloadApiObservable extends ProgressObservable<File> {

  public DownloadApiObservable(Observable<Response<ProgressListener.Progress<File>>> upstream) {
    super(upstream);
  }
}
