package sa.zad.easyretrofitexample.model;

import com.google.gson.annotations.SerializedName;

public class DataModel<M> {

  @SerializedName("data") public M data;
}
