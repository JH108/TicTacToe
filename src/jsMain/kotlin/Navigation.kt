import csstype.*
import emotion.react.css
import features.user.Profile
import me.jesse.models.Game
import me.jesse.models.User
import react.*
import react.dom.html.ReactHTML.div

external interface NavigationProps : Props {
    // ? what goes here ?
}

val Navigation = FC<NavigationProps> { props ->
    div {
        css {
            display = Display.flex
            flexDirection = FlexDirection.column
            justifyContent = JustifyContent.center
            alignItems = AlignItems.center
            height = 100.vh
        }
    }
}
