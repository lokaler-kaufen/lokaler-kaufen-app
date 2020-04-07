package de.qaware.mercury.e2e

import geb.Page

class ImprintPage extends Page {

    static url = "/#/imprint"

    static at = { browser.pageUrl.endsWith('/#/imprint') }

    static content = {
        header { $("h1") }
        footer { module(FooterModule) }
    }
}
