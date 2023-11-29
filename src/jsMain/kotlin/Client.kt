import kotlinx.browser.document
import react.create
import react.dom.client.createRoot

object ClientConfiguration {
    // TODO: Change this based on the build type (dev, prod, etc)
    val baseUrl = "http://localhost:8080"
    val apiUrl = "$baseUrl/api"
}

fun main() {
    val container = document.createElement("div")
    document.body!!.appendChild(container)

    val clientApplication = ClientApplication.create {
        configuration = ClientConfiguration
    }

    createRoot(container).render(clientApplication)
}