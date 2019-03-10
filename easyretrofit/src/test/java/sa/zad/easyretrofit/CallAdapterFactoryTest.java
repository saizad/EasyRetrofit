package sa.zad.easyretrofit;

import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import sa.zad.easyretrofit.base.EasyRetrofitCallAdapterFactory;
import sa.zad.easyretrofit.call.adapter.NeverErrorCallAdapter;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class CallAdapterFactoryTest {

  private Annotation[] annotations = {};

  @Test
  public void notParameterizedException() {
    EasyRetrofitCallAdapterFactory factory = new EasyRetrofitCallAdapterFactory();
    for (Method declaredMethod : ServiceNotParametrizedExceptionTest.class.getDeclaredMethods()) {
      try {
        factory.get(declaredMethod.getGenericReturnType(), annotations, null);
        Assert.fail();
        break;
      } catch (Exception ignored) {
      }
    }
  }

  @Test
  public void NeverErrorObservableFactoryTest() {
    EasyRetrofitCallAdapterFactory factory = new EasyRetrofitCallAdapterFactory();
    for (Method declaredMethod : NeverErrorObservableServiceTest.class.getDeclaredMethods()) {
      final Object callAdapter = factory.get(declaredMethod.getGenericReturnType(), annotations, null);
      Assert.assertEquals(callAdapter.getClass().getName(), NeverErrorCallAdapter.class.getName());
    }
  }

}