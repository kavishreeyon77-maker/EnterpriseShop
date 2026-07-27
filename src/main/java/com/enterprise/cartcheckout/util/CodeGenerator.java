package com.enterprise.cartcheckout.util;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Utility for generating unique identifiers for orders, invoices, and checkout tokens.
 */
public final class CodeGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String DATE_FORMAT = "yyyyMMddHHmmss";

    private CodeGenerator() {
        // utility class
    }

    /**
     * Generates a unique order number in format: ORD-{date}-{random6digits}
     *
     * @return unique order number string
     */
    public static String generateOrderNumber() {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT));
        int random = 100000 + RANDOM.nextInt(900000);
        return String.format("ORD-%s-%d", date, random);
    }

    /**
     * Generates a unique invoice number.
     *
     * @return unique invoice number string
     */
    public static String generateInvoiceNumber() {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
        int random = 100000 + RANDOM.nextInt(900000);
        return String.format("INV-%s-%d", date, random);
    }

    /**
     * Generates a cryptographically secure checkout session token.
     *
     * @return UUID-based checkout token
     */
    public static String generateCheckoutToken() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

    /**
     * Generates a transaction ID for payment records.
     *
     * @return unique transaction ID string
     */
    public static String generateTransactionId() {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT));
        return String.format("TXN-%s-%s", date, UUID.randomUUID().toString().substring(0, 8).toUpperCase());
    }
}
