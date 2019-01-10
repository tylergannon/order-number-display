package app

import kotlinx.css.pct
import orders.connectedCompletedOrdersDisplay
import orders.connectedOrderNumberForm
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import store.Order
import store.OrderArea
import styled.styledDiv

interface InternalAppState : RState {
    var orderNumberEntry: Int?
    var currentColor: OrderArea
    var orderNumberValid: Boolean
    var redOrders: List<Order>
    var blueOrders: List<Order>
}

class App : RComponent<RProps, InternalAppState>() {
    override fun RBuilder.render() {
        styledDiv {
            css.height = 85.pct
            connectedCompletedOrdersDisplay {}
        }
        styledDiv {
            css.height = 15.pct
            connectedOrderNumberForm {}
        }
    }
}

fun RBuilder.app() = child(App::class) {}
