package sa.zad.easyretrofit;

import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;


public class ProgressRequestBody extends RequestBody {

  private static final int DEFAULT_BUFFER_SIZE = 4096;

  private ProgressListener mListener;

  private final File mFile;

  public ProgressRequestBody(File file) {
    this(file, new ProgressListener() {
      @Override
      public void onProgressStart(@NonNull Progress upload) {

      }

      @Override
      public void onProgressUpdate(@NonNull Progress upload) {

      }

      @Override
      public void onError(@NonNull Progress upload, Exception exception) {

      }

      @Override
      public void onFinish(@NonNull Progress upload) {

      }
    });
  }

  public ProgressRequestBody(@NonNull File file, @NonNull ProgressListener listener) {
    mListener = listener;
    mFile = file;
  }

  public void setListener(ProgressListener mListener) {
    this.mListener = mListener;
  }

  @Override
  public MediaType contentType() {
    return MediaType.parse("multipart/form-data");
  }

  @Override
  public long contentLength() {
    return mFile.length();
  }

  @Override
  public void writeTo(@NonNull BufferedSink sink) {
    long fileLength = mFile.length();
    ProgressListener.Progress upload = new ProgressListener.Progress(fileLength);
    byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
    long uploaded = 0;

    try (FileInputStream in = new FileInputStream(mFile)) {
      mListener.onProgressStart(upload);
      while (true) {

        int read = in.read(buffer);
        if (read == -1) {
          break;
        }
        sink.write(buffer, 0, read);
        uploaded += read;
        upload.setWritten(uploaded);
        mListener.onProgressUpdate(upload);
      }

    } catch (Exception exception) {
      mListener.onError(upload,exception);
    } finally {
      mListener.onFinish(upload);
    }
  }
}
