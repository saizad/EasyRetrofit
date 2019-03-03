package sa.zad.easyretrofit.base;

import android.os.Environment;
import android.support.annotation.NonNull;

import java.io.File;
import java.net.URI;

import okhttp3.HttpUrl;
import okhttp3.ResponseBody;
import retrofit2.Call;
import rx.functions.Action1;
import sa.zad.easyretrofit.ProgressListener;
import sa.zad.easyretrofit.utils.FileUtils;

public class FileDownloadEnqueue extends CallDownloadEnqueueObservable<File> {

  public FileDownloadEnqueue(Call<ProgressListener.Progress<File>> originalCall) {
    super(originalCall);
  }

  @Override
  protected final File responseBodyReady(@NonNull ResponseBody responseBody, HttpUrl url, Action1<Integer> writtenCallback) throws Exception {
    final File saveTo = saveTo(url);
    FileUtils.writeStreamToFile(responseBody.byteStream(), saveTo, writtenCallback);
    return saveTo;
  }

  @NonNull
  protected File saveTo(HttpUrl url) throws Exception{
    File file = new File(new URI(url.toString()).getPath());
    return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
        file.getName());
  }
}
