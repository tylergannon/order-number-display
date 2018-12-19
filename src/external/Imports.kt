package external

import react.*
import redux.*

@JsModule("react-new-window")
external class NewWindow : Component<RProps, RState> {
    override fun render(): ReactElement?
}



enum class TextFitMode{ single, multi }

interface TextFitProps : RProps {
    var text: String
    var min: Int
    var max: Int
    var mode: String
}


//@JsModule("remote-redux-devtools")
//external fun <S>composeWithDevtools(config: dynamic): (Enhancer<S, Action, Action, RAction, WrapperAction>) -> Enhancer<S, Action, Action, RAction, WrapperAction>
//
