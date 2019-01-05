package app

import orders.OrderNumberForm
import orders.completedOrdersDisplay
import react.*
import react.dom.div
import store.*
import kotlin.js.Date

interface InternalAppState : RState {
    var orderNumberEntry: Int?
    var currentColor: OrderArea
    var orderNumberValid: Boolean
}

class App : RComponent<RProps, InternalAppState>() {
    var unsubscribe: () -> Unit = {}

    private fun mapStoreToState(state: AppState) = setState {
        currentColor = state.currentColor
        orderNumberValid = state.orderNumberValid
        orderNumberEntry = state.orderNumberEntry
    }

    override fun componentWillMount() {
        mapStoreToState(appStore.getState())
        unsubscribe = appStore.subscribe {
            mapStoreToState(appStore.getState())
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
                    appStore.dispatch(NewOrderAction(orderNumber, Date.now()))
            }
        }
    }
}

fun RBuilder.app() = child(App::class) {}
