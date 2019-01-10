package grid

import kotlinx.html.DIV
import kotlinx.html.classes
import react.RBuilder
import react.dom.RDOMBuilder
import react.dom.i
import styled.StyledDOMBuilder
import styled.styledDiv

enum class Width {
    xs, sm, md, lg, xl;
}

typealias Children<T> = RDOMBuilder<T>.() -> Unit
typealias StyledChildren<T> = StyledDOMBuilder<T>.() -> Unit

fun RBuilder.icon(name: String) = i("icon $name") {}
fun RBuilder.icon2x(name: String) = i("icon icon-2x $name") {}
fun RBuilder.icon3x(name: String) = i("icon icon-3x $name") {}
fun RBuilder.icon4x(name: String) = i("icon icon-4x $name") {}

fun RBuilder.container(classes: Set<String> = setOf(), children: StyledChildren<DIV>) {
    styledDiv {
        attrs.classes = setOf("container").plus(classes)
        children()
    }
}

fun RBuilder.columns(classes: Set<String> = setOf(), children: StyledChildren<DIV>) {
    styledDiv {
        attrs.classes = setOf("columns").plus(classes)
        children()
    }
}

fun RBuilder.column(size: Int, classes: Set<String> = setOf(), children: StyledChildren<DIV> = {}) {
    styledDiv {
        attrs.classes = setOf("col-$size").plus(classes)
        children()
    }
}
