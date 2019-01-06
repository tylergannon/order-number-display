package app

import orders.OrderNumberForm
import orders.connectedCompletedOrdersDisplay
import orders.connectedOrderNumberForm
import react.*
import react.dom.div
import store.*

interface InternalAppState : RState {
    var orderNumberEntry: Int?
    var currentColor: OrderArea
    var orderNumberValid: Boolean
    var redOrders: List<Order>
    var blueOrders: List<Order>
}

class App : RComponent<RProps, InternalAppState>() {
    override fun RBuilder.render() {
        div("control-orders-list") {
            connectedCompletedOrdersDisplay {}
        }
        connectedOrderNumberForm {}
    }
}

fun RBuilder.app() = child(App::class) {}
