package orders

import grid.columns
import grid.container
import grid.icon
import kotlinx.css.BorderStyle
import kotlinx.css.Color
import kotlinx.css.pct
import kotlinx.css.properties.border
import kotlinx.css.px
import kotlinx.html.DIV
import kotlinx.html.classes
import react.*
import react.dom.RDOMBuilder
import react.dom.div
import react.redux.rConnect
import redux.RAction
import redux.WrapperAction
import store.AppState
import store.Order
import store.OrderArea
import styled.StyleSheet
import styled.css
import styled.styledDiv
import textFit

interface CompletedOrdersDisplayProps: RProps {
    var redOrders: List<Order>
    var blueOrders: List<Order>
    var message: String
}

object OrderDisplayStyle : StyleSheet("OrderDisplay") {
    val wrapper by css {
        children {
            paddingLeft = 16.px
            paddingRight = 16.px
        }
    }
}

class CompletedOrdersDisplay : RComponent<CompletedOrdersDisplayProps, RState>() {
    private val mainHeight: Int = 80

    override fun RBuilder.render() {
        container {
            css.height = 100.pct
            columns {
                css.height = (100 - mainHeight).pct
                div("${OrderArea.Red.bg} col-6") {
                    textFit {
                        attrs.mode = "single"
                        icon("icon-arrow-left")
                        +"Left Side"
                    }
                }
                div("${OrderArea.Blue.bg} col-6") {
                    textFit {
                        attrs.mode = "single"
                        +"Right Side"
                        icon("icon-arrow-right")
                    }
                }
            }
            columns {
                css.height = mainHeight.pct
                styledDiv {
                    attrs.classes = setOf("red-list", "col-6", OrderArea.Red.fg)
                    css.height = 100.pct
                    css.border(16.px, BorderStyle.solid, Color.currentColor)
                    renderOrders(props.redOrders)
                }
                styledDiv {
                    attrs.classes = setOf("blue-list", "col-6", OrderArea.Blue.fg)
                    css.border(16.px, BorderStyle.solid, Color.currentColor)
                    renderOrders(props.blueOrders)
                }
            }
//            columns {
//                styledDiv {
//                    attrs.classes = setOf("col-12")
//                    if (props.message.isNotEmpty())
//                        css.height = 60.px
//                    textFit {
//                        attrs.mode = "single"
//                        +props.message
//                    }
//                }
//            }
        }
    }

    private fun RDOMBuilder<DIV>.renderOrders(orders: List<Order>) {
        container {
            styledDiv {
                attrs { classes = setOf("columns") }
                css {
                    +OrderDisplayStyle.wrapper
                }
                if (orders.isEmpty()) emptyOrder()
                else orders.sortedBy { it.orderNumber }.forEach { orderDisplay(it) }
            }
        }
    }
}

val connectedCompletedOrdersDisplay: RClass<RProps> =
        rConnect<AppState, RAction, WrapperAction, RProps,
                    CompletedOrdersDisplayProps, RProps, CompletedOrdersDisplayProps>({state, _ ->
            redOrders = state.redOrders
            blueOrders = state.blueOrders
            message = state.message
        }, {_, _ ->})(CompletedOrdersDisplay::class.js as RClass<CompletedOrdersDisplayProps>)
