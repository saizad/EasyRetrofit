package sa.zad.easyretrofit.base;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import retrofit2.Response;
import rx.functions.Action1;
import sa.zad.easyretrofit.ProgressListener;
import sa.zad.easyretrofit.transformers.ProgressTransformer;

public class ProgressObservable<T> extends NeverErrorObservable<ProgressListener.Progress<T>> {
  private Action1<ProgressListener.Progress<T>> progressAction;
  private Long seconds;

  public ProgressObservable(Observable<Response<ProgressListener.Progress<T>>> upstream) {
    super(upstream);
    this.upstream = this.upstream
        .observeOn(AndroidSchedulers.mainThread())
        .map(Response::body)
        .compose(new ProgressTransformer<>(upload -> {
          if (progressAction != null)
            progressAction.call(upload);
        }))
        .map(Response::success);
  }

  public ProgressObservable<T> progress(Action1<ProgressListener.Progress<T>> progress) {
    progressAction = progress;
    return this;
  }

  public ProgressObservable<T> progress(Action1<ProgressListener.Progress<T>> progress, Long seconds) {
    progressAction = progress;
    this.seconds = seconds;
    return this;
  }
}
