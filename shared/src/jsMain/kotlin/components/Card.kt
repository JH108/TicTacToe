package components

import csstype.*
import emotion.react.css
import react.*
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h4
import react.dom.html.ReactHTML.h6

external interface CardProps : PropsWithChildren {
    var title: String
    var subtitle: String?
    var onClick: (() -> Unit)?
}

val Card = FC<CardProps> { props ->
    div {
        css {
            margin = 10.px
            padding = 10.px
            display = Display.flex
            flexDirection = FlexDirection.column
            justifyContent = JustifyContent.start
            backgroundColor = Color("#f5f5f5")
            borderRadius = 10.px
        }

        props.onClick?.let {
            onClick = {
                it()
            }
        }

        h4 {
            +props.title
        }

        props.subtitle?.let {
            h6 {
                +it
            }
        }

        div {
            css {
                display = Display.flex
                flexDirection = FlexDirection.row
                flexWrap = FlexWrap.wrap
                justifyContent = JustifyContent.spaceEvenly
                alignItems = AlignItems.center
                alignSelf = AlignSelf.start
            }

            child(
                props.children
            )
        }
    }
}
