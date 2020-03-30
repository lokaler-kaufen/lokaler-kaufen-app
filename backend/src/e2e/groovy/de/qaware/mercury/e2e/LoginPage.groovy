package de.qaware.mercury.e2e

import geb.Page
import geb.module.FormElement
import geb.module.PasswordInput
import geb.module.TextInput

class LoginPage extends Page {

    static url = "/#/login"

    static at = { browser.pageUrl.endsWith('/#/login') }

    static content = {
        header { $("h2") }
        email { $("input", type: 'text', formcontrolname: 'email').module(TextInput) }
        password { $("input", type: 'password', formcontrolname: 'password').module(PasswordInput) }
        loginButton { $("button", 0, type: 'submit').module(FormElement) }
        error { $('p', class: 'error') }
        footer { module(FooterModule) }
    }

    void login() {
        loginButton.click()
    }
}
