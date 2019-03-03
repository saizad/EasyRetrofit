package sa.zad.easyretrofit.utils;

import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import rx.functions.Action1;

public class FileUtils {

  public static void writeStreamToFile(@NonNull InputStream input, @NonNull File file) throws IOException {
    writeStreamToFile(input, file, integer -> {
    });
  }

  public static void writeStreamToFile(@NonNull InputStream input, @NonNull File file, @NonNull Action1<Integer> writtenAction) throws IOException {
    OutputStream output = null;
    try {
      output = new FileOutputStream(file);
        byte[] buffer = new byte[4 * 1024]; // or other buffer size
        int read;
        int written = 0;
        while ((read = input.read(buffer)) != -1) {
          output.write(buffer, 0, read);
          written += read;
          writtenAction.call(written);
          throw new IOException("asdfasdf");
        }
        output.flush();
    } finally {
      input.close();

      if(ObjectUtils.isNotNull(output))
        output.close();
    }
  }
}
