package sa.zad.easyretrofit.lib;

import android.support.annotation.NonNull;

import java.io.File;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import sa.zad.easyretrofit.ProgressListener;
import sa.zad.easyretrofit.UploadRequestBody;

public class UploadObservable<T> extends ProgressObservable<T> {

  public UploadObservable(Observable<Response<ProgressListener.Progress<T>>> upstream) {
    super(upstream);
  }

  @NonNull
  public static MultipartBody.Part part(File file) {
    return part(file, MediaType.parse("multipart/form-data"));
  }

  @NonNull
  public static MultipartBody.Part part(File file, MediaType mediaType) {
    return MultipartBody.Part.createFormData("image", file.getName(), new UploadRequestBody(RequestBody.create(mediaType, file)));
  }
}
