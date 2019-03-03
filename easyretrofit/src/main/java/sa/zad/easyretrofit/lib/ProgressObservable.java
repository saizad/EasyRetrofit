package sa.zad.easyretrofit.lib;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import retrofit2.Response;
import rx.functions.Action1;
import sa.zad.easyretrofit.ProgressListener;
import sa.zad.easyretrofit.transformers.RetrofitResponseOperator;

public class ProgressObservable<T> extends EasyObservable<T> {

  private Observable<ProgressListener.Progress<T>> progressUpstream;

  ProgressObservable(Observable<Response<ProgressListener.Progress<T>>> upstream) {
    super(upstream.takeLast(1).map(progressResponse -> Response.success(progressResponse.body().value)));
    progressUpstream = upstream
        .compose(upstream1 -> upstream1.onErrorResumeNext(Observable.empty()))
        .window(30, TimeUnit.MILLISECONDS)
        .flatMap(responseObservable -> responseObservable.takeLast(1))
        .lift(new RetrofitResponseOperator<>())
        .map(Response::body)
        .observeOn(AndroidSchedulers.mainThread());
  }

  public ProgressObservable<T> progressUpdate(Action1<ProgressListener.Progress<T>> progressAction) {
    this.progressUpstream = this.progressUpstream.doOnNext(progress -> {
      if(!progress.hasValue())
        progressAction.call(progress);
    });
    this.upstream = setUpstream();
    return this;
  }

  public ProgressObservable<T> onProgressCompleted(Action1<ProgressListener.Progress<T>> progressCompletedAction) {
    this.progressUpstream = this.progressUpstream
        .takeLast(1)
        .doOnNext(progressCompletedAction::call);
    this.upstream = setUpstream();
    return this;
  }

  public ProgressObservable<T> onProgressStart(Action1<ProgressListener.Progress<T>>
                                                   progressStartAction, Long waitFor, Long minRemaining) {
    this.progressUpstream = this.progressUpstream
        .skipWhile(progress -> {
          if((progress.elapsedTime() < waitFor || progress.timeRemaining() < minRemaining)
              && !progress.hasValue()) {
            progressStartAction.call(progress);
            return true;
          }
          return false;
        });

    this.upstream = setUpstream();
    return this;
  }

  private Observable<Response<T>> setUpstream(){
    return this.progressUpstream.map(tProgress -> Response.success(tProgress.value));
  }
}
