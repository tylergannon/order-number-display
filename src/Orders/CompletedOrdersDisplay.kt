package orders

import grid.column
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
import react.redux.rConnect
import redux.RAction
import redux.WrapperAction
import store.AppState
import store.DisplayOrder
import store.Order
import styled.StyleSheet
import styled.css
import styled.styledDiv
import textFit

interface CompletedOrdersDisplayProps : CompletedOrdersStateProps, WrappedCompletedOrdersDisplayProps

interface CompletedOrdersStateProps : RProps {
    var redOrders: List<Order>
    var blueOrders: List<Order>
    var displayOrder: DisplayOrder
}

interface WrappedCompletedOrdersDisplayProps : RProps

interface CompletedOrdersDisplayState : RState

object OrderDisplayStyle : StyleSheet("OrderDisplay") {
    val wrapper by css {
        children {
            paddingLeft = 16.px
            paddingRight = 16.px
        }
    }
}

class CompletedOrdersDisplay : RComponent<CompletedOrdersDisplayProps, CompletedOrdersDisplayState>() {
    private val mainHeight: Int = 80

    private val leftOrders
        get() = if (props.displayOrder == DisplayOrder.BR)
            props.blueOrders else props.redOrders

    private val rightOrders
        get() = if (props.displayOrder == DisplayOrder.BR)
            props.redOrders else props.blueOrders

    override fun RBuilder.render() {
        container {
            css.height = 100.pct
            columns {
                css.height = (100 - mainHeight).pct
                column(6, setOf(props.displayOrder.left.bg)) {
                    textFit {
                        attrs.mode = "single"
                        icon("icon-arrow-left")
                        +"Left Side"
                    }
                }
                column(6, setOf(props.displayOrder.right.bg)) {
                    textFit {
                        attrs.mode = "single"
                        +"Right Side"
                        icon("icon-arrow-right")
                    }
                }
            }
            columns {
                css.height = mainHeight.pct
                column(6, setOf(props.displayOrder.left.fg)) {
                    css {
                        height = 100.pct
                        border(16.px, BorderStyle.solid, Color.currentColor)
                    }
                    renderOrders(leftOrders)
                }
                column(6, setOf(props.displayOrder.right.fg)) {
                    css {
                        height = 100.pct
                        border(16.px, BorderStyle.solid, Color.currentColor)
                    }
                    renderOrders(rightOrders)
                }
            }
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

val connectedCompletedOrdersDisplay: RClass<WrappedCompletedOrdersDisplayProps> =
        rConnect<AppState, RAction, WrapperAction, WrappedCompletedOrdersDisplayProps,
                CompletedOrdersStateProps, RProps, CompletedOrdersDisplayProps>({ state, _ ->
            redOrders = state.redOrders
            blueOrders = state.blueOrders
            displayOrder = state.insideDisplayOrder
        }, {_, _ ->})(CompletedOrdersDisplay::class.js as RClass<CompletedOrdersDisplayProps>)
