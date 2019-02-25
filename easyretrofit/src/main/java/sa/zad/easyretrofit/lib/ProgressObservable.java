package sa.zad.easyretrofit.lib;

import android.annotation.SuppressLint;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import retrofit2.Response;
import rx.functions.Action1;
import sa.zad.easyretrofit.ProgressListener;
import sa.zad.easyretrofit.transformers.ProgressTransformer;
import sa.zad.easyretrofit.utils.ObjectUtils;

public class ProgressObservable<T> extends EasyObservable<ProgressListener.Progress<T>> {
  private Action1<ProgressListener.Progress<T>> progressAction;
  private Long seconds;
  private Action1<ProgressListener.Progress<T>> downloadStartAction;

  @SuppressLint("CheckResult")
  public ProgressObservable(Observable<Response<ProgressListener.Progress<T>>> upstream) {
    super(upstream);
    this.upstream = this.upstream
        .observeOn(AndroidSchedulers.mainThread())
        .map(Response::body)
        .compose(new ProgressTransformer<>(progress -> {
          if (ObjectUtils.isNotNull(downloadStartAction)) {
            downloadStartAction.call(progress);
            downloadStartAction = null;
          } else if (progressAction != null)
            progressAction.call(progress);
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

  public ProgressObservable<T> onProgressStart(Action1<ProgressListener.Progress<T>> downloadStartAction) {
    this.downloadStartAction = downloadStartAction;
    return this;
  }
}
