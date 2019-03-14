package sa.zad.easyretrofit.call;

import org.junit.Test;

import retrofit2.Call;

import static org.mockito.Mockito.when;

public class CallEnqueueObservableTest extends BaseCallObservableTest<CallEnqueueObservable<String>> {

  @Override
  protected CallEnqueueObservable<String> getCallObservable(Call mockCall) {
    return new CallEnqueueObservable<String>(mockCall);
  }

  @Test
  public void enqueueSuccessTest() {
    when(mockCall.isExecuted()).thenReturn(true);
    doAnswerEnqueueSuccess();
    assertForSuccess();
  }

  @Test
  public void enqueueFailTest() {
    when(mockCall.isExecuted()).thenReturn(true);
    doAnswerEnqueueError();
    assertForError();
  }

  @Test
  public void observableCompleteTest() {
    testComplete();
  }
}
