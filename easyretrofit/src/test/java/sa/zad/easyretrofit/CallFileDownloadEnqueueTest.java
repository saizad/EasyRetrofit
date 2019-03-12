package sa.zad.easyretrofit;

import org.junit.Test;

import retrofit2.Call;
import retrofit2.Response;
import sa.zad.easyretrofit.call.CallFileDownloadEnqueue;

public class CallFileDownloadEnqueueTest extends BaseCallObservableTest<CallFileDownloadEnqueue> {

  @Override
  protected CallFileDownloadEnqueue getCallObservable(Call mockCall) {
    return new CallFileDownloadEnqueue(mockCall);
  }

  @Test
  public void failResponse() {
    failedResponse();
  }

  @Test
  public void randomExceptionAfterSuccessRequest() {
    doAnswerEnqueueSuccess(Response.success(""));
    callObservable.test().assertError(__ -> true);
  }
}
