package external

import react.Component
import react.RProps
import react.RState
import react.ReactElement

interface NewWindowProps : RProps {
    //    var url: String?
//    var name: String?
//    var title: String?
    var onUnload: (() -> Unit)?
    var onBlock: (() -> Unit)?
    var features: dynamic?
}

@JsModule("react-new-window")
external class NewWindow : Component<NewWindowProps, RState> {
    override fun render(): ReactElement?
}

enum class TextFitMode{ single, multi }

interface TextFitProps : RProps {
    var text: String
    var min: Int
    var max: Int
    var mode: String
    var onReady: () -> Unit
}
