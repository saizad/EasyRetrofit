package sa.zad.easyretrofitexample

import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.exceptions.MissingBackpressureException
import io.reactivex.schedulers.Schedulers
import org.junit.Assert.assertEquals
import org.junit.Test


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun whenMissingStrategyUsed_thenException() {
        val observable = Observable.range(1, 200)
        val subscriber = observable
                .toFlowable(BackpressureStrategy.LATEST)
                .observeOn(Schedulers.computation())
                .reduce { t1: Int, t2: Int ->
                    val i = t1 + t2
//                    System.out.println(t1)
//                    System.out.println(t2)
//                    System.out.println(i)
//                    System.out.println()
                    i
                }
                .doOnSuccess {
                    System.out.println(it)
                }
                .test()
        subscriber.awaitTerminalEvent()
        subscriber.assertError(MissingBackpressureException::class.java)
    }
}
