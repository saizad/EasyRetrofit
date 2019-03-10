package sa.zad.easyretrofit;


import retrofit2.Response;
import retrofit2.adapter.rxjava2.Result;
import sa.zad.easyretrofit.observables.NeverErrorObservable;

public interface NeverErrorObservableServiceTest {

  NeverErrorObservable<Result<String>> neverErrorObservableResultParameterized();

  NeverErrorObservable<Response<String>> neverErrorObservableResponseParameterized();

  NeverErrorObservable<String> neverErrorObservableBody();

  NeverErrorObservable neverErrorObservable();

}
