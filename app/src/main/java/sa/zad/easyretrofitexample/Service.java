package sa.zad.easyretrofitexample;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import sa.zad.easyretrofit.lib.EasyObservable;
import sa.zad.easyretrofit.lib.DownloadApiObservable;
import sa.zad.easyretrofit.lib.ResultObservable;
import sa.zad.easyretrofitexample.model.DataModel;
import sa.zad.easyretrofitexample.model.Register;
import sa.zad.easyretrofitexample.model.RegisterBody;
import sa.zad.easyretrofitexample.model.User;

interface Service {

  @POST("api/register/")
  EasyObservable<Register> register(@Body RegisterBody registerBody);

  @GET("api/users/{user}")
  ResultObservable<DataModel<User>> users(@Path(value = "user", encoded = true) Integer userId);

  @GET
  @Streaming
  DownloadApiObservable download(@Url String url);

  @GET("/api/unknown/23")
  ResultObservable<DataModel<User>> notFound();

}
