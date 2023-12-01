package features.user

import csstype.*
import emotion.react.css
import react.FC
import react.Props
import react.dom.html.InputType
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.form
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.section

data class OnboardingState(
    val username: String,
    val firstName: String,
    val lastName: String
)

val Onboarding = FC<Props> {

    section {
        css {
            display = Display.flex
            flexDirection = FlexDirection.column
            justifyContent = JustifyContent.center
            alignItems = AlignItems.center
            height = 100.pct
            width = 100.pct
        }

        form {
            css {
                display = Display.flex
                flexDirection = FlexDirection.column
                justifyContent = JustifyContent.center
                alignItems = AlignItems.center
                height = 100.pct
                width = 100.pct
            }
            input {
                // username
                type = InputType.text

            }
            input {
                // first name

            }
            input {
                // last name

            }
        }
    }
}