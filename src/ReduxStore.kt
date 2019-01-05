//package Store
//
//import kotlinx.serialization.SerialId
//import react.RState
//import redux.*
//import kotlin.browser.window
//import kotlinx.serialization.Serializable
//import kotlin.browser.localStorage
//import kotlinx.serialization.json.JSON
//
//import kotlin.js.Date
//
//private val ORDERS_IN_QUEUE = 20
//
//enum class OrderArea(val rgb: String) {
//    Red("FF0000"), Blue("0000FF");
//
//    fun next() = if (this == Red) Blue else Red
//
//    companion object {
//        val initialState: OrderArea = Red
//    }
//}
//
//data class Order(val orderNumber: Int, val completedTime: Date)
//
//@Serializable
//data class AppState (@SerialId(1) val redOrders: List<Order> = listOf(),
//                     @SerialId(2) val blueOrders: List<Order> = listOf(),
//                     @SerialId(3) val currentColor: OrderArea = OrderArea.initialState,
//                     @SerialId(4) val orderNumberEntry: Int? = null,
//                     @SerialId(5) val orderNumberValid: Boolean = false) : RState
//
//
///******************************************************************************
// * ACTIONS
// *****************************************************************************/
//
//class NewOrderAction(val orderNumber: Int, val orderDate: Date): RAction
//class OrderNumberEntryChangeAction(val orderNumber: Int?): RAction
//class ChangeSidesAction : RAction
//
///******************************************************************************
// * ACTIONS
// *****************************************************************************/
//
//
//
//private fun addOrderToList(order: Order, list: List<Order>) =
//        list.plus(order).run { dropWhile { size >= ORDERS_IN_QUEUE } }
//
//private fun orderNumberValidator(number: Int?, appState: AppState) = (number ?: 0).let { it < 1000 }
//
//private fun storeState(state: AppState) = localStorage.setItem("state", JSON.stringify(AppState.serializer(), appStore.getState()))
//
//
//fun appReducer(state: AppState, action: RAction) = when(action) {
//
//    is NewOrderAction -> {
//
//        val newOrder = Order(action.orderNumber, action.orderDate)
//
//        if (state.currentColor == OrderArea.Red)
//            state.copy(redOrders = addOrderToList(newOrder, state.redOrders),
//                    currentColor = OrderArea.Blue,
//                    orderNumberEntry = null)
//        else
//            state.copy(blueOrders = addOrderToList(newOrder, state.blueOrders),
//                    currentColor = OrderArea.Red,
//                    orderNumberEntry = null)
//    }
//    is OrderNumberEntryChangeAction -> {
//        state.copy(orderNumberEntry = action.orderNumber, orderNumberValid = orderNumberValidator(action.orderNumber, state))
//    }
//    else -> state
//}
//
//
//
//val appStore = createStore<AppState, RAction, WrapperAction>(::appReducer, AppState(),
//        window.asDynamic().__REDUX_DEVTOOLS_EXTENSION_COMPOSE__(kotlinext.js.js {  })(rEnhancer<AppState>()))
//
