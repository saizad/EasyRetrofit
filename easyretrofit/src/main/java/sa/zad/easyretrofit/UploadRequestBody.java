package sa.zad.easyretrofit;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

public class UploadRequestBody extends RequestBody {

    private final RequestBody requestBody;
    private ProgressListener<?> progressListener = new ProgressListener() {
      @Override
      public void onProgressStart(@NonNull Progress progress) {

      }

      @Override
      public void onProgressUpdate(@NonNull Progress progress) {

      }

      @Override
      public void onError(@NonNull Progress progress, Exception exception) {

      }

      @Override
      public void onFinish(@NonNull Progress progress) {

      }
    };
    private final ProgressListener.Progress progress;

    public UploadRequestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
        this.progress = new ProgressListener.Progress(contentLength());
    }

    public void setListener(ProgressListener<?> progressListener) {
        this.progressListener = progressListener;
    }

    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public long contentLength() {
        try {
            return requestBody.contentLength();
        } catch (IOException e) {
            progressListener.onError(progress, e);
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public void writeTo(@NonNull BufferedSink sink) throws IOException {
        ProgressSink progressSink = new ProgressSink(sink);
        BufferedSink bufferedSink = Okio.buffer(progressSink);
        progressListener.onProgressStart(progress);
        requestBody.writeTo(bufferedSink);
        bufferedSink.flush();
        progressListener.onFinish(progress);
    }

    final class ProgressSink extends ForwardingSink {

        private long written = 0;

        ProgressSink(Sink sink) {
            super(sink);
        }

        @Override
        public void write(@NonNull Buffer source, long byteCount) throws IOException {
            super.write(source, byteCount);
            written += byteCount;
            progress.setWritten(written);
            progressListener.onProgressUpdate(progress);
        }

    }
}
