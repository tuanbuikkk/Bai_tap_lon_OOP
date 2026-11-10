package com.yourteam.englishapp;

import static org.junit.Assert.*;

import com.yourteam.englishapp.model.AccessLevel;
import com.yourteam.englishapp.model.Role;
import org.junit.Test;

public class AccessLevelTest {
    @Test public void premiumAccessesFree() {
        assertTrue(AccessLevel.PREMIUM.canAccess(AccessLevel.FREE));
        assertTrue(AccessLevel.PREMIUM.canAccess(AccessLevel.PREMIUM));
        assertTrue(AccessLevel.FREE.canAccess(AccessLevel.FREE));
        assertFalse(AccessLevel.FREE.canAccess(AccessLevel.PREMIUM));
    }

    @Test public void onlyAdminManagesUsers() {
        assertTrue(Role.ADMIN.canManageUsers());
        assertFalse(Role.USER.canManageUsers());
    }
}
