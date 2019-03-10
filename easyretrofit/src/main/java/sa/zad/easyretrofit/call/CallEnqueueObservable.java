package sa.zad.easyretrofit.call;

import android.support.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.functions.Action1;

public class CallEnqueueObservable<T> extends CallObservable<T> {

  public CallEnqueueObservable(Call<T> originalCall) {
    super(originalCall);
  }

  @Override
  protected void makeCall(Action1<? super Response<T>> responseAction, Action1<Throwable> throwableAction)   {
    call.enqueue(new Callback<T>() {
      @Override
      public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
        responseAction.call(response);
      }

      @Override
      public void onFailure(@NonNull retrofit2.Call<T> call, @NonNull Throwable t) {
        throwableAction.call(t);
      }
    });
  }
}
