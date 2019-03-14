package sa.zad.easyretrofit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class EasyRetrofitTest {

  private MockEasyRetrofit mockEasyRetrofit;

  @Before
  public void setUp()  {
    MockitoAnnotations.initMocks(this);
    mockEasyRetrofit = mock(MockEasyRetrofit.class, CALLS_REAL_METHODS);
  }

  @Test
  public void createRetrofitTest() {
    mockEasyRetrofit.provideRetrofit();
    verify(mockEasyRetrofit, times(1)).addCallAdapterFactory();
    verify(mockEasyRetrofit, times(1)).addConverterFactory();
    verify(mockEasyRetrofit, times(1)).provideApplication();
    verify(mockEasyRetrofit, times(1)).easyRetrofitClient();
  }
}
