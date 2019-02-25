package sa.zad.easyretrofitexample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

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

  public void toast(String toastText) {
    Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show();
  }

  public void log(String logText) {
    log(this.getLocalClassName(), logText);
  }

  public void log(String tag, String logText) {
    Log.d(this.getLocalClassName(), logText);
  }
}
