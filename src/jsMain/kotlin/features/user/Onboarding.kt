package features.user

import ClientConfiguration
import UserContext
import csstype.*
import emotion.react.css
import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import mainScope
import me.jesse.models.User
import me.jesse.serializers.CommonSerializerModule
import me.jesse.tictactoe.UIRoute
import org.w3c.fetch.Headers
import org.w3c.fetch.Request
import org.w3c.fetch.RequestInit
import react.FC
import react.Props
import react.dom.html.ButtonType
import react.dom.html.InputType
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.form
import react.dom.html.ReactHTML.h4
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.section
import react.router.useNavigate
import react.useContext
import react.useState

@Serializable
data class OnboardingState(
    val username: String,
    val firstName: String,
    val lastName: String
)

val Onboarding = FC<Props> {
    var userState by useState(OnboardingState("", "", ""))
    val userContext = useContext(UserContext)
    val navigate = useNavigate()
    var error by useState<String?>(null)
    var isLoading by useState(false)

    section {
        css {
            display = Display.flex
            flexDirection = FlexDirection.column
            justifyContent = JustifyContent.center
            alignItems = AlignItems.center
            height = 100.pct
            width = 100.pct
        }

        if (isLoading) {
            p {
                +"Loading..."
            }
        }

        error?.let {
            p {
                css {
                    color = Color("#ff0000")
                }

                +it
            }
        }

        form {
            css {
                marginTop = 25.px
                padding = 10.px
                display = Display.flex
                flexDirection = FlexDirection.column
                justifyContent = JustifyContent.center
                alignItems = AlignItems.center
                height = 100.pct
                backgroundColor = Color("#f5f5f5")
                borderRadius = 10.px
            }

            onSubmit = { formEvent ->
                formEvent.preventDefault()

                isLoading = true

                mainScope.launch {
                    val response = createUser(userState)

                    if (response.status.isSuccessCode()) {
                        response.user?.let {
                            userContext?.second?.invoke(it)
                        }
                        navigate(UIRoute.Profile.path)
                    } else {
                        error = response.error
                    }
                }

                isLoading = false
            }

            h4 {
                +"Create a New User"
            }

            input {
                css {
                    margin = 10.px
                    padding = 10.px
                }

                type = InputType.text
                title = "Username"
                placeholder = "Enter a username"
                name = "username"
                value = userState.username
                onChange = { event ->
                    val target = event.target

                    userState = userState.copy(username = target.value)
                }
            }
            input {
                css {
                    margin = 10.px
                    padding = 10.px
                }

                type = InputType.text
                title = "First Name"
                placeholder = "Enter your first name"
                name = "firstName"
                value = userState.firstName
                onChange = { event ->
                    val target = event.target

                    userState = userState.copy(firstName = target.value)
                }
            }
            input {
                css {
                    margin = 10.px
                    padding = 10.px
                }

                type = InputType.text
                title = "Last Name"
                placeholder = "Enter your last name"
                name = "lastName"
                value = userState.lastName
                onChange = { event ->
                    val target = event.target

                    userState = userState.copy(lastName = target.value)
                }
            }

            button {
                css {
                    margin = 10.px
                    padding = 10.px
                    backgroundColor = Color("#a5f5a5")
                }

                type = ButtonType.submit
                disabled = isLoading

                +"Create User"
            }
        }
    }
}

data class CreateUserResponse(
    val status: Int,
    val user: User?,
    val error: String?
)

suspend fun createUser(user: OnboardingState): CreateUserResponse {
    val response = window.fetch(
        Request(
            input = ClientConfiguration.apiUrl + "/register",
            init = RequestInit(
                method = "POST",
                headers = Headers().apply {
                    set("Content-Type", "application/json")
                },
                body = CommonSerializerModule.json.encodeToString(user)
            )
        )
    ).await()

    return when (response.status.toInt()) {
        200 -> {
            val body = response.text().await()
            val newUser = CommonSerializerModule.json.decodeFromString<User>(body)

            CreateUserResponse(
                response.status.toInt(),
                newUser,
                null
            )
        }

        else -> {
            val body = response.text().await().takeIf { it.isNotEmpty() } ?: "Unknown Error"

            CreateUserResponse(
                response.status.toInt(),
                null,
                body
            )
        }
    }
}

fun Int.isSuccessCode(): Boolean {
    return this in 200..299
}
