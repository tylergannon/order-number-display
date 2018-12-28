package Orders

import Store.Order
import react.*
import react.dom.div
import kotlin.browser.window
import kotlin.js.Date
import kotlinx.html.HTMLTag
import kotlinx.html.classes
import react.dom.RDOMBuilder
import react.dom.tag
import textFit

interface OrderDisplayProps : RProps {
    var orderNumber: Int
    var completedTime: Date
}

interface OrderDisplayState : RState {
    var age: Int
}

inline fun RBuilder.custom(tagName: String, block: RDOMBuilder<HTMLTag>.() -> Unit): ReactElement = tag(block) {
    HTMLTag(tagName, it, mapOf(), null, true, false) // I dont know yet what the last 3 params mean... to lazy to look it up
}

class EmptyOrder : RComponent<RProps, RState>() {
    override fun RBuilder.render() {
        div("order-display") {
            textFit { +"No Orders Yet" }
        }
    }
}

class OrderDisplay(props: OrderDisplayProps) : RComponent<OrderDisplayProps, OrderDisplayState>(props) {
    override fun OrderDisplayState.init(props: OrderDisplayProps) {
        age = ((Date.now() - props.completedTime.getTime())/1000).toInt()
    }

    var timerID: Int? = null

    override fun componentDidMount() {
        timerID = window.setInterval({
            // actually, the operation is performed on a state's copy, so it stays effectively immutable
            setState { age += 1 }
        }, 1000)
    }

    override fun componentWillUnmount() {
        window.clearInterval(timerID!!)
    }

    override fun componentWillReceiveProps(nextProps: OrderDisplayProps) {
        setState {
            age = ((Date.now() - props.completedTime.getTime())/1000).toInt()
        }
    }

    private val formattedOrderNumber: String get() = props.orderNumber.toString().padStart( 3, '0')

    override fun RBuilder.render() {
        div {
            attrs {
                classes = setOf("order-display")
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
    attrs.orderNumber = order.orderNumber
    attrs.completedTime = order.completedTime
    attrs.key = order.orderNumber.toString()
}
