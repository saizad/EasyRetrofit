package sa.zad.easyretrofitexample;

import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import java.io.File;

import sa.zad.easyretrofit.call.adapter.FileDownloadCallAdapter;

public class SampleDownloadApiCallAdapter extends FileDownloadCallAdapter {

  /*@Override
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
  }*/

  private static boolean isFileImage(@NonNull File file){
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeFile(file.getPath(), options);
    return options.outHeight != -1;
  }
}
