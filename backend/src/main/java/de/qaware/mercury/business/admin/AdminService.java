package de.qaware.mercury.business.admin;

import java.util.List;

public interface AdminService {
    List<Admin> findAdminsToNotifyOnShopApprovalNeeded();
}
