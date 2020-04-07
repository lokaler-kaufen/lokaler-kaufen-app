package de.qaware.mercury.e2e

import geb.Page

class PrivacyPage extends Page {
    static url = "/#/privacy"

    static at = { browser.pageUrl.endsWith('/#/privacy') }

    static content = {
        header { $("h1") }
        footer { module(FooterModule) }
    }
}
