package sa.zad.easyretrofit;

import org.junit.Test;

import java.io.IOException;

import retrofit2.Response;

import static org.mockito.Mockito.when;

public class CallObservableTest extends BaseCallObservableTest {

  @Test
  public void executeThrowableTest() throws IOException {
    willAnswerException().given(mockCall).execute();
    assertForError();
  }

  @Test
  public void executeSuccessTest() throws IOException {
    when(mockCall.execute()).thenReturn(Response.success(FAKE_BODY));
    assertForSuccess();
  }

  @Test
  public void observableCompleteTest() throws Exception {
    when(mockCall.execute()).thenReturn(Response.success(FAKE_BODY));
    assertForComplete();
  }
}

