package sa.zad.easyretrofit;

import android.os.Environment;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.io.File;

import io.reactivex.functions.Predicate;
import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import sa.zad.easyretrofit.call.CallFileDownloadEnqueue;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@PrepareForTest({Environment.class})
public class CallFileDownloadEnqueueTest extends BaseCallObservableTest<CallFileDownloadEnqueue> {

  @Rule
  public TemporaryFolder storageDirectory = new TemporaryFolder();

  private File nonExistentDirectory;
  private File existentDirectory;

  @Override
  protected CallFileDownloadEnqueue getCallObservable(Call mockCall) {
    return new CallFileDownloadEnqueue(mockCall);
  }

  @Override
  public void setUp() {
    super.setUp();
    nonExistentDirectory = Mockito.mock(File.class);
    Mockito.when(nonExistentDirectory.exists()).thenReturn(false);

    existentDirectory = storageDirectory.getRoot();
  }

  @Test
  public void failResponse() {
    final Response responseMock = mock(Response.class);
    final ResponseBody responseBodyMock = mock(ResponseBody.class);

    when(responseMock.code()).thenReturn(404);
    when(responseMock.errorBody()).thenReturn(responseBodyMock);

    doAnswerEnqueueSuccess(responseMock);
    callObservable.test().assertValue(progressResponse -> progressResponse.errorBody() != null);
  }

  @Test
  public void randomException() {
    final Response responseMock = mock(Response.class);
    willAnswerException().given(responseMock).isSuccessful();
    doAnswerEnqueueSuccess(responseMock);
    assertForError();
  }

  @Test
  public void enqueueSuccessTest() {
    Mockito.when(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)).thenReturn(existentDirectory);

    String content = "content";
    final Response responseMock = Response.success(ResponseBody.create(null, content), Headers.of());
    Response.success(content, Headers.of());
    doAnswerEnqueueSuccess(responseMock);
    callObservable.test().assertValue(new Predicate<Response<ProgressListener.Progress<File>>>() {
      @Override
      public boolean test(Response<ProgressListener.Progress<File>> progressResponse) throws Exception {
        return progressResponse.isSuccessful();
      }
    });
  }
}
