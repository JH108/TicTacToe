import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.serialization.encodeToString
import me.jesse.models.User
import me.jesse.serializers.CommonSerializerModule
import react.*
import react.dom.client.createRoot

object ClientConfiguration {
    // TODO: Change this based on the build type (dev, prod, etc)
    val baseUrl = "http://localhost:8080"
    val apiUrl = "$baseUrl/api"
}

fun main() {
    val container = document.getElementById("root")

    val app = App.create()

    if (container == null) {
        val errorRoot = document.createElement("div")

        errorRoot.textContent = "Error: Could not find root element."

        document.body?.appendChild(errorRoot)

        return
    }

    createRoot(container).render(app)
}

val UserContext = createContext<Pair<User?, (User?) -> Unit>?>(null)

fun persistUserToLocalStorage(user: User?) {
    if (user == null) {
        window.localStorage.removeItem("user")
        return
    }
    window.localStorage.setItem("user", CommonSerializerModule.json.encodeToString(user))
}

fun retrieveUserFromLocalStorage(): User? {
    val user = window.localStorage.getItem("user") ?: return null

    return CommonSerializerModule.json.decodeFromString(user)
}

val App = FC<Props> {
    val (user, setUser) = useState<User?>(null)

    useEffectOnce {
        val savedUser = retrieveUserFromLocalStorage()

        if (savedUser != null) {
            setUser(savedUser)
        }
    }

    UserContext.Provider(user to {
        setUser(it)
        persistUserToLocalStorage(it)
    }) {
        child(Navigation.create())
    }
}
