package orders

import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onSubmitFunction
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.*
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
    var addOrder: (orderNumber: Int) -> Unit
    var clearDisplay: () -> Unit
    var changeSides: () -> Unit
}

internal class OrderNumberForm(props: OrderNumberFormProps) : RComponent<OrderNumberFormProps, RState>(props) {
    override fun RBuilder.render() {
        div("${props.currentColor.name} control-container") {
            form(action = null, classes = "control") {
                attrs {
                    onSubmitFunction = {
                        val orderNumberEntry = props.orderNumberEntry
                        if (orderNumberEntry == null) props.changeSides()
                        else props.addOrder(orderNumberEntry)
                        it.preventDefault()
                    }
                }

                span {
                    input(type = InputType.number, name = "order_number", classes = "number-input") {
                        attrs {
                            value = props.orderNumberEntry.toString()
                            onChangeFunction = {
                                val inputElement = it.target as HTMLInputElement
                                props.orderNumberEntryChanged(inputElement.valueAsNumber.toInt())
                            }
                        }
                    }
                }

                span {
                    div {
                        input(type = InputType.submit, name = "submit_number") {
                            attrs {
                                value = "Submit"
                                disabled = !props.orderNumberValid
                            }
                        }
                    }
                    div {
                        button {
                            attrs.onClickFunction = {e ->
                                e.preventDefault()
                                props.changeSides()
                            }
                            +"Change Sides"
                        }
                    }

                    div {
                        button {
                            attrs.onClickFunction = {e ->
                                e.preventDefault()
                                props.clearDisplay()
                            }
                            +"Clear Display"
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
}

internal interface DispatchProps: RProps {
    var orderNumberEntryChanged: (orderNumber: Int?) -> Unit
    var addOrder: (orderNumber: Int) -> Unit
    var clearDisplay: () -> Unit
    var changeSides: () -> Unit
}

private val mapStateToProps: StateProps.(AppState, RProps) -> Unit = {state, _ ->
    orderNumberEntry = state.orderNumberEntry
    orderNumberValid = state.orderNumberValid
    currentColor = state.currentColor
}

private val mapDispatchToProps: DispatchProps.((RAction) -> WrapperAction, RProps) -> Unit = {dispatch, _ ->
    orderNumberEntryChanged = { orderNumber -> dispatch(OrderNumberEntryChangeAction(orderNumber)) }
    addOrder = { orderNumber -> dispatch(NewOrderAction(orderNumber, Date.now())) }
    clearDisplay = { dispatch(ClearStateAction()) }
    changeSides = { dispatch(ChangeSidesAction()) }
}

val connectedOrderNumberForm: RClass<RProps> = rConnect<AppState, RAction, WrapperAction, RProps,
        StateProps, DispatchProps, OrderNumberFormProps>(mapStateToProps, mapDispatchToProps)(
        OrderNumberForm::class.js as RClass<OrderNumberFormProps>
)
