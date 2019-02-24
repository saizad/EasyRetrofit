package sa.zad.easyretrofitexample;

import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.IOException;

import okhttp3.ResponseBody;
import sa.zad.easyretrofit.ProgressListener;
import sa.zad.easyretrofit.lib.adapter.DownloadApiCallAdapter;

public class SampleDownloadApiCallAdapter extends DownloadApiCallAdapter {

  @Override
  protected void saveToDisk(ResponseBody body, File destinationFile, ProgressListener<File> progressListener) {
    File file;
    try {
      file = File.createTempFile(destinationFile.getName(), null, Sample.getInstance().getCacheDir());
      super.saveToDisk(body, file, progressListener);
      if (isFileImage(file)) {
        MediaStore.Images.Media.insertImage(
            Sample.getInstance().getContentResolver(),
            file.getPath(), file.getName(), null
        );
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static boolean isFileImage(@NonNull File file){
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeFile(file.getPath(), options);
    return options.outHeight != -1;
  }
}
