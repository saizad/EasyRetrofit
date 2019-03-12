package sa.zad.easyretrofit.observables;

import android.support.annotation.NonNull;

import java.io.File;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import sa.zad.easyretrofit.ProgressListener;
import sa.zad.easyretrofit.UploadRequestBody;
import sa.zad.easyretrofit.base.ProgressObservable;

public class UploadObservable<T> extends ProgressObservable<T> {

  public UploadObservable(Observable<Response<ProgressListener.Progress<T>>> upstream) {
    super(upstream);
  }

  @NonNull
  public static MultipartBody.Part part(String key, File file) {
    return part(key, file, MediaType.parse("multipart/form-data"));
  }

  @NonNull
  public static MultipartBody.Part part(String key, File file, MediaType mediaType) {
    return MultipartBody.Part.createFormData(key, file.getName(), new UploadRequestBody(RequestBody.create(mediaType, file)));
  }
}
