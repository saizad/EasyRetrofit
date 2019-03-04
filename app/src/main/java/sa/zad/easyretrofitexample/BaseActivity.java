package sa.zad.easyretrofitexample;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import sa.zad.easyretrofit.Utils;


public abstract class BaseActivity extends AppCompatActivity {

  protected Service service;

  private PublishSubject<ActivityResult> activityResultPublishSubject = PublishSubject.create();
  private PublishSubject<PermissionResult> permissionResultPublishSubject = PublishSubject.create();

  public static Intent getActivityIntent(Class<?> activity, Context context) {
    return new Intent(context, activity);
  }

  @Override
  @CallSuper
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    service = Sample.getInstance().getService();
  }

  public void log(String logText) {
    log(this.getLocalClassName(), logText);
  }

  public void log(String tag, String logText) {
    Log.d(this.getLocalClassName(), logText);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    activityResultPublishSubject.onNext(new ActivityResult(requestCode, resultCode, data));
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    permissionResultPublishSubject.onNext(new PermissionResult(requestCode, permissions, grantResults));
  }

  protected Observable<Intent> result(int requestCode) {
    return activityResultPublishSubject.filter(activityResult ->
        activityResult.resultCode == RESULT_OK
            && activityResult.requestCode == requestCode
            && Utils.isNotNull(activityResult.data))
        .map(activityResult -> activityResult.data);
  }

  @SuppressLint("CheckResult")
  public Observable<Boolean> permissionResult(int requestCode) {
    return permissionResultPublishSubject
        .filter(permissionResult -> permissionResult.requestCode == requestCode)
        .map(permissionResult -> {
          for (String permission : permissionResult.permissions) {
            if (!isPermGranted(BaseActivity.this, permission)) {
              return false;
            }
          }
          return true;
        });
  }

  public static boolean isPermGranted(Context context, String permsCheck) {
    return ActivityCompat.checkSelfPermission(context, permsCheck)
        == PackageManager.PERMISSION_GRANTED;
  }

  public void toast(String toastText) {
    Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show();
  }

  public static class ActivityResult {

    public final int requestCode;
    public final int resultCode;
    @Nullable
    public final Intent data;

    ActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
      this.requestCode = requestCode;
      this.resultCode = resultCode;
      this.data = data;
    }
  }

  public static class PermissionResult {

    public final int requestCode;
    @NonNull
    public final String[] permissions;
    @NonNull
    public final int[] grantResults;

    PermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
      this.requestCode = requestCode;
      this.permissions = permissions;
      this.grantResults = grantResults;
    }
  }
}
