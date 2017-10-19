package com.github.mictaege.doozer;

import com.github.mictaege.lenientfun.FunctionalRuntimeException;
import com.github.mictaege.lenientfun.LenientBiConsumer;
import com.github.mictaege.lenientfun.LenientConsumer;
import com.github.mictaege.lenientfun.LenientSupplier;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.stream.Stream;

import static java.util.Optional.ofNullable;

/**  It's only meaning of life is to build all kinds of objects as sweet as sugar. */
public final class Doozer {

    /** hidden constructor */
    private Doozer() {
        super();
    }

    /**
     * @param objSupplier Either a lambda or a constructor reference to create the object
     * @param objModifier Endless list of lambdas to modify the object
     * @param <T> The type of the object to make
     * @return The object
     */
    public static <T> T  makeA(final LenientSupplier<T> objSupplier, final LenientConsumer<With<T>>... objModifier) {
        final T obj;
        try {
            obj = objSupplier.get();
        } catch (final Exception e) {
            throw new FunctionalRuntimeException("Error during object creation.", e);
        }
        Stream.of(objModifier).forEach(
                m -> {
                    try {
                        m.accept(new With<>(obj));
                    } catch (final Exception e) {
                        throw new FunctionalRuntimeException("Error during object modification.", e);
                    }
                }
        );
        return obj;
    }

    /**
     * @see #makeA(LenientSupplier, LenientConsumer[])
     */
    public static <T> T  makeFrom(final LenientSupplier<T> objSupplier, final LenientConsumer<With<T>>... objModifier) {
        return makeA(objSupplier, objModifier);
    }

    /**
     * A modification of the object.
     *
     * @param <T> The type of the object
     */
    public static class With<T> {

        private final T obj;

        With(final T obj) {
            super();
            this.obj = obj;
        }

        /**
         * @param field An enum that defines the field of the object to modify. E.g. 'PersonField.firstName'.
         *                      Note that name of the enum has to be case-sensitive equal to the objects instance field.
         *                      So if an object 'Person' has a field 'firstName' the enum has to have the name
         *                      'PersonField.firstName' and not 'PersonField.FIRSTNAME', or 'PersonField.firstname' etc.
         * @param be The value for the field
         * @param <V> The type of the value
         */
        public <V> void with(final Enum<?> field, final V be) {
            with(field.name(), be);
        }

        /**
         * @param field A String that defines the field of the object to modify. E.g. 'firstName'.
         *                      Note that the given name has to be case-sensitive equal to the objects instance field.
         *                      So if an object 'Person' has a field 'firstName' the given name has to have the name
         *                      'firstName' and not 'FIRSTNAME', or 'firstname' etc.
         * @param be The value for the field
         * @param <V> The type of the value
         */
        public <V> void with(String field, V be) {
            try {
                final Field fieldObj = ofNullable(FieldUtils.getField(obj.getClass(), field, true))
                        .orElseThrow(() -> new IllegalArgumentException("The field '" + field + "' is missing"));
                fieldObj.set(obj, be);
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException(e);
            }
        }

        /**
         * @see #with(Enum, Object)
         */
        public <V> void but(final Enum<?> field, final V be) {
            with(field, be);
        }


        /**
         * @see #with(String, Object)
         */
        public <V> void but(final String field, final V be) {
            with(field, be);
        }

        /**
         * @param method Method reference
         * @param with The value to be set
         * @param <V> The type of the value
         */
        public <V> void apply(final LenientBiConsumer<T, V> method, final V with) {
            try {
                method.accept(obj, with);
            } catch (final Exception e) {
                throw new FunctionalRuntimeException(e);
            }
        }

        /**
         * @return The object itself
         */
        public T apply() {
            return obj;

        }
    }
}
