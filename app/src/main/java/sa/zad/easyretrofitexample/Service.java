package sa.zad.easyretrofitexample;

import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import sa.zad.easyretrofit.CachePolicy;
import sa.zad.easyretrofit.observables.FileDownloadObservable;
import sa.zad.easyretrofit.observables.NeverErrorObservable;
import sa.zad.easyretrofit.observables.ResultObservable;
import sa.zad.easyretrofit.observables.UploadObservable;
import sa.zad.easyretrofitexample.model.DataModel;
import sa.zad.easyretrofitexample.model.RegisterBody;
import sa.zad.easyretrofitexample.model.User;

import static sa.zad.easyretrofit.EasyRetrofitClient.CACHE_POLICY_HEADER;

interface Service {

  @POST("api/register/")
  NeverErrorObservable<Void> register(@Body RegisterBody registerBody);

  @GET("api/users/{user}")
  ResultObservable<DataModel<User>> users(@Path(value = "user", encoded = true) Integer userId);

  @GET
  @Streaming
  FileDownloadObservable cacheDownload(@Url String url, @Header(CACHE_POLICY_HEADER) @CachePolicy int cachePolicy);

  @GET
  MediaStoreObservable mediaDownload(@Url String url);

  @GET
  @Streaming
  FileDownloadObservable download(@Url String url);

  @GET("/api/unknown/23")
  ResultObservable<DataModel<User>> notFound();

  @Multipart
  @POST
  UploadObservable<Void> upload(@Url String url, @Part MultipartBody.Part file);

}
