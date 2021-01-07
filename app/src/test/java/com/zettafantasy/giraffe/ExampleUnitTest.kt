package com.zettafantasy.giraffe

import com.zettafantasy.giraffe.data.Converters
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun testConverters() {
        val converters = Converters()
        val str: String = converters.fromListInt(listOf(0))
        assertEquals(str, "[0]")

        val array = converters.toListInt(str)
        assertEquals(0, array[0])
    }
}