package sa.zad.easyretrofitexample;

import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import sa.zad.easyretrofit.lib.DownloadApiObservable;
import sa.zad.easyretrofit.lib.EasyObservable;
import sa.zad.easyretrofit.lib.ResultObservable;
import sa.zad.easyretrofit.lib.UploadApiObservable;
import sa.zad.easyretrofitexample.model.DataModel;
import sa.zad.easyretrofitexample.model.MediaModel;
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


  @Multipart
  @POST
  UploadApiObservable<Void> upload(@Url String url, @Part MultipartBody.Part file);

  @Headers({"auth-token: 8d7b80ea-366d-498e-bdfa-5da53f93db5d"})
  @Multipart
  @POST
  UploadApiObservable<DataModel<MediaModel>> uploadMedia(@Part MultipartBody.Part image, @Url String url);
}
