package sa.zad.easyretrofit;

import org.junit.Before;
import org.junit.Test;

import okhttp3.MultipartBody;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Response;
import sa.zad.easyretrofit.call.CallUploadEnqueueObservable;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CallUploadEnqueueTest extends BaseCallObservableTest<CallUploadEnqueueObservable<ProgressListener.Progress>> {

  private UploadRequestBody mockUploadRequest;

  @Override
  public CallUploadEnqueueObservable<ProgressListener.Progress> getCallObservable(Call mockCall) {
    return new CallUploadEnqueueObservable<>(mockCall);
  }

  @Before
  public void before() {
    mockUploadRequest = setRequestBody();
  }

  private UploadRequestBody setRequestBody() {
    UploadRequestBody mockUploadRequest = mock(UploadRequestBody.class);
    Request.Builder builder = new Request.Builder();
    final MultipartBody.Part part = MultipartBody.Part.createFormData("name", "test", mockUploadRequest);
    builder.url("http://localhost/");
    MultipartBody.Builder mulBuilder = new MultipartBody.Builder();
    mulBuilder.addPart(part);
    final Request request = builder.put(mulBuilder.build()).build();
    when(mockCall.request()).thenReturn(request);
    return mockUploadRequest;
  }

  @Test
  public void uploadRequestListener_Set() {
    callObservable.test();
    verify(mockUploadRequest).setListener(callObservable);
  }

  @Test
  public void uploadProgressUpdate() {
    final ProgressListener.Progress progress = new ProgressListener.Progress(3L);
    doAnswerInvocation(__ -> {
      callObservable.onProgressStart(progress);
      callObservable.onProgressUpdate(progress);
      callObservable.onFinish(progress);
    }).when(mockUploadRequest).setListener(any(ProgressListener.class));
    doAnswerEnqueueSuccess();

    callObservable.test().assertValueCount(2);
  }

  @Test
  public void responseProgressSuccess() {
    final UploadRequestBody mockUploadRequest = setRequestBody();
    final ProgressListener.Progress progress = new ProgressListener.Progress(3L);
    doAnswerInvocation(__ -> callObservable.onProgressUpdate(progress))
        .when(mockUploadRequest).setListener(any(ProgressListener.class));
    doAnswerEnqueueSuccess(Response.success(progress));

    callObservable.test().assertValueCount(2);
  }

  @Test
  public void responseFailed() {
    failedResponse();
  }

  @Test
  public void observableCompleteTest() {
    final ProgressListener.Progress progress = new ProgressListener.Progress(3L);
    doAnswerInvocation(__ -> {
      callObservable.onProgressStart(progress);
    }).when(mockUploadRequest).setListener(any(ProgressListener.class));
    testComplete();
  }

  @Test
  public void uploadError() {
    final UploadRequestBody mockUploadRequest = setRequestBody();
    final ProgressListener.Progress progress = new ProgressListener.Progress(3L);
    doAnswerInvocation(__ -> callObservable.onError(progress, fakeException))
        .when(mockUploadRequest).setListener(any(ProgressListener.class));
    assertForError();
  }
}
