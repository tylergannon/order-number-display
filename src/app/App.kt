package app

import Orders.completedOrdersDisplay
import Store.*
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onSubmitFunction
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.form
import react.dom.input
import newWindow
import react.dom.div
import kotlin.js.Date

interface InternalAppState : RState {
    var orderNumberEntry: Int?
    var currentColor: OrderArea
    var orderNumberValid: Boolean
}

class App : RComponent<RProps, InternalAppState>() {
    var unsubscribe: () -> Unit = {}

    override fun componentWillMount() {
        setState {
            currentColor = OrderArea.initialState
            orderNumberEntry = null
            orderNumberValid = false
        }
        unsubscribe = appStore.subscribe {
            val appState = appStore.getState()
            setState {
                currentColor = appState.currentColor
                orderNumberValid = orderNumberValidator(orderNumberEntry, appState)
            }
        }
    }

    private fun orderNumberValidator(number: Int?, appState: AppState) = (number ?: 0).let { it < 100 || it >= 1000 ||
            appState.redOrders.find {
                order -> order.orderNumber == it
            } != null ||
            appState.blueOrders.find {
                order -> order.orderNumber == it
            } != null
    }

    override fun componentWillUnmount() {
        unsubscribe()
        unsubscribe = {}
    }

    override fun RBuilder.render() {
        div("control-orders-list") {
            completedOrdersDisplay()
        }

        newWindow {
            completedOrdersDisplay()
        }

        div("${state.currentColor.name} control-container") {
            form(action = null, classes = "control") {
                attrs {
                    onSubmitFunction = {
                        appStore.dispatch(NewOrderAction(state.orderNumberEntry!!, Date(Date.now())))
                        setState { orderNumberEntry = null }
                        it.preventDefault()
                    }
                }

                input(type = InputType.number, name = "order_number", classes = "number-input") {
                    attrs {
                        value = state.orderNumberEntry.toString()
                        onChangeFunction = {
                            val inputElement = it.target as HTMLInputElement
                            setState { orderNumberEntry = inputElement.valueAsNumber.toInt() }
                        }
                    }
                }

                input(type = InputType.submit, name = "submit_number") {
                    attrs {
                        value = "Submit"
                        disabled = !state.orderNumberValid
                    }
                }
            }
        }
    }
}

fun RBuilder.app() = child(App::class) {}
