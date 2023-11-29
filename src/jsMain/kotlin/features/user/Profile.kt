package features.user

import react.FC
import react.Props

external interface ProfileProps : Props {
    var name: String
}

val Profile = FC<ProfileProps> {

}