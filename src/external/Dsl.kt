import external.NewWindow
import external.TextFit
import external.TextFitMode
import external.TextFitProps
import react.RBuilder
import react.RHandler
import react.RProps

fun RBuilder.newWindow(handler: RHandler<RProps>) = child(NewWindow::class, handler)

fun RBuilder.textFit(min: Int = 1, max: Int = 1, mode: TextFitMode = TextFitMode.multi, handler: RHandler<TextFitProps>)
        = child(TextFit::class, handler)

