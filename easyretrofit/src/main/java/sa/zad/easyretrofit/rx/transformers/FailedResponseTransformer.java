package sa.zad.easyretrofit.rx.transformers;

import androidx.annotation.Nullable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import retrofit2.adapter.rxjava2.Result;
import rx.functions.Action1;
import sa.zad.easyretrofit.ResponseException;

public class FailedResponseTransformer<T> extends BaseErrorTransformer<Result<T>, ResponseException> {

    public FailedResponseTransformer(@Nullable Action1<ResponseException> action) {
        super(action);
    }

    @Override
    public ObservableSource<Result<T>> apply(Observable<Result<T>> upstream) {
        return upstream
                .flatMap(tResult -> {
                    if (!tResult.isError() && !tResult.response().isSuccessful()) {
                        callAction(new ResponseException(tResult.response()));
                        return Observable.empty();
                    }
                    return Observable.just(tResult);
                });
    }
}
