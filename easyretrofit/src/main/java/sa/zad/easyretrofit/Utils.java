package sa.zad.easyretrofit;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Patterns;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import rx.functions.Action1;

public final class Utils {

  public static boolean isNotNull(final @Nullable Object object) {
    return object != null;
  }

  /**
   * Returns the first non-null value of its arguments.
   */
  @NonNull
  public static <T> T coalesce(final @Nullable T value, final @NonNull T theDefault) {
    if (value != null) {
      return value;
    }
    return theDefault;
  }

  /**
   * Converts a {@link String} to an {@link Integer}, or default int if the integer cannot be parsed.
   */
  public static @NonNull
  Integer toInteger(final @Nullable String s, final Integer defaultInt) {
    if (s != null) {
      try {
        return Integer.parseInt(s);
      } catch (final @NonNull NumberFormatException e) {
        return defaultInt;
      }
    }

    return defaultInt;
  }



  @Nullable
  public static <T> T createInstance(Class<T> tClass){
    try {
      return tClass.newInstance();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    return null;
  }

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
      }
      output.flush();
    } finally {
      input.close();

      if(Utils.isNotNull(output))
        output.close();
    }
  }

  public static boolean isURL(String url) {
    if (url == null) {
      return false;
    }
    return Patterns.WEB_URL.matcher(url).matches();
  }

  public static String trimEnd(String value) {
    // Use replaceFirst to remove trailing spaces.
    return value.replaceFirst("\\s+$", "");
  }

  public static String trimStart(String value) {
    // Remove leading spaces.
    return value.replaceFirst("^\\s+", "");
  }

  public static String stripTrailingLeadingNewLines(String text) {
    return trimEnd(trimStart(text));
  }

  /**
   * Check if a string is null or empty
   *
   * @param s string to check
   * @return True if null or empty
   */
  public static boolean isNullOrEmpty(String s) {
    return s == null || s.isEmpty();
  }
}
