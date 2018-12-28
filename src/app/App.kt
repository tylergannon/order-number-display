package app

import Orders.OrderNumberForm
import Orders.completedOrdersDisplay
import Store.*
import react.*
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
                orderNumberValid = appState.orderNumberValid
                orderNumberEntry = appState.orderNumberEntry
            }
        }
    }

    override fun componentWillUnmount() {
        unsubscribe()
        unsubscribe = {}
    }

    override fun RBuilder.render() {
        div("control-orders-list") {
            completedOrdersDisplay()
        }

//        newWindow {
//            completedOrdersDisplay()
//        }
        child(OrderNumberForm::class) {
            attrs.orderNumberEntry = state.orderNumberEntry
            attrs.orderNumberValid = state.orderNumberValid
            attrs.currentColor = state.currentColor
            attrs.orderNumberEntryChanged = fun(orderNumber: Int?) {
                appStore.dispatch(OrderNumberEntryChangeAction(orderNumber))
            }
            attrs.addOrder = fun(orderNumber: Int?) {
                if (orderNumber == null)
                    appStore.dispatch(ChangeSidesAction())
                else
                    appStore.dispatch(NewOrderAction(orderNumber, Date(Date.now())))
            }
        }

    }
}

fun RBuilder.app() = child(App::class) {}
