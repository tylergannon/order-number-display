package external

import react.*
import react.dom.h2

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

interface FitTextProps : RProps {
    var compressor: Float
    var minFontSize: Float
    var maxFontSize: Float
}

@JsModule("react-fittext")
external class FitText : Component<FitTextProps, RState> {
    override fun render(): ReactElement?
}

fun RBuilder.fitText(str: String, compressor: Float = 1f, minFontSize: Float = 1f, maxFontSize: Float = 100f) {
    child(FitText::class) {
        attrs.compressor = compressor
        attrs.minFontSize = minFontSize
        attrs.maxFontSize = maxFontSize
        h2 { +str }
//        str
    }
}

//@JsModule("remote-redux-devtools")
//external fun <S>composeWithDevtools(config: dynamic): (Enhancer<S, Action, Action, RAction, WrapperAction>) -> Enhancer<S, Action, Action, RAction, WrapperAction>
//
