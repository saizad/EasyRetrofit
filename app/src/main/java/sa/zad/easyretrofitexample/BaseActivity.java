package sa.zad.easyretrofitexample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

  protected Service service;

  public static Intent getActivityIntent(Class<?> activity, Context context) {
    return new Intent(context, activity);
  }

  @Override
  @CallSuper
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    service = Sample.getInstance().getService();
  }
}
