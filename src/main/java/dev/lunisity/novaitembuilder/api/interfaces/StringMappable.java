package dev.lunisity.novaitembuilder.api.interfaces;

@FunctionalInterface
public interface StringMappable<T> {

    String map(final T object);

}