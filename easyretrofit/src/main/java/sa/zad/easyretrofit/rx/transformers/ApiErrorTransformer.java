package sa.zad.easyretrofit.rx.transformers;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import rx.functions.Action1;
import sa.zad.easyretrofit.ResponseException;

public class ApiErrorTransformer<T, E> extends BaseErrorTransformer<T, ResponseException> {

    private final Action1<E> apiError;
    private final Class<E> eClass;

    public ApiErrorTransformer(Action1<E> apiError, Class<E> eClass) {
        super(null);
        this.apiError = apiError;
        this.eClass = eClass;
    }

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream
                .onErrorResumeNext(e -> {
                    if (!(e instanceof ResponseException)) {
                        return Observable.error(e);
                    } else {
                        try {
                            final E errorBody = ((ResponseException) e).getErrorBody(eClass);
                            apiError.call(errorBody);
                            return Observable.empty();
                        } catch (Exception exception){
                            return Observable.error(e);
                        }
                    }
                });
    }
}
