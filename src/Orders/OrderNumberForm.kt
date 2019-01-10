package orders

import grid.column
import grid.columns
import grid.container
import grid.icon2x
import kotlinx.css.VerticalAlign
import kotlinx.css.pct
import kotlinx.css.properties.LineHeight
import kotlinx.css.pt
import kotlinx.css.px
import kotlinx.html.ButtonType
import kotlinx.html.InputType
import kotlinx.html.classes
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onSubmitFunction
import newWindow
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLTextAreaElement
import org.w3c.dom.events.Event
import react.*
import react.dom.textArea
import react.dom.value
import react.redux.rConnect
import redux.RAction
import redux.WrapperAction
import store.*
import styled.css
import styled.styledButton
import styled.styledForm
import styled.styledInput
import kotlin.js.Date

internal interface OrderNumberFormProps : RProps {
    var orderNumberEntry: Int?
    var orderNumberValid: Boolean
    var currentColor: OrderArea
    var orderNumberEntryChanged: (orderNumber: Int?) -> Unit
    var message: String
    var addOrder: (orderNumber: Int) -> Unit
    var clearDisplay: () -> Unit
    var changeSides: () -> Unit
    var changeMessage: (String) -> Unit
}

private val eventHandler: (() -> Unit) -> (Event) -> Unit = { handler ->
    { event ->
        handler()
        event.preventDefault()
    }
}

internal class OrderNumberForm(props: OrderNumberFormProps) : RComponent<OrderNumberFormProps, RState>(props) {
    private val onFormSubmit = { event: Event ->
        val orderNumberEntry = props.orderNumberEntry
        if (orderNumberEntry == null) props.changeSides()
        else props.addOrder(orderNumberEntry)
        event.preventDefault()
    }

    private fun RBuilder.numberInput() {
        styledInput(InputType.number) {
            attrs {
                classes = setOf("number-input")
                value = props.orderNumberEntry.toString()
                onChangeFunction = {
                    val poop = it.target.let {
                        it as HTMLInputElement
                    }.valueAsNumber.toInt()
                    console.log("New value", poop)
                    props.orderNumberEntryChanged(poop)
                }
                autoComplete = false
            }
            css {
                height = 75.px
                fontSize = 50.pt
                width = 100.pct
                lineHeight = LineHeight("75px")
                verticalAlign = VerticalAlign.middle
            }
        }
    }

    private fun RBuilder.submitForm() {
        styledButton {
            attrs {
                type = ButtonType.submit
                classes = setOf("m-1")
                value = "Submit"
                disabled = !props.orderNumberValid
            }
            css {
                borderWidth = 5.px
                borderRadius = 8.px
            }
            icon2x("icon-plus")
        }
    }

    private fun RBuilder.switchButton() {
        styledButton {
            attrs {
                classes = setOf("m-1")
                onClickFunction = eventHandler(props.changeSides)
            }
            css {
                borderWidth = 5.px
                borderRadius = 8.px
            }
            icon2x("icon-resize-horiz")
        }
    }

    private fun RBuilder.openNewWindowButton() {
        styledButton {
            attrs {
                classes = setOf("m-1")
                onClickFunction = eventHandler {
                    newWindow {
                        connectedCompletedOrdersDisplay {}
                    }
                }
            }
            css {
                borderWidth = 5.px
                borderRadius = 8.px
            }
            icon2x("icon-share")
        }
    }

    private fun RBuilder.clearButton() {
        styledButton {
            attrs {
                classes = setOf("m-1")
                onClickFunction = eventHandler(props.clearDisplay)
            }
            css {
                borderWidth = 5.px
                borderRadius = 8.px
            }
            icon2x("icon-delete")
        }
    }

    override fun RBuilder.render() {
        styledForm {
            css.height = 100.pct
            attrs.onSubmitFunction = { event: Event ->
                console.log("On submit", props, appStore.getState())
                val orderNumberEntry = props.orderNumberEntry
                if (orderNumberEntry == null) props.changeSides()
                else props.addOrder(orderNumberEntry)
                event.preventDefault()
            }
            container {
                css.height = 100.pct
                columns {
                    attrs.classes = setOf("py-2", "columns", props.currentColor.bg)
                    css.height = 100.pct
                    column(1) { }
                    column(3) { numberInput() }
                    column(2, setOf("py-2")) {
                        css.verticalAlign = VerticalAlign.middle
                        submitForm()
                        switchButton()
                        clearButton()
                    }
                    column(6) {
                        textArea {
                            attrs.value = props.message
                            attrs.onChangeFunction = { event ->
                                event.target.let { it as HTMLTextAreaElement }.value.run { props.changeMessage(this) }
                            }
                        }
                    }
                }
            }
        }
    }
}

internal interface StateProps: RProps {
    var orderNumberEntry: Int?
    var orderNumberValid: Boolean
    var currentColor: OrderArea
    var message: String
}

internal interface DispatchProps: RProps {
    var orderNumberEntryChanged: (orderNumber: Int?) -> Unit
    var addOrder: (orderNumber: Int) -> Unit
    var clearDisplay: () -> Unit
    var changeSides: () -> Unit
    var changeMessage: (String) -> Unit
}

private val mapStateToProps: StateProps.(AppState, RProps) -> Unit = {state, _ ->
    orderNumberEntry = state.orderNumberEntry
    orderNumberValid = state.orderNumberValid
    currentColor = state.currentColor
    message = state.message
}

private val mapDispatchToProps: DispatchProps.((RAction) -> WrapperAction, RProps) -> Unit = {dispatch, _ ->
    orderNumberEntryChanged = { orderNumber -> dispatch(OrderNumberEntryChangeAction(orderNumber)) }
    addOrder = { orderNumber -> dispatch(NewOrderAction(orderNumber, Date.now())) }
    clearDisplay = { dispatch(ClearStateAction()) }
    changeSides = { dispatch(ChangeSidesAction()) }
    changeMessage = { message -> dispatch(ChangeMessageAction(message)) }
}

val connectedOrderNumberForm: RClass<RProps> = rConnect<AppState, RAction, WrapperAction, RProps,
        StateProps, DispatchProps, OrderNumberFormProps>(mapStateToProps, mapDispatchToProps)(
        OrderNumberForm::class.js as RClass<OrderNumberFormProps>
)
