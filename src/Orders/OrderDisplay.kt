package orders

import kotlinx.html.HTMLTag
import kotlinx.html.classes
import react.*
import react.dom.RDOMBuilder
import react.dom.div
import react.dom.tag
import store.Order
import textFit
import kotlin.browser.window
import kotlin.js.Date

interface OrderDisplayProps : RProps {
    var orderNumber: Int
    var completedTime: Double
}

interface OrderDisplayState : RState {
    var age: Int
}

inline fun RBuilder.custom(tagName: String, block: RDOMBuilder<HTMLTag>.() -> Unit): ReactElement = tag(block) {
    HTMLTag(tagName, it, mapOf(), null, true, false) // I dont know yet what the last 3 params mean... to lazy to look it up
}

fun RBuilder.emptyOrder() = div("order-display") { textFit { +"No Orders Yet" } }

class OrderDisplay(props: OrderDisplayProps) : RComponent<OrderDisplayProps, OrderDisplayState>(props) {

    private val formattedOrderNumber
        get() =
            props.orderNumber.toString().padStart(3, '0')

    private var timerID: Int? = null

    override fun OrderDisplayState.init(props: OrderDisplayProps) {
        age = ((Date.now() - props.completedTime) / 1000).toInt()
    }

    override fun componentWillMount() {
        timerID = window.setInterval({
            setState { age += 1 }
            if (state.age > 5) timerID?.also { window.clearInterval(timerID!!) }
        }, 1000)
    }

    override fun componentWillUnmount() {
        timerID?.also { window.clearInterval(timerID!!) }
    }

    override fun RBuilder.render() {
        div {
            attrs {
                classes = setOf("order-display", "col-3")
                if (state.age <= 5) { classes += "flashit" }
            }

            textFit {
                attrs {
                    mode = "single"
                }
                +formattedOrderNumber
            }
        }
    }
}

fun RBuilder.orderDisplay(order: Order) = child(OrderDisplay::class) {
    with(this.attrs) {
        orderNumber = order.orderNumber
        completedTime = order.completedTime
        key = order.completedTime.toString()
    }
}
