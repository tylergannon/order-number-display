package Orders

import Store.Order
import Store.appStore
import kotlinx.html.DIV
import react.*
import react.dom.RDOMBuilder
import react.dom.div

interface CompletedOrdersDisplayState: RState {
    var redOrders: List<Order>
    var blueOrders: List<Order>
}

class CompletedOrdersDisplay : RComponent<RProps, CompletedOrdersDisplayState>() {
    override fun CompletedOrdersDisplayState.init(props: RProps) {
        redOrders = listOf()
        blueOrders = listOf()
    }

    var unsubscribe: () -> Unit = {}

    override fun componentWillMount() {
        setState {
            redOrders = listOf()
            blueOrders = listOf()
        }
        unsubscribe = appStore.subscribe {
            val currentState = appStore.getState()
            setState {
                redOrders = currentState.redOrders
                blueOrders = currentState.blueOrders
            }
        }
    }

    override fun componentWillUnmount() {
        unsubscribe()
        unsubscribe = {}
    }

    override fun RBuilder.render() {
        div("completed-orders-display fullheight") {
            div("order-list red-list", renderOrders(state.redOrders))
            div("order-list blue-list", renderOrders(state.blueOrders))
        }
    }

    private fun renderOrders(orders: List<Order>): RDOMBuilder<DIV>.() -> Unit {
        return fun RDOMBuilder<DIV>.() {
            if (orders.isEmpty()) { child(EmptyOrder::class) {} }
            else orders.sortedBy { it.orderNumber }.forEach { orderDisplay(it) }
        }
    }
}

fun RBuilder.completedOrdersDisplay() = child(CompletedOrdersDisplay::class) {}
