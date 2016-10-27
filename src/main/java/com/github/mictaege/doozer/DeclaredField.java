package com.github.mictaege.doozer;

/**
 * Interface for all enumerations that declares the fields of an object.
 * @param <T> The type of the object
 * @deprecated Just use plain enumerations instead
 */
@SuppressWarnings("squid:S2326")
@Deprecated
public interface DeclaredField<T> {

    String name();

}
