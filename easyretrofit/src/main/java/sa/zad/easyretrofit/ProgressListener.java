package sa.zad.easyretrofit;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


public interface ProgressListener<R> {
  void onProgressStart(@NonNull Progress<R> progress);

  void onProgressUpdate(@NonNull Progress<R> progress);

  void onError(@NonNull Progress<R> progress, Exception exception);

  void onFinish(@NonNull Progress<R> progress);

  class Progress<R> {
    public long written;
    public long size;
    public @Nullable
    R value;
    private final long startTime;
    private long latestTime;


    public Progress(long size) {
      this(size, System.currentTimeMillis());
    }

    public Progress(long size, long startTime) {
      this.size = size;
      this.startTime = startTime;
    }

    public long estimatedTimeRemaining() {
      return totalDuration() - elapsedTime();
    }

    public long elapsedTime(){
      return latestTime - startTime;
    }

    public long totalDuration() {
      return (elapsedTime() * size / written);
    }

    public long sizeRemaining(){
      return size - written;
    }

    public float sizeRemainingKB(){
      return Utils.decimalFloat(sizeRemaining() / 1024F);
    }

    public float sizeRemainingMB(){
      return Utils.decimalFloat(sizeRemainingKB() / 1024F);
    }

    public float getProgress() {
      return Utils.decimalFloat(100f * written / size);
    }

    public void setWritten(long written) {
      latestTime = System.currentTimeMillis();
      this.written = written;
    }

    public void setValue(@Nullable R value) {
      this.value = value;
    }

    public boolean isCompleted(){
      return written == size;
    }

    @NonNull
    @Override
    public String toString() {
      return "Size = " + size + " Written = " + written + " Progress = " + getProgress();
    }
  }
}
