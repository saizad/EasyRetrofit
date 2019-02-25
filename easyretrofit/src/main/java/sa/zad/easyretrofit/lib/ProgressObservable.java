package sa.zad.easyretrofit.lib;

import android.annotation.SuppressLint;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Predicate;
import retrofit2.Response;
import rx.functions.Action1;
import rx.functions.Func1;
import sa.zad.easyretrofit.ProgressListener;
import sa.zad.easyretrofit.transformers.ProgressTransformer;
import sa.zad.easyretrofit.utils.ObjectUtils;

public class ProgressObservable<T> extends EasyObservable<ProgressListener.Progress<T>> {
  private Action1<ProgressListener.Progress<T>> progressAction;
  private Long waitMillis;
  private Func1<ProgressListener.Progress<T>, Boolean> downloadStartAction;

  @SuppressLint("CheckResult")
  public ProgressObservable(Observable<Response<ProgressListener.Progress<T>>> upstream) {
    super(upstream);
    this.upstream = upstream
        .observeOn(AndroidSchedulers.mainThread())
        .map(Response::body).skipWhile(new Predicate<ProgressListener.Progress<T>>() {
      @Override
      public boolean test(ProgressListener.Progress<T> tProgress) throws Exception {
        if(!tProgress.hasValue() && tProgress.elapsedTime() <= waitMillis && ObjectUtils.isNotNull(downloadStartAction)) {
          downloadStartAction.call(tProgress);
          return true;
        }
        return false;
      }
    }).compose(new ProgressTransformer<>(progress -> {
      if(ObjectUtils.isNotNull(progressAction)){
        progressAction.call(progress);
      }
    })).map(Response::success);
  }

  public ProgressObservable<T> progress(Action1<ProgressListener.Progress<T>> progress) {
    progressAction = progress;
    return this;
  }

  public ProgressObservable<T> onProgressStart(Func1<ProgressListener.Progress<T>, Boolean>
                                                   downloadStartAction, Long seconds) {
    this.downloadStartAction = downloadStartAction;
    this.waitMillis = seconds * 1000;
    return this;
  }
}
