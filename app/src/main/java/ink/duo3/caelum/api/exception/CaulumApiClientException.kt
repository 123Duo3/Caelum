package ink.duo3.caelum.api.exception

class CaulumApiClientException: Exception {
    constructor(message: String): super(message)
    constructor(cause: Throwable): super(cause)
    constructor(message: String, cause: Throwable): super(message, cause)
}