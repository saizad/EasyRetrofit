package sa.zad.easyretrofitexample.model;

import com.google.gson.annotations.SerializedName;

public class User {
  @SerializedName("id")
  public Long id;
  @SerializedName("first_name")
  public String first_name;
  @SerializedName("last_name")
  public String last_name;
  @SerializedName("download")
  public String avatar;
}
