package ink.duo3.caelum.api.model

import kotlinx.serialization.Serializable

@Serializable
data class WebResp<T>(
    val ok: Boolean,
    val data: T
)