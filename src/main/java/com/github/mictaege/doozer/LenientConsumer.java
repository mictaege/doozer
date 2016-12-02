package com.github.mictaege.doozer;

/** */
@FunctionalInterface
public interface LenientConsumer<T> {

    /**
     * @param t the input argument
     */
    void accept(T t) throws Exception;

}
