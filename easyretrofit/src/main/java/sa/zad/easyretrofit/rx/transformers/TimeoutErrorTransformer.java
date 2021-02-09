package sa.zad.easyretrofit.rx.transformers;

import java.net.SocketTimeoutException;

import androidx.annotation.Nullable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import rx.functions.Action1;

public class TimeoutErrorTransformer<T> extends BaseErrorTransformer<T, SocketTimeoutException> {

    public TimeoutErrorTransformer(@Nullable Action1<SocketTimeoutException> errorAction) {
        super(errorAction);
    }

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream
                .onErrorResumeNext(e -> {
                    if (!(e instanceof SocketTimeoutException)) {
                        return Observable.error(e);
                    } else {
                        callAction(new SocketTimeoutException("Request time out"));
                        return Observable.empty();
                    }
                });
    }
}
