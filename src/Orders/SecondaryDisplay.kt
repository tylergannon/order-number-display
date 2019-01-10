package orders

import external.NewWindow
import kotlinx.css.pct
import react.*
import redux.RAction
import redux.WrapperAction
import store.AppState
import store.CloseDisplayWindowAction
import store.DisplayWindowBlockedAction
import store.Order
import styled.styledDiv
import textFit


interface SecondaryDisplayStateProps : RProps {
    var redOrders: List<Order>
    var blueOrders: List<Order>
    var message: String
    var displayWindowOpen: Boolean
}

interface SecondaryDisplayDispatchProps : RProps {
    var onUnload: () -> Unit
    var onBlock: () -> Unit
}

interface SecondaryDisplayProps : SecondaryDisplayStateProps, SecondaryDisplayDispatchProps

class SecondaryDisplay : RComponent<SecondaryDisplayProps, RState>() {
    override fun RBuilder.render() {
        if (props.displayWindowOpen) {
            child(NewWindow::class) {
                attrs {
                    onBlock = props.onBlock
                    onUnload = props.onUnload
                }
                styledDiv {
                    css.height = 85.pct
                    css.width = 100.pct
                    child(CompletedOrdersDisplay::class) {
                        attrs.blueOrders = props.blueOrders
                        attrs.redOrders = props.redOrders
                    }
                }
                styledDiv {
                    css.height = 15.pct
                    css.width = 100.pct
                    textFit {
                        attrs.mode = "single"
                        +props.message
                    }
                }
            }
        }
    }
}

val connectedSecondaryDisplay: react.RClass<RProps> =
        (react.redux.rConnect<AppState, RAction, WrapperAction, RProps,
                SecondaryDisplayStateProps, SecondaryDisplayDispatchProps, SecondaryDisplayProps>({ state, _ ->
            redOrders = state.redOrders
            blueOrders = state.blueOrders
            message = state.message
            displayWindowOpen = state.displayWindowOpen
        }, { dispatch, _ ->
            onUnload = { dispatch(CloseDisplayWindowAction()) }
            onBlock = { dispatch(DisplayWindowBlockedAction()) }
        }))(SecondaryDisplay::class.js as RClass<SecondaryDisplayProps>)
