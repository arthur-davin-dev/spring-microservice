package kh.edu.istad.platform.customer.domain.valueobject;

import java.util.Locale;

public enum CustomerSegment {
    SILVER,
    GOLD,
    PREMIUM;

    public static CustomerSegment from(String value) {
        if (value == null || value.isBlank()) return null;
        return valueOf(value.trim().toUpperCase(Locale.ROOT));
    }

    public String label() {
        String lower = name().toLowerCase(Locale.ROOT);
        return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
    }
}
