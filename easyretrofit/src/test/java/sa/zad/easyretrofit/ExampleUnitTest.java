package sa.zad.easyretrofit;

import org.junit.Test;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
  @Test
  public void addition_isCorrect() {
    DecimalFormat df = new DecimalFormat("#.##");
    df.setRoundingMode(RoundingMode.DOWN);
    final double number = 100.0001;
    System.out.println("Down = " + df.format(number));
    df.setRoundingMode(RoundingMode.UP);
    System.out.println( "UP = " + df.format(number));
    df.setRoundingMode(RoundingMode.HALF_DOWN);
    System.out.println( "HALF_DOWN = " + df.format(number));
    df.setRoundingMode(RoundingMode.HALF_EVEN);
    System.out.println( "HALF_EVEN = " + df.format(number));
    df.setRoundingMode(RoundingMode.CEILING);
    System.out.println( "CEILING = " + df.format(number));
    df.setRoundingMode(RoundingMode.HALF_UP);
    System.out.println( "HALF_UP = " + df.format(number));
    df.setRoundingMode(RoundingMode.FLOOR);
    System.out.println( "FLOOR = " + df.format(number));
    assertEquals(4, 2 + 2);
  }
}