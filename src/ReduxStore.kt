package Store

import react.RState
import redux.*
import kotlin.browser.window


import kotlin.js.Date

private val ORDERS_IN_QUEUE = 20

enum class OrderArea(val rgb: String) {
    Red("FF0000"), Blue("0000FF");

    fun next() = if (this == Red) Blue else Red

    companion object {
        val initialState: OrderArea = Red
    }
}

data class Order(val orderNumber: Int, val completedTime: Date)

data class AppState (val redOrders: List<Order> = listOf(),
                     val blueOrders: List<Order> = listOf(),
                     val currentColor: OrderArea = OrderArea.initialState) : RState

data class NewOrderAction(val orderNumber: Int, val orderDate: Date): RAction

private fun addOrderToList(order: Order, list: List<Order>) =
        list.plus(order).run { dropWhile { size >= ORDERS_IN_QUEUE } }

fun appReducer(state: AppState, action: RAction) = when(action) {

    is NewOrderAction -> {
        val newOrder = Order(action.orderNumber, action.orderDate)

        if (state.currentColor == OrderArea.Red)
            state.copy(redOrders = addOrderToList(newOrder, state.redOrders), currentColor = OrderArea.Blue)
        else
            state.copy(blueOrders = addOrderToList(newOrder, state.blueOrders), currentColor = OrderArea.Red)
    }
    else -> state
}

val appStore = createStore<AppState, RAction, WrapperAction>(::appReducer, AppState(),
        window.asDynamic().__REDUX_DEVTOOLS_EXTENSION_COMPOSE__(kotlinext.js.js {  })(rEnhancer<AppState>()))

