package sa.zad.easyretrofitexample;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import sa.zad.easyretrofit.base.ProgressObservable;

public class Utils {

  public static long readTextIntToMillis(TextView textView) {
    return readTextIntToMillis(textView, ProgressObservable.DEFAULT_THROTTLE / 1000F);
  }

  public static long readTextIntToMillis(TextView textView, long defaultValue) {
    return toLong(textView.getText().toString(), defaultValue) * 1000;
  }

  public static long readTextIntToMillis(TextView textView, float defaultValue) {
    return (long) (toFloat(textView.getText().toString(), defaultValue) * 1000);
  }

  public static float toFloat(final @Nullable String s, final float defaultValue) {
    if (s != null) {
      try {
        return Float.parseFloat(s);
      } catch (final @NonNull NumberFormatException e) {
        return defaultValue;
      }
    }
    return defaultValue;
  }

  public static long toLong(final @Nullable String s, final long defaultValue) {
    if (s != null) {
      try {
        return Long.parseLong(s);
      } catch (final @NonNull NumberFormatException e) {
        return defaultValue;
      }
    }

    return defaultValue;
  }

  public static void switchVisibility(View view) {
    switchVisibility(view, view.getVisibility() != View.VISIBLE);
  }

  public static void switchVisibility(View view, boolean visible) {
    if (visible) {
      view.setVisibility(View.VISIBLE);
    } else {
      view.setVisibility(View.GONE);
    }
  }

  public static void hideSoftKeyboard(Activity activity) {
    InputMethodManager inputMethodManager =
        (InputMethodManager) activity.getSystemService(
            Activity.INPUT_METHOD_SERVICE);
    inputMethodManager.hideSoftInputFromWindow(
        activity.getCurrentFocus().getWindowToken(), 0);
  }
}
