package ink.duo3.caelum.api

import ink.duo3.caelum.BuildConfig

class CaelumApiClientTest {
    companion object {
        fun getTestClient(): CaelumApiClient {
            return CaelumApiClient(BuildConfig.API_BASE_URL)
        }
    }
}