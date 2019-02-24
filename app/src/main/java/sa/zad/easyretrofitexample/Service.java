package sa.zad.easyretrofitexample;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;
import sa.zad.easyretrofit.base.NeverErrorObservable;
import sa.zad.easyretrofit.lib.DownloadApiObservable;
import sa.zad.easyretrofitexample.model.DataModel;
import sa.zad.easyretrofitexample.model.Register;
import sa.zad.easyretrofitexample.model.RegisterBody;
import sa.zad.easyretrofitexample.model.User;

interface Service {

  @POST("api/register/")
  NeverErrorObservable<Register> register(@Body RegisterBody registerBody);

  @GET("api/users/{user}")
  NeverErrorObservable<DataModel<User>> users(@Path(value = "user", encoded = true) Integer userId);

  @GET
  DownloadApiObservable avatar(@Url String url);

  @GET("/api/unknown/23")
  NeverErrorObservable<DataModel<User>> notFound();

}
