package orders

import kotlinx.html.DIV
import react.*
import react.dom.RDOMBuilder
import react.dom.div
import react.redux.rConnect
import redux.RAction
import redux.WrapperAction
import store.AppState
import store.Order
import store.appStore

interface CompletedOrdersDisplayProps: RProps {
    var redOrders: List<Order>
    var blueOrders: List<Order>
}

class CompletedOrdersDisplay : RComponent<CompletedOrdersDisplayProps, RState>() {

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

val connectedCompletedOrdersDisplay: RClass<RProps> =
        rConnect<AppState, RAction, WrapperAction, RProps,
                    CompletedOrdersDisplayProps, RProps, CompletedOrdersDisplayProps>({state, _ ->
            redOrders = state.redOrders
            blueOrders = state.blueOrders
        }, {_, _ ->})(CompletedOrdersDisplay::class.js as RClass<CompletedOrdersDisplayProps>)
