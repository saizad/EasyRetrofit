package sa.zad.easyretrofit;

import androidx.annotation.NonNull;

import retrofit2.HttpException;


public class ResponseException extends HttpException {

    public ResponseException(final @NonNull retrofit2.Response response) {
        super(response);
    }

    @NonNull
    public <E> E getErrorBody(Class<E> error) throws Exception {
        return EasyRetrofit.getInstance().gson().fromJson(response().errorBody().string(), error);
    }
}
