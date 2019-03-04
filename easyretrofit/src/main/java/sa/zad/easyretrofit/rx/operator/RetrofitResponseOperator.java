package sa.zad.easyretrofit.rx.operator;

import retrofit2.Response;
import sa.zad.easyretrofit.ResponseException;

public class RetrofitResponseOperator<T> extends BaseOperator<Response<T>> {

  public RetrofitResponseOperator() {
  }

  @Override
  public void onNext(Response<T> response) {
     if (!response.isSuccessful()) {
      failedResponse(response);
    } else {
      successResponse(response);
    }
  }

  protected void failedResponse(Response<T> response) {
    observer.onError(new ResponseException(response));
  }

  protected void successResponse(Response<T> response) {
    observer.onNext(response);
  }
}
