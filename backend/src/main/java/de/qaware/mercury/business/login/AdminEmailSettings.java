package de.qaware.mercury.business.login;

import lombok.Value;

@Value
public class AdminEmailSettings {
    boolean onShopApprovalNeeded;

    public static AdminEmailSettings noEmails() {
        return new AdminEmailSettings(false);
    }
}
