package dev.lunisity.novaitembuilder.interfaces;

@FunctionalInterface
public interface TriConsumer<A, B, C> {

    void apply(final A a, final B b, final C c);

}