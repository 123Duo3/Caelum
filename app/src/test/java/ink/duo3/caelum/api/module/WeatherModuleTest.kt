package ink.duo3.caelum.api.module

import ink.duo3.caelum.api.CaelumApiClientTest
import kotlinx.coroutines.test.runTest
import org.junit.Test

class WeatherModuleTest {
    private val client = CaelumApiClientTest.Companion.getTestClient()

    @Test
    fun testGetLocations() = runTest {
        println(client.weatherModule().getCityByLocation(117.22, 31.82))
    }

    @Test
    fun testGet10Day() = runTest {
        println(client.weatherModule().get10d("101040100"))
    }
}