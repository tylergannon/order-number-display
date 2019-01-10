package index

import app.app
import kotlinext.js.require
import kotlinext.js.requireAll
import react.dom.render
import react.redux.provider
import store.appStore
import kotlin.browser.document

fun main(args: Array<String>) {
    require("spectre.css/dist/spectre.css")
    require("spectre.css/dist/spectre-icons.css")
    requireAll(require.context("src", true, js("/\\.css$/")))

    render(document.getElementById("root")) {
        provider(appStore) {
            app()
        }
    }
}
