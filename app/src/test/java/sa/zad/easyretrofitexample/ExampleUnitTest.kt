package sa.zad.easyretrofitexample

import org.junit.Assert
import org.junit.Test


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun test() {
        var tofloat = Utils.toFloat("0.2", 0F)
        Assert.assertEquals(tofloat*1000, 1)
    }
}
