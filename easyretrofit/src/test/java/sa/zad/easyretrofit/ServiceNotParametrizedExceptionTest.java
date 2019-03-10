package sa.zad.easyretrofit;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.adapter.rxjava2.Result;
import sa.zad.easyretrofit.observables.NeverErrorObservable;
import sa.zad.easyretrofit.observables.UploadObservable;

public interface ServiceNotParametrizedExceptionTest {

  Observable<Result> observableResultNotParameterized();

  Observable<Response> observableResponseNotParameterized();

  UploadObservable<Result> uploadObservableResultNotParameterized();

  UploadObservable<Response> uploadObservableResponseNotParameterized();

  NeverErrorObservable<Result> neverErrorObservableResultNotParameterized();

  NeverErrorObservable<Response> neverErrorObservableResponseNotParameterized();

  Single<Result> singleResultNotParameterized();

  Single<Response> singleResponseNotParameterized();

  Maybe<Result> maybeResultNotParameterized();

  Maybe<Response> maybeResponseNotParameterized();

  Flowable<Result> flowableResultNotParameterized();

  Flowable<Response> flowableResponseNotParameterized();

  Call<Result> callResultNotParameterized();

  Call<Response> callResponseNotParameterized();
}
