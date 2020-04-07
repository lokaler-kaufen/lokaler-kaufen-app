package de.qaware.mercury.e2e

import geb.Page
import geb.module.FormElement
import geb.module.TextInput

class StartPage extends Page {

    static url = "/#/"

    static at = { title.startsWith('lokaler.kaufen') }

    static content = {
        postcode { $("input", 0, type: 'text').module(TextInput) }
        searchButton { $("button", 0, type: 'submit').module(FormElement) }
        footer { module(FooterModule) }
    }
}
