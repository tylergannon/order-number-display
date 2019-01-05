package orders

import kotlinx.html.DIV
import react.*
import react.dom.RDOMBuilder
import react.dom.div
import store.AppState
import store.Order
import store.appStore


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

    private fun mapStoreToState(state: AppState) = setState {
        blueOrders = state.blueOrders
        redOrders = state.redOrders
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
