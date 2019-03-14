package sa.zad.easyretrofit;


import retrofit2.Response;
import retrofit2.adapter.rxjava2.Result;
import retrofit2.http.GET;
import retrofit2.http.Url;
import sa.zad.easyretrofit.observables.NeverErrorObservable;

public interface NeverErrorObservableServiceTest {

  @GET
  NeverErrorObservable<Result<String>> neverErrorObservableResultParameterized();

  @GET
  NeverErrorObservable<Response<String>> neverErrorObservableResponseParameterized();

  @GET
  NeverErrorObservable<String> neverErrorObservableBody(@Url String url);

  @GET
  NeverErrorObservable neverErrorObservable();

}
