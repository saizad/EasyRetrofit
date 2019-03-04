package sa.zad.easyretrofitexample.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import rx.functions.Func2;
import sa.zad.easyretrofit.ResponseException;
import sa.zad.easyretrofit.utils.Utils;


public class ErrorModel implements Serializable {

  @Expose @SerializedName("error") public Error error;

  @Nullable
  public static Error fromThrowable(final @NonNull Throwable t) {
    if (t instanceof ResponseException) {
      final ResponseException exception = (ResponseException) t;
      final ErrorModel errorBody = exception.getErrorBody(ErrorModel.class);
      return errorBody.error;
    }
    return null;
  }

  public static List<String> buildErrors(Error error, Func2<String, String, Boolean> callback){
    List<String> errors = new ArrayList<>();
    errors.add(error.description);
    for (ErrorFields field : error.fields) {
      if(!callback.call(field.field, field.message)){
        errors.add(String.format("%s : %s", field.field, field.message));
      }
    }
    return errors;
  }

  @Nullable
  public static String getFieldError(String key, Error error){
    for (ErrorFields field : error.fields) {
      if(field.field.equalsIgnoreCase(key)){
        return field.message;
      }
    }
    return null;
  }

  public static String buildMultiError(Error error, String... key){
    StringBuilder message = new StringBuilder();
    for (String s : key) {
      for (ErrorFields field : error.fields) {
        if(field.field.equalsIgnoreCase(s)){
          message.append(field.message).append("\n");
        }
      }
    }
    return Utils.stripTrailingLeadingNewLines(message.toString());
  }

  public static Error buildNewErrorField(Error error, String fieldName, String message){
    if(!Utils.isNullOrEmpty(message)){
      error.fields.add(new ErrorFields(message, fieldName));
    }
    return error;
  }

  public static Error buildNewErrorFieldFromFields(Error error, String fieldName, String... key){
    return buildNewErrorField(error, fieldName, buildMultiError(error, key));
  }
  public static class Error implements Serializable {

    @Expose @SerializedName("fields") public List<ErrorFields> fields;
    @Expose @SerializedName("description") public String description;
    @Expose @SerializedName("error_code") public int errorCode;
    @Expose @SerializedName("error") public String error;
    @Expose @SerializedName("status") public int status;

  }

  public static class ErrorFields {

    public ErrorFields(){}

    public ErrorFields(String message, String field) {
      this.message = message;
      this.field = field;
    }

    @Expose @SerializedName("message") public String message;
    @Expose @SerializedName("field") public String field;
  }
}
