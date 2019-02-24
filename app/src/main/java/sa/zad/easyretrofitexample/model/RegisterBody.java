package sa.zad.easyretrofitexample.model;

import com.google.gson.annotations.SerializedName;

public class RegisterBody {

  public RegisterBody(String email, String password) {
    this.email = email;
    this.password = password;
  }

  @SerializedName("email")
  public String email;
  @SerializedName("password")
  public String password;
}
