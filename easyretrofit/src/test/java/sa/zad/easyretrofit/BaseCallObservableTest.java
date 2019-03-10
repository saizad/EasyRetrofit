package sa.zad.easyretrofit;

import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;

import org.junit.Before;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.functions.Action1;
import rx.functions.Func1;
import sa.zad.easyretrofit.call.CallObservable;

import static org.mockito.BDDMockito.willAnswer;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

public abstract class BaseCallObservableTest<C extends CallObservable<?>> {

  protected static final String FAKE_BODY = "fake_body";
  protected static final String FAKE_THROWABLE_MESSAGE = "fake_throwable";
  @Mock
  protected Call mockCall;
  protected C callObservable;

  @Before
  @CallSuper
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    callObservable = getCallObservable(mockCall);
    when(mockCall.clone()).thenReturn(mockCall);
  }

  protected C getCallObservable(Call mockCall) {
    return (C) new CallObservable<>(mockCall);
  }

  protected void assertForError() {
    callObservable.test().assertError(throwable -> throwable.getMessage().equalsIgnoreCase(FAKE_THROWABLE_MESSAGE));
  }

  protected void assertForSuccess() {
    assertForSuccess(response -> {
      if (response.body() instanceof String) {
        final String body = (String) response.body();
        return body.equalsIgnoreCase(FAKE_BODY);
      }
      return false;
    });
  }

  protected void assertForSuccess(Func1<Response<?>, Boolean> assertCallback) {
    callObservable.test().assertValue(assertCallback::call);
  }

  protected void doAnswerEnqueueFailed() {
    doAnswerEnqueue(callback -> {
      callback.onFailure(mockCall, new Exception(FAKE_THROWABLE_MESSAGE));
    });
  }

  protected void doAnswerEnqueue(Action1<Callback<Object>> action1) {
    doAnswer(invocation -> {
      Callback<Object> callback = (Callback<Object>) invocation.getArguments()[0];
      action1.call(callback);
      return callback;
    }).when(mockCall).enqueue(any(Callback.class));
  }

  protected void doAnswerEnqueueSuccess() {
    doAnswerEnqueueSuccess(Response.success(FAKE_BODY));
  }

  protected void doAnswerEnqueueSuccess(@Nullable Response<Object> response) {
    doAnswerEnqueue(callback -> {
      callback.onResponse(mockCall, response);
    });
  }

  protected BDDMockito.BDDStubber willAnswerException(){
    return willAnswer(invocation -> {
      throw new Exception(FAKE_THROWABLE_MESSAGE);
    });
  }
}
