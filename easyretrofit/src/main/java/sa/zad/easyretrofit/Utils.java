package sa.zad.easyretrofit;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import rx.functions.Action1;

public class Utils {


  public static void writeStreamToFile(@NonNull InputStream input, @NonNull File file) throws IOException {
    writeStreamToFile(input, file, integer -> {
    });
  }

  public static void writeStreamToFile(@NonNull InputStream input, @NonNull File file, @NonNull Action1<Long> writtenAction) throws IOException {
    OutputStream output = null;
    try {
      output = new FileOutputStream(file);
      byte[] buffer = new byte[4 * 1024]; // or other buffer size
      int read;
      long written = 0;
      while ((read = input.read(buffer)) != -1) {
        output.write(buffer, 0, read);
        written += read;
        writtenAction.call(written);
      }
      output.flush();
    } finally {
      input.close();

      if (Utils.isNotNull(output))
        output.close();
    }
  }

  public static boolean isNotNull(final @Nullable Object object) {
    return object != null;
  }

  @NonNull
  public static <T> T coalesce(final @Nullable T value, final @NonNull T theDefault) {
    if (value != null) {
      return value;
    }
    return theDefault;
  }

  public static float decimalFloat(float value) {
    DecimalFormat df = new DecimalFormat("#.##");
    df.setRoundingMode(RoundingMode.HALF_UP);
    return Float.parseFloat(df.format(value));
  }

}
