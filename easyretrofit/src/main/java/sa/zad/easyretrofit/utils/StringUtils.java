package sa.zad.easyretrofit.utils;

import android.support.annotation.Nullable;
import android.util.Patterns;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rx.functions.Func1;

public class StringUtils {


  public static boolean isNameValid(String name) {
    if (name == null) {
      return false;
    }
    final String[] split = stripTrailingLeadingNewLines(name).split(" ");
    if(split.length > 2){
      return false;
    }

    for (int i = 0; i < split.length; i++) {
      final String splitName = split[i];
      if(i == 0 && !isSingleNameValid(splitName)){
        return false;
      }else if(i == 1 && !isLastNameValid(splitName)){
        return false;
      }
    }
    return true;
  }

  public static boolean isLastNameValid(@Nullable String name) {
    return ObjectUtils.isNull(name) || isSingleNameValid(name);
  }

  public static boolean isSingleNameValid(@Nullable String name) {
    return ObjectUtils.isNotNull(name) && name.matches("^[A-Za-z-]+$");
  }

  public static boolean isUserNameValid(@Nullable String name) {
    return ObjectUtils.isNull(name) || name.matches("^[0-9A-Za-z_-]+$");
  }

  public static boolean isEmail(String email) {
    if (email == null) {
      return false;
    }
    // Assigning the email format regular expression
    String emailPattern = "^([A-Za-z0-9_\\+\\-\\.])+\\@([A-Za-z0-9_\\-\\.])+\\.([A-Za-z]{2,4})";
    return email.matches(emailPattern);
  }

  public static boolean isValidEmail(String email) {
    return !StringUtils.isNullOrEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
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

  public static int alphabetFirstCompare(String lhs, String rhs) {
    boolean lhsStartsWithLetter = Character.isLetter(lhs.charAt(0));
    boolean rhsStartsWithLetter = Character.isLetter(rhs.charAt(0));

    if ((lhsStartsWithLetter && rhsStartsWithLetter) || (!lhsStartsWithLetter
        && !rhsStartsWithLetter)) {
      // they both start with letters or not-a-letters
      return lhs.compareTo(rhs);
    } else if (lhsStartsWithLetter) {
      // the first string starts with letter and the second one is not
      return -1;
    } else {
      // the second string starts with letter and the first one is not
      return 1;
    }
  }

  public static boolean isNumber(String num) {
    String regex = "^\\-?[0-9]*\\.?[0-9]+$";
    Pattern p = Pattern.compile(regex);
    Matcher m = p.matcher(num);
    return m.find();
  }

  public static String join(String delimiter, String... strings) {
    if (strings.length > 0) {
      StringBuilder stringBuilder = new StringBuilder();
      for (String n : strings) {
        stringBuilder.append(n).append(delimiter);
      }
      stringBuilder.deleteCharAt(stringBuilder.length() - 1);
      return stringBuilder.toString();
    } else {
      return "";
    }
  }

  @Nullable
  public static <T> String extractListValue(List<T> list, Func1<T, String> mapValue) {
    if (!list.isEmpty()) {
      StringBuilder stringBuilder = new StringBuilder();
      for (T n : list) {
        stringBuilder.append(mapValue.call(n)).append(",");
      }
      stringBuilder.deleteCharAt(stringBuilder.length() - 1);
      return stringBuilder.toString();
    } else {
      return null;
    }
  }

  public static String capitalizeFirstWord(String string) {
    if (ObjectUtils.isNull(string) || string.length() == 0) {
      return string;
    } else {
      return string.substring(0, 1).toUpperCase() + string.substring(1);
    }
  }

  public static boolean isSame(@Nullable String str1, @Nullable String str2) {
    return isSame(str1, str2, true);
  }

  public static boolean isSame(@Nullable String str1, @Nullable String str2, boolean ignoreCase) {
    str1 = ObjectUtils.coalesce(str1, "");
    str2 = ObjectUtils.coalesce(str2, "");
    if (ignoreCase) { return str1.equalsIgnoreCase(str2); } else { return str1.equals(str2); }
  }
}
