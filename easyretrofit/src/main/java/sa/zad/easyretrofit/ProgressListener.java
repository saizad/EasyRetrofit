package sa.zad.easyretrofit;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import sa.zad.easyretrofit.utils.ObjectUtils;


public interface ProgressListener<R> {
  void onProgressStart(@NonNull Progress<R> progress);
  void onProgressUpdate(@NonNull Progress<R> progress);
  void onError(@NonNull Progress<R> progress, Exception exception);
  void onFinish(@NonNull Progress<R> progress);

  class Progress<R>{
    public long written;
    public long size;
    public @Nullable
    R value;


    public Progress(long size) {
      this.size = size;
    }


    public float getProgress() {
      return (100f * written / size);
    }

    public void setWritten(long written) {
      this.written = written;
    }

    public void setValue(@Nullable R value) {
      this.value = value;
    }

    public boolean hasValue(){
      return ObjectUtils.isNotNull(value);
    }
  }
}
