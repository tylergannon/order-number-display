package orders

import kotlinx.html.DIV
import react.*
import react.dom.RDOMBuilder
import react.dom.div
import store.Order
import store.appStore

interface CompletedOrdersDisplayProps: RProps {
    var redOrders: List<Order>
    var blueOrders: List<Order>
}

class CompletedOrdersDisplay : RComponent<CompletedOrdersDisplayProps, RState>() {

    var unsubscribe: () -> Unit = {}

    override fun RBuilder.render() {
        div("completed-orders-display fullheight") {
            div("order-list red-list", renderOrders(props.redOrders))
            div("order-list blue-list", renderOrders(props.blueOrders))
        }
    }

    private fun renderOrders(orders: List<Order>): RDOMBuilder<DIV>.() -> Unit {
        return fun RDOMBuilder<DIV>.() {
            if (orders.isEmpty()) { child(EmptyOrder::class) {} }
            else orders.sortedBy { it.orderNumber }.forEach { orderDisplay(it) }
        }
    }
}

fun RBuilder.completedOrdersDisplay(redOrders: List<Order>, blueOrders: List<Order>) = child(CompletedOrdersDisplay::class) {
    attrs.blueOrders = blueOrders
    attrs.redOrders = redOrders
}
