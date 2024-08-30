package dev.lunisity.novaitembuilder.api.text;

import dev.lunisity.novaitembuilder.api.color.ColorAPI;
import dev.lunisity.novaitembuilder.api.interfaces.StringMappable;
import dev.lunisity.novaitembuilder.api.time.TimeFormatMode;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class TextReplacer {

    private static final Map<Class<?>, StringMappable<?>> MAPPERS = new HashMap<>();

    public static <T> void registerMapper(final Class<T> clazz, final StringMappable<T> mapper) {
        TextReplacer.MAPPERS.put(clazz, mapper);
    }

    private final Map<String, String> placeholders = new HashMap<>();

    public TextReplacer with(final String placeholder, final String replacement) {
        this.placeholders.put(placeholder, replacement);
        return this;
    }

    public TextReplacer with(final String placeholder, final Supplier<String> supplier) {
        return with(placeholder, supplier.get());
    }

    public TextReplacer with(final String placeholder, final Object object) {

        final Class<?> clazz = object.getClass();

        if (TextReplacer.MAPPERS.containsKey(clazz)) {
            return with(placeholder, TextReplacer.MAPPERS.get(clazz));
        }
        return with(placeholder, object.toString());
    }

    public TextReplacer withTimeFull(final String placeholder, final long time, final TimeUnit timeUnit, final TimeFormatMode timeFormatMode) {
        return with(placeholder, timeFormatMode.apply(time, timeUnit));
    }


    public TextReplacer withTime(final String placeholder, final long time, final TimeUnit timeUnit, final TimeFormatMode timeFormatMode) {
        return with(placeholder, timeFormatMode.apply(time, timeUnit));
    }

    public TextReplacer withTime(final String placeholder, final long time, final TimeFormatMode timeFormatMode) {
        return with(placeholder, timeFormatMode.apply(time, TimeUnit.SECONDS));
    }

    public String apply(String string) {

        if (string == null) {
            throw new IllegalArgumentException("Cannot colorize a null string...");
        }

        if (string.isEmpty()) {
            return string;
        }

        if (this.placeholders.isEmpty()) {
            return ColorAPI.apply(string);
        }

        for (Map.Entry<String, String> entry : this.placeholders.entrySet()) {
            string = string.replace(entry.getKey(), entry.getValue());
        }
        return ColorAPI.apply(string);
    }

    public List<String> apply(final List<String> list) {

        if (list == null || list.isEmpty()) {
            return new LinkedList<>();
        }
        return list.stream().map(this::apply).collect(Collectors.toList());
    }

    public String[] apply(String... string) {
        return apply(Arrays.asList(string)).toArray(new String[string.length]);
    }
}

