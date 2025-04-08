package ink.duo3.caelum.api

import kotlinx.coroutines.test.runTest
import org.junit.Test


class WeatherModuleTest {
    private val client = CaelumApiClientTest.getTestClient()

    @Test
    fun testGetLocations() = runTest {
        println(client.weatherModule().getCityByLocation(117.22, 31.82))
    }
}