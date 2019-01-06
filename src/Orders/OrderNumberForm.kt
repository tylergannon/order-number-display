package orders

import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onSubmitFunction
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.button
import react.dom.div
import react.dom.form
import react.dom.input
import store.OrderArea

interface OrderNumberFormProps : RProps {
    var orderNumberEntry: Int?
    var orderNumberValid: Boolean
    var currentColor: OrderArea
    var orderNumberEntryChanged: (orderNumber: Int?) -> Unit
    var addOrder: (orderNumber: Int?) -> Unit
    var clearDisplay: () -> Unit
}

class OrderNumberForm(props: OrderNumberFormProps) : RComponent<OrderNumberFormProps, RState>(props) {
    override fun RBuilder.render() {
        div("${props.currentColor.name} control-container") {
            form(action = null, classes = "control") {
                attrs {
                    onSubmitFunction = {
                        props.addOrder(props.orderNumberEntry)
                        it.preventDefault()
                    }
                }

                input(type = InputType.number, name = "order_number", classes = "number-input") {
                    attrs {
                        value = props.orderNumberEntry.toString()
                        onChangeFunction = {
                            val inputElement = it.target as HTMLInputElement
                            props.orderNumberEntryChanged(inputElement.valueAsNumber.toInt())
                        }
                    }
                }

                input(type = InputType.submit, name = "submit_number") {
                    attrs {
                        value = "Submit"
                        disabled = !props.orderNumberValid
                    }
                }

                button {
                    attrs.onClickFunction = {e ->
                        e.preventDefault()
                        console.log(e)
                        props.clearDisplay()
                    }

                    attrs.name = "Clear Display"
                    attrs.value = "Clear Display"
                    +"Clear Display"
                }
            }
        }
    }
}
