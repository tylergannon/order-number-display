package orders

import grid.*
import kotlinx.css.*
import kotlinx.css.properties.LineHeight
import kotlinx.html.ButtonType
import kotlinx.html.InputType
import kotlinx.html.classes
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onSubmitFunction
import kotlinx.html.tabIndex
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLTextAreaElement
import org.w3c.dom.events.Event
import react.*
import react.dom.a
import react.dom.li
import react.dom.ul
import react.dom.value
import react.redux.rConnect
import redux.RAction
import redux.WrapperAction
import store.*
import styled.*
import textFit
import kotlin.js.Date

interface OrderNumberFormProps : RProps {
    var orderNumberEntry: Int?
    var orderNumberValid: Boolean
    var currentColor: OrderArea
    var orderNumberEntryChanged: (orderNumber: Int?) -> Unit
    var message: String
    var displayOrder: DisplayOrder
    var addOrder: (orderNumber: Int) -> Unit
    var clearDisplay: () -> Unit
    var changeSides: () -> Unit
    var openDisplayWindow: () -> Unit
    var changeMessage: (String) -> Unit
    var switchDisplayDirection: () -> Unit
}

private val eventHandler: (() -> Unit) -> (Event) -> Unit = { handler ->
    { event ->
        handler()
        event.preventDefault()
    }
}

class OrderNumberForm(props: OrderNumberFormProps) : RComponent<OrderNumberFormProps, RState>(props) {

    private fun RBuilder.numberInput() {
        styledInput(InputType.number) {
            attrs {
                classes = setOf("number-input")
                value = props.orderNumberEntry.toString()
                onChangeFunction = {
                    val poop = it.target.let {
                        it as HTMLInputElement
                    }.valueAsNumber.toInt()
                    console.log("New value", poop)
                    props.orderNumberEntryChanged(poop)
                }
                autoComplete = false
            }
            css {
                height = 75.px
                fontSize = 50.pt
                width = 140.px
                lineHeight = LineHeight("75px")
                verticalAlign = VerticalAlign.middle
            }
        }
    }

    private fun RBuilder.submitForm() {
        styledButton {
            attrs {
                type = ButtonType.submit
                classes = setOf("m-1")
                value = "Submit"
                disabled = !props.orderNumberValid
            }
            css {
                borderWidth = 5.px
                borderRadius = 8.px
            }
            icon2x("icon-plus")
        }
    }

    private fun RBuilder.switchButton() {
        styledButton {
            attrs {
                classes = setOf("m-1")
                onClickFunction = eventHandler(props.changeSides)
            }
            css {
                borderWidth = 5.px
                borderRadius = 8.px
            }
            icon2x("icon-resize-horiz")
        }
    }

    private fun RBuilder.openNewWindowButton() {
        styledButton {
            attrs {
                classes = setOf("m-1")
                onClickFunction = eventHandler(props.openDisplayWindow)
            }
            css {
                borderWidth = 5.px
                borderRadius = 8.px
            }
            icon2x("icon-share")
        }
    }

    private fun RBuilder.clearButton() {
        styledButton {
            attrs {
                classes = setOf("m-1")
                onClickFunction = eventHandler(props.clearDisplay)
            }
            css {
                borderWidth = 5.px
                borderRadius = 8.px
            }
            icon2x("icon-delete")
        }
    }

    override fun RBuilder.render() {
        styledForm {
            css.height = 100.pct
            attrs.onSubmitFunction = { event: Event ->
                console.log("On submit", props, appStore.getState())
                val orderNumberEntry = props.orderNumberEntry
                if (orderNumberEntry == null) props.changeSides()
                else props.addOrder(orderNumberEntry)
                event.preventDefault()
            }
            container {
                css {
                    height = 100.pct
                }
                columns {
                    attrs.classes = setOf("py-2", "columns")
                    css {
                        height = 100.pct
                        backgroundColor = props.currentColor.color
                        position = Position.relative
                    }

                    styledDiv {
                        attrs.classes = setOf("dropdown")
                        css {
                            position = Position.absolute
                        }
                        a("#",classes = "btn btn-link dropdown-toggle") {
                            attrs.tabIndex = "0"
                            icon("icon-more-vert")
                        }
                        ul("menu") {
                            li("menu-item") { a("#") {
                                attrs.onClickFunction = eventHandler(props.switchDisplayDirection)
                                +"Switch Display"
                            } }
                        }
                    }

                    column(2) {
                        css.fontWeight = FontWeight.bolder
                        if (props.currentColor === props.displayOrder.left) {
                            css {
                                lineHeight = LineHeight("0.5")
                                color = props.displayOrder.left.arrowColor
                            }
                            textFit {
                                attrs.mode = "single"
                                attrs.max = 200
                                +"⇚"
                            }
                        }
                    }
                    column(2) { numberInput() }
                    column(4) {
                        submitForm()
                        switchButton()
                        clearButton()
                        openNewWindowButton()
                    }
                    column(2) {
                        styledTextArea {
                            attrs.value = props.message
                            attrs.onChangeFunction = { event ->
                                event.target.let { it as HTMLTextAreaElement }.value.run { props.changeMessage(this) }
                            }
                            css {
                                display = Display.inlineBlock
                                verticalAlign = VerticalAlign.bottom
                                marginLeft = 20.px
                            }
                            css.height = 75.px
                        }
                    }
                    column(2) {
                        css {
                            fontWeight = FontWeight.bolder
                        }
                        if (props.currentColor === props.displayOrder.right) {
                            css {
                                lineHeight = LineHeight("0.5")
                                color = props.displayOrder.right.arrowColor
                            }
                            textFit {
                                attrs.mode = "single"
                                attrs.max = 200
                                +"⇛"
                            }
                        }
                    }
                }
            }
        }
    }
}

interface StateProps : RProps {
    var orderNumberEntry: Int?
    var orderNumberValid: Boolean
    var currentColor: OrderArea
    var message: String
    var displayOrder: DisplayOrder
}

interface DispatchProps : RProps {
    var orderNumberEntryChanged: (orderNumber: Int?) -> Unit
    var addOrder: (orderNumber: Int) -> Unit
    var clearDisplay: () -> Unit
    var changeSides: () -> Unit
    var changeMessage: (String) -> Unit
    var openDisplayWindow: () -> Unit
    var switchDisplayDirection: () -> Unit
}

private val mapStateToProps: StateProps.(AppState, RProps) -> Unit = {state, _ ->
    orderNumberEntry = state.orderNumberEntry
    orderNumberValid = state.orderNumberValid
    currentColor = state.currentColor
    message = state.message
    displayOrder = state.insideDisplayOrder
}

private val mapDispatchToProps: DispatchProps.((RAction) -> WrapperAction, RProps) -> Unit = {dispatch, _ ->
    orderNumberEntryChanged = { orderNumber -> dispatch(OrderNumberEntryChangeAction(orderNumber)) }
    addOrder = { orderNumber -> dispatch(NewOrderAction(orderNumber, Date.now())) }
    clearDisplay = { dispatch(ClearStateAction()) }
    changeSides = { dispatch(ChangeSidesAction()) }
    changeMessage = { message -> dispatch(ChangeMessageAction(message)) }
    openDisplayWindow = { dispatch(OpenDisplayWindowAction()) }
    switchDisplayDirection = { dispatch(SwitchDisplayDirectionAction()) }
}

val connectedOrderNumberForm: RClass<RProps> = rConnect<AppState, RAction, WrapperAction, RProps,
        StateProps, DispatchProps, OrderNumberFormProps>(mapStateToProps, mapDispatchToProps)(
        OrderNumberForm::class.js as RClass<OrderNumberFormProps>
)
