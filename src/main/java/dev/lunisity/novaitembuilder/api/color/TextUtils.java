package dev.lunisity.novaitembuilder.api.color;

import java.util.regex.Pattern;

public class TextUtils {

    public static boolean isEmpty(final String text) {
        return text == null || text.isEmpty();
    }

    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");
    private static final Pattern RAINBOW = Pattern.compile("<RAINBOW([0-9]{1,3})>(.*?)</RAINBOW>");

    private TextUtils() {
        throw new UnsupportedOperationException("Cannot instantiate");
    }
}
