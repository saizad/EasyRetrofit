package sa.zad.easyretrofit;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;


@Retention(SOURCE)
@IntDef({CachePolicy.LOCAL_IF_AVAILABLE, CachePolicy.LOCAL_IF_FRESH, CachePolicy.SERVER_ONLY, CachePolicy.LOCAL_ONLY})
public @interface CachePolicy {

  int LOCAL_IF_AVAILABLE = 0;
  int LOCAL_IF_FRESH = 10;
  int SERVER_ONLY = 20;
  int LOCAL_ONLY = 30;
}
