package sa.zad.easyretrofit.observables;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import retrofit2.Response;
import rx.functions.Action1;
import sa.zad.easyretrofit.ProgressListener;

/**
 * This observable class provides progress updates for both uploads and downloads
 */
public class ProgressObservable<T> extends NeverErrorObservable<T> {

  public final static long DEFAULT_THROTTLE = 1000;
  public long defaultThrottle = DEFAULT_THROTTLE;
  private Observable<ProgressListener.Progress<T>> progressUpstream;

  ProgressObservable(Observable<Response<ProgressListener.Progress<T>>> upstream) {
    super(upstream.takeLast(1).map(progressResponse -> Response.success(progressResponse.body().value)));
    progressUpstream = new NeverErrorObservable<>(upstream);
  }

  /**
   * <p>set throttle for onProgressStart and progressUpdate callback</p>
   * <br/>
   * <dl>
   * <dt><b>Note:</b></dt>
   * <dd>Note any throttle applied here will affect on all progress callbacks</dd>
   * </dl>
   *
   * @param throttle set in millis
   * @return {@link ProgressObservable}
   */

  public ProgressObservable<T> applyThrottle(long throttle) {
    this.defaultThrottle = throttle;
    return this;
  }

  /**
   * @see #progressUpdate(Action1, long) for docs
   */

  public ProgressObservable<T> progressUpdate(Action1<ProgressListener.Progress<T>> progressAction) {
    return progressUpdate(progressAction, defaultThrottle);
  }

  /**
   * <p>Emits progress updates.</p>
   * <p>
   * <br/>
   * <dl>
   * <dt><b>Note:</b></dt>
   * <dd>Call this method before {@link #onProgressCompleted(Action1) onProgressCompleted}
   * and {@link #onProgressStart(Action1, Long, Long) onProgressStart} if it's chained.</dd>
   * </dl>
   *
   * @param progressAction progress update callback
   * @param throttle       throttle in millis
   * @return {@link ProgressObservable}
   */

  public ProgressObservable<T> progressUpdate(Action1<ProgressListener.Progress<T>> progressAction, long throttle) {
    this.progressUpstream = this.progressUpstream
        .throttleFirst(throttle, TimeUnit.MILLISECONDS)
        .doOnNext(progress -> {
          if (!progress.isCompleted())
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
   * <p>
   * <br/>
   * <dl>
   * <dt><b>Note:</b></dt>
   * <dd>Call this method at very last i.e. after {@link #onProgressStart(Action1, Long, Long) onProgressStart}
   * and {@link #progressUpdate(Action1) progressUpdate} if it is chained.</dd>
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
   * @see #onProgressStart(Action1, Long, Long, long)   for docs
   */

  public ProgressObservable<T> onProgressStart(Action1<ProgressListener.Progress<T>> progressStartAction) {
    return onProgressStart(progressStartAction, 0L, 0L, defaultThrottle);
  }

  /**
   * @see #onProgressStart(Action1, Long, Long, long)   for docs
   */

  public ProgressObservable<T> onProgressStart(Action1<ProgressListener.Progress<T>>
                                                   progressStartAction, Long waitFor, Long minRemaining) {
    return onProgressStart(progressStartAction, waitFor, minRemaining, defaultThrottle);
  }

  /**
   * <p>Receive progress start update for uploading/downloading</p>
   * <br/>
   * <dl>
   * <dt><b>Note:</b></dt>
   * <dd>Call this method before {@link #progressUpdate(Action1) progressUpdate} and
   * {@link #onProgressCompleted(Action1) onProgressCompleted}</dd>
   * </dl>
   *
   * @param progressStartAction progress start action callback
   * @param waitFor             receive progress start updates for millis
   * @param minRemaining        continue receiving progress start updates after waitFor period is over
   * @param throttle            throttle start updates in millis
   * @return {@link ProgressObservable}
   */


  public ProgressObservable<T> onProgressStart(Action1<ProgressListener.Progress<T>>
                                                   progressStartAction, Long waitFor, Long minRemaining, long throttle) {
    final long[] lastTime = new long[1];
    this.progressUpstream = this.progressUpstream
        .skipWhile(progress -> {
          if ((progress.elapsedTime() < waitFor || progress.estimatedTimeRemaining() < minRemaining)
              && !progress.isCompleted()) {
            if (System.currentTimeMillis() - lastTime[0] > throttle) {
              lastTime[0] = System.currentTimeMillis();
              progressStartAction.call(progress);
            }
            return true;
          }
          return false;
        });

    this.upstream = setUpstream();
    return this;
  }
}
