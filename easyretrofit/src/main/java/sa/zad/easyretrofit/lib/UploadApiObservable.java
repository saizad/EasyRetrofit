package sa.zad.easyretrofit.lib;

import android.support.annotation.NonNull;

import java.io.File;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Response;
import sa.zad.easyretrofit.ProgressListener;
import sa.zad.easyretrofit.ProgressRequestBody;
import sa.zad.easyretrofit.base.ProgressObservable;

public class UploadApiObservable<T> extends ProgressObservable<T> {

  public UploadApiObservable(Observable<Response<ProgressListener.Progress<T>>> upstream) {
    super(upstream);
  }

  @NonNull
  public static MultipartBody.Part part(File file) {
    return MultipartBody.Part.createFormData("image", file.getName(), new ProgressRequestBody(file));
  }
}
