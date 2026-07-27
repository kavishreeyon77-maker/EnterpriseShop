package com.enterprise.cartcheckout.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Utility class to retrieve the currently authenticated user's details.
 */
public final class SecurityUtils {

    private SecurityUtils() {
        // utility class
    }

    /**
     * Returns the email (username) of the currently authenticated user.
     *
     * @return email string of current user
     * @throws IllegalStateException if no authenticated user found in context
     */
    public static String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new IllegalStateException("No authenticated user found in security context");
        }
        return authentication.getName();
    }
}
