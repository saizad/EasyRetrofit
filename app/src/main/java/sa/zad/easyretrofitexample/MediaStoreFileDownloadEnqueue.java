package sa.zad.easyretrofitexample;

import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import androidx.annotation.NonNull;

import java.io.File;
import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.ResponseBody;
import retrofit2.Call;
import rx.functions.Action1;
import sa.zad.easyretrofit.ProgressListener;
import sa.zad.easyretrofit.call.CallFileDownloadEnqueue;

public class MediaStoreFileDownloadEnqueue extends CallFileDownloadEnqueue {

  public MediaStoreFileDownloadEnqueue(Call<ProgressListener.Progress<File>> originalCall) {
    super(originalCall);
  }

  @Override
  protected File responseBodyReady(@NonNull ResponseBody responseBody, HttpUrl url, Action1<Long> writtenCallback) throws Exception {
    final File file = super.responseBodyReady(responseBody, url, writtenCallback);
    try {
      if (isFileImage(file)) {
        MediaStore.Images.Media.insertImage(
            Sample.getInstance().getContentResolver(),
            file.getPath(), file.getName(), null
        );
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return file;
  }

  private static boolean isFileImage(@NonNull File file){
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeFile(file.getPath(), options);
    return options.outHeight != -1;
  }
}
