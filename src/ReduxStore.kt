
package store

import kotlinext.js.jsObject
import react.RState
import redux.*
import kotlin.browser.localStorage
import kotlin.js.Date

private val ORDERS_IN_QUEUE = 16

enum class OrderArea(val rgb: String) {
    Red("FF0000"), Blue("0000FF");

    fun next() = if (this == Red) Blue else Red

    companion object {
        val initialState: OrderArea = Red
    }
}

interface OrderJSON {
    var orderNumber: Int
    var completedTime: Double
}

interface AppStateJSON {
    var redOrders: Array<OrderJSON>
    var blueOrders: Array<OrderJSON>
    var currentColor: String
    var orderNumberEntry: Int?
    var orderNumberValid: Boolean
}

data class Order(val orderNumber: Int, val completedTime: Double)

fun List<Order>.plusBounded(order: Order, bound: Int = ORDERS_IN_QUEUE) = plus(order).run {
    if (size > bound) {
        console.log(this, size, bound)
        drop(size - bound)
    } else this
}

data class AppState (val redOrders: List<Order> = listOf(),
                     val blueOrders: List<Order> = listOf(),
                     val currentColor: OrderArea = OrderArea.initialState,
                     val orderNumberEntry: Int? = null,
                     val orderNumberValid: Boolean = false) : RState {

    fun plusOrder(order: Order) : AppState = copy(
            redOrders = if (currentColor == OrderArea.Red) redOrders.plusBounded(order) else redOrders,
            blueOrders = if (currentColor == OrderArea.Blue) blueOrders.plusBounded(order) else blueOrders,
            currentColor = currentColor.next(),
            orderNumberEntry = null
    )

    companion object {
        val initialValue = AppState()
        fun fromJSON(state: AppStateJSON) = AppState(
                redOrders = state.redOrders.map { Order(it.orderNumber, it.completedTime) },
                blueOrders = state.blueOrders.map { Order(it.orderNumber, it.completedTime) },
                currentColor = OrderArea.valueOf(state.currentColor),
                orderNumberEntry = state.orderNumberEntry,
                orderNumberValid = state.orderNumberValid
        )
        fun deserialize(json: String) = fromJSON(JSON.parse(json))
    }

    fun serialize() = JSON.stringify(toJSON())

    fun toJSON(): AppStateJSON {
        val me = this
        val poop = { it: Order ->
            jsObject<OrderJSON> {
                orderNumber = it.orderNumber
                completedTime = it.completedTime
            }
        }
        return jsObject {
            redOrders = me.redOrders.map(poop).toTypedArray()
            blueOrders = me.blueOrders.map(poop).toTypedArray()
            currentColor = me.currentColor.name
            orderNumberEntry = me.orderNumberEntry
            orderNumberValid = me.orderNumberValid
        }
    }

}


/******************************************************************************
 * ACTIONS
 *****************************************************************************/

class NewOrderAction(val orderNumber: Int, val orderDate: Double): RAction
class OrderNumberEntryChangeAction(val orderNumber: Int?): RAction
class ChangeSidesAction : RAction
class ClearStateAction: RAction

/**----------------------------------------------------------------------------
END ACTIONS
----------------------------------------------------------------------------*/

private fun orderNumberValidator(number: Int?, appState: AppState) = (number ?: 0).let { it < 1000 }

private fun loadState(state: AppState = AppState.initialValue) =
        localStorage.getItem("state")?.let {
            AppState.deserialize(it)
        } ?: state

/******************************************************************************
 * REDUCER
 *****************************************************************************/

val appReducer: Reducer<AppState, RAction> = fun(state: AppState, action: RAction): AppState {

    return when(action) {
        is ClearStateAction     ->  AppState.initialValue
        is ChangeSidesAction    ->  state.copy(currentColor = state.currentColor.next(), orderNumberEntry = null)
        is NewOrderAction       ->
            state.plusOrder(Order(action.orderNumber, action.orderDate))
        is OrderNumberEntryChangeAction ->
            state.copy( orderNumberEntry = action.orderNumber,
                    orderNumberValid = orderNumberValidator(action.orderNumber, state))

        else -> state
    }
}

/**----------------------------------------------------------------------------
END REDUCER
----------------------------------------------------------------------------*/


typealias AppMiddleware = Middleware<AppState, RAction, WrapperAction, RAction, WrapperAction>

val storeStateMiddleware: AppMiddleware = { store -> { next -> { action ->
    next(action).apply {
        localStorage.setItem("state", store.getState().serialize())
    }
} } }

val appStore = createStore(
        appReducer,
        loadState(),
        compose(applyMiddleware(storeStateMiddleware), rEnhancer())
)

//    window.asDynamic().__REDUX_DEVTOOLS_EXTENSION_COMPOSE__(kotlinext.js.js { })(
//    )
