package orders

import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onSubmitFunction
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.button
import react.dom.div
import react.dom.form
import react.dom.input
import react.redux.rConnect
import redux.RAction
import redux.WrapperAction
import store.*
import kotlin.js.Date

internal interface OrderNumberFormProps : RProps {
    var orderNumberEntry: Int?
    var orderNumberValid: Boolean
    var currentColor: OrderArea
    var orderNumberEntryChanged: (orderNumber: Int?) -> Unit
    var addOrder: (orderNumber: Int?) -> Unit
    var clearDisplay: () -> Unit
}

internal class OrderNumberForm(props: OrderNumberFormProps) : RComponent<OrderNumberFormProps, RState>(props) {
    override fun RBuilder.render() {
        div("${props.currentColor.name} control-container") {
            form(action = null, classes = "control") {
                attrs {
                    onSubmitFunction = {
                        props.addOrder(props.orderNumberEntry)
                        it.preventDefault()
                    }
                }

                input(type = InputType.number, name = "order_number", classes = "number-input") {
                    attrs {
                        value = props.orderNumberEntry.toString()
                        onChangeFunction = {
                            val inputElement = it.target as HTMLInputElement
                            props.orderNumberEntryChanged(inputElement.valueAsNumber.toInt())
                        }
                    }
                }

                input(type = InputType.submit, name = "submit_number") {
                    attrs {
                        value = "Submit"
                        disabled = !props.orderNumberValid
                    }
                }

                button {
                    attrs.onClickFunction = {e ->
                        e.preventDefault()
                        console.log(e)
                        props.clearDisplay()
                    }

                    attrs.name = "Clear Display"
                    attrs.value = "Clear Display"
                    +"Clear Display"
                }
            }
        }
    }
}

internal interface StateProps: RProps {
    var orderNumberEntry: Int?
    var orderNumberValid: Boolean
    var currentColor: OrderArea
}

internal interface DispatchProps: RProps {
    var orderNumberEntryChanged: (orderNumber: Int?) -> Unit
    var addOrder: (orderNumber: Int?) -> Unit
    var clearDisplay: () -> Unit
}

private val mapStateToProps: StateProps.(AppState, RProps) -> Unit = {state, _ ->
    orderNumberEntry = state.orderNumberEntry
    orderNumberValid = state.orderNumberValid
    currentColor = state.currentColor
}

private val mapDispatchToProps: DispatchProps.((RAction) -> WrapperAction, RProps) -> Unit = {dispatch, _ ->
    orderNumberEntryChanged = { orderNumber -> dispatch(OrderNumberEntryChangeAction(orderNumber)) }
    addOrder = { orderNumber ->
        if (orderNumber == null) dispatch(ChangeSidesAction())
        else dispatch(NewOrderAction(orderNumber, Date.now()))
    }
    clearDisplay = { dispatch(ClearStateAction()) }
}

val connectedOrderNumberForm: RClass<RProps> = rConnect<AppState, RAction, WrapperAction, RProps,
        StateProps, DispatchProps, OrderNumberFormProps>(mapStateToProps, mapDispatchToProps)(
        OrderNumberForm::class.js as RClass<OrderNumberFormProps>
)
