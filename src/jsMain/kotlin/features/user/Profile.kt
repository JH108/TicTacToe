package features.user

import UserContext
import components.Card
import csstype.*
import emotion.react.css
import me.jesse.models.User
import react.FC
import react.Props
import react.create
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.section
import react.useContext

external interface ProfileProps : Props

external interface UserProfileProps : Props {
    var userDetails: User
}

val UserProfile = FC<UserProfileProps> { props ->
    section {
        css {
            display = Display.flex
            flexDirection = FlexDirection.column
            justifyContent = JustifyContent.center
            alignItems = AlignItems.center
            height = 100.pct
            width = 100.pct
        }

        Card {
            title = "User Details"

            p {
                +"Username: ${props.userDetails.username}"
            }
            p {
                +"First Name: ${props.userDetails.firstName}"
            }
            p {
                +"Last Name: ${props.userDetails.lastName}"
            }
        }
    }
}

val Profile = FC<ProfileProps> {
    val user = useContext(UserContext)

    div {
        css {
            display = Display.flex
            flexDirection = FlexDirection.column
            justifyContent = JustifyContent.center
            alignItems = AlignItems.center
            height = 100.pct
            width = 100.pct
        }

        user?.first?.let {
            UserProfile {
                userDetails = it
            }
        }

        if (user?.first == null) {
            child(Onboarding.create())
        }
    }
}
