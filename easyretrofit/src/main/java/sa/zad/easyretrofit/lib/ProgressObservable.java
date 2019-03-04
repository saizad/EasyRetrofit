package sa.zad.easyretrofit.lib;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.plugins.RxJavaPlugins;
import retrofit2.Response;
import rx.functions.Action1;
import sa.zad.easyretrofit.ProgressListener;

/**
 *
 * This observable class provides progress updates for both uploads and downloads
 */
public class ProgressObservable<T> extends NeverErrorObservable<T> {

  private Observable<ProgressListener.Progress<T>> progressUpstream;

  ProgressObservable(Observable<Response<ProgressListener.Progress<T>>> upstream) {
    super(upstream.takeLast(1).map(progressResponse -> Response.success(progressResponse.body().value)));

    final Observable<Response<ProgressListener.Progress<T>>> windowUpstream = upstream
        .window(30, TimeUnit.MILLISECONDS)
        .flatMap(responseObservable -> responseObservable.takeLast(1));

    progressUpstream = new NeverErrorObservable<>(windowUpstream);

    /*
      Ignores any errors on {@link ProgressObservable}
      Todo temp fixed, there might be unknown side affects
     */
    RxJavaPlugins.setErrorHandler(throwable -> { });
  }

  /**
   *
   * <p>Emits progress updates.</p>
   *
   * <br/>
   * <dl>
   *  <dt><b>Note:</b></dt>
   *  <dd>Call this method before {@link #onProgressCompleted(Action1) onProgressCompleted}
   *  and {@link #onProgressStart(Action1, Long, Long) onProgressStart} if it's chained.</dd>
   * </dl>
   *
   * @param progressAction progress update callback
   * @return {@link ProgressObservable}
   */

  public ProgressObservable<T> progressUpdate(Action1<ProgressListener.Progress<T>> progressAction) {
    this.progressUpstream = this.progressUpstream.doOnNext(progress -> {
      if (!progress.hasValue())
        progressAction.call(progress);
    });
    this.upstream = setUpstream();
    return this;
  }

  private Observable<Response<T>> setUpstream() {
    return this.progressUpstream.map(tProgress -> Response.success(tProgress.value));
  }

  /**
   * <p>Emits progress completed. If error is encountered during progress,
   * progressCompletedAction will not receive call back.</p>
   *
   * <br/>
   * <dl>
   *  <dt><b>Note:</b></dt>
   *  <dd>Call this method at very last i.e. after {@link #onProgressStart(Action1, Long, Long) onProgressStart}
   *    and {@link #progressUpdate(Action1) progressUpdate} if it is chained.</dd>
   * </dl>
   *
   * @param progressCompletedAction callback
   * @return {@link ProgressObservable}
   */

  public ProgressObservable<T> onProgressCompleted(Action1<ProgressListener.Progress<T>> progressCompletedAction) {
    this.progressUpstream = this.progressUpstream
        .takeLast(1)
        .doOnNext(progressCompletedAction::call);
    this.upstream = setUpstream();
    return this;
  }

  /**
   *
   *<p>Receive progress start update for uploading/downloading</p>
   *<br/>
   * <dl>
   *   <dt><b>Note:</b></dt>
   *   <dd>Call this method before {@link #progressUpdate(Action1) progressUpdate} and
   *    {@link #onProgressCompleted(Action1) onProgressCompleted}</dd>
   * </dl>
   *
   * @param progressStartAction progress action listener
   * @param waitFor receive progress updates for millis
   * @param minRemaining continue receiving progress updates after waitFor period is over
   * @return {@link ProgressObservable}
   */

  public ProgressObservable<T> onProgressStart(Action1<ProgressListener.Progress<T>>
                                                   progressStartAction, Long waitFor, Long minRemaining) {
    this.progressUpstream = this.progressUpstream
        .skipWhile(progress -> {
          if ((progress.elapsedTime() < waitFor || progress.timeRemaining() < minRemaining)
              && !progress.hasValue()) {
            progressStartAction.call(progress);
            return true;
          }
          return false;
        });

    this.upstream = setUpstream();
    return this;
  }
}
