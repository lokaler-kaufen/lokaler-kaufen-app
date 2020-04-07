package de.qaware.mercury.e2e

import geb.Module

class FooterModule extends Module {
    static content = {
        login { $("div.app-footer a", 0) }
        register { $("div.app-footer a", 1) }
        imprint { $("div.app-footer a", 2) }
        privacy { $("div.app-footer a", 3) }
    }

    void login() {
        login.click(LoginPage)
    }

    void register() {
        register.click()
    }

    void imprint() {
        imprint.click(ImprintPage)
    }

    void privacy() {
        privacy.click(PrivacyPage)
    }
}
