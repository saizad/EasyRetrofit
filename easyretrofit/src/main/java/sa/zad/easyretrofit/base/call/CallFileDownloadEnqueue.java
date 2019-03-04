package sa.zad.easyretrofit.base.call;

import android.os.Environment;
import android.support.annotation.NonNull;

import java.io.File;
import java.net.URI;

import okhttp3.HttpUrl;
import okhttp3.ResponseBody;
import retrofit2.Call;
import rx.functions.Action1;
import sa.zad.easyretrofit.ProgressListener;
import sa.zad.easyretrofit.base.CallDownloadEnqueueObservable;
import sa.zad.easyretrofit.utils.Utils;

public class CallFileDownloadEnqueue extends CallDownloadEnqueueObservable<File> {

  public CallFileDownloadEnqueue(Call<ProgressListener.Progress<File>> originalCall) {
    super(originalCall);
  }

  @Override
  protected final File responseBodyReady(@NonNull ResponseBody responseBody, HttpUrl url, Action1<Integer> writtenCallback) throws Exception {
    final File saveTo = saveTo(url);
    Utils.writeStreamToFile(responseBody.byteStream(), saveTo, writtenCallback);
    return saveTo;
  }

  @NonNull
  protected File saveTo(HttpUrl url) throws Exception{
    File file = new File(new URI(url.toString()).getPath());
    return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
        file.getName());
  }
}
