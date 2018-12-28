package Orders

import Store.NewOrderAction
import Store.Order
import Store.OrderArea
import Store.appStore
import kotlinx.html.InputType
import kotlinx.html.classes
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onSubmitFunction
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.ValidityState
import react.*
import react.dom.div
import react.dom.form
import react.dom.input
import redux.RAction
import textFit
import kotlin.browser.window
import kotlin.js.Date

interface OrderNumberFormProps : RProps {
    var orderNumberEntry: Int?
    var orderNumberValid: Boolean
    var currentColor: OrderArea
    var orderNumberEntryChanged: (orderNumber: Int?) -> Unit
    var addOrder: (orderNumber: Int?) -> Unit
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
            }
        }
    }

}

