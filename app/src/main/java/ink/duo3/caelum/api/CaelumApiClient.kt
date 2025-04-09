package ink.duo3.caelum.api

import ink.duo3.caelum.api.exception.CaulumApiClientException
import ink.duo3.caelum.api.module.WeatherModule
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.serialization.json.Json

class CaelumApiClient(val baseUrl: String) {
    internal val client = HttpClient()

    internal fun buildUrl(path: String): String {
        return baseUrl + path
    }

    internal suspend inline fun <reified T> get(path: String, block: HttpRequestBuilder.() -> Unit): T {
        val resp = try {
            client.get(buildUrl(path), block)
        } catch (e: Exception) {
            throw CaulumApiClientException("Request failed", e)
        }

        if (!resp.status.isSuccess()) {
            throw CaulumApiClientException("Response error, status code: ${resp.status}")
        }

        val content = resp.bodyAsText()
        val data = try {
            Json.decodeFromString<T>(content)
        } catch (e: Exception) {
            throw CaulumApiClientException("Failed to parse response, content: $content", e)
        }
        return data
    }

    fun weatherModule(): WeatherModule = WeatherModule(this)
}