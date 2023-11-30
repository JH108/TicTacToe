import kotlinx.browser.document
import me.jesse.models.User
import react.*
import react.dom.client.createRoot

object ClientConfiguration {
    // TODO: Change this based on the build type (dev, prod, etc)
    val baseUrl = "http://localhost:8080"
    val apiUrl = "$baseUrl/api"
}

fun main() {
    val container = document.createElement("div")
    document.body!!.appendChild(container)

    val app = App.create()

    createRoot(container).render(app)
}

val UserContext = createContext<Pair<User?, (User?) -> Unit>?>(null)

val App = FC<Props> {
    val (user, setUser) = useState<User?>(null)

    UserContext.Provider(user to {
        setUser(it)
    }) {
        child(Navigation.create())
    }
}