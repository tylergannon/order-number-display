package index

import app.*
import kotlinext.js.*
import react.dom.*
import react.redux.provider
import store.appStore
import kotlin.browser.*

fun main(args: Array<String>) {
    requireAll(require.context("src", true, js("/\\.css$/")))

    render(document.getElementById("root")) {
        provider(appStore) {
            app()
        }
    }
}
