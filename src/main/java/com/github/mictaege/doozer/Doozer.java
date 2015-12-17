package com.github.mictaege.doozer;

import java.lang.reflect.Field;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.apache.commons.lang3.reflect.FieldUtils;

/**  It's only meaning of life is to build all kinds of objects as sweet as sugar. */
public final class Doozer {

	/**
	 * @param objSupplier Either a lambda or a constructor reference to create the object
	 * @param objModifier Endless list of lambdas to modify the object
	 * @param <T> The type of the object to make
	 * @return The object
	 */
	public static <T> T  makeA(final Supplier<T> objSupplier, final Consumer<With<T>>... objModifier) {
		final T obj = objSupplier.get();
		Stream.of(objModifier).forEach(
				m -> m.accept(new With<>(obj))
		);
		return obj;
	}

	/**
	 * @see #makeA(Supplier, Consumer[])
	 */
	public static <T> T  makeFrom(final Supplier<T> objSupplier, final Consumer<With<T>>... objModifier) {
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
		 * @param declaredField The field of the object to modify
		 * @param val The value for the field
		 * @param <V> The type of the value
		 */
		public <V> void with(final DeclaredField<? super T> declaredField, final V val) {
			try {
				final Field field = FieldUtils.getField(obj.getClass(), declaredField.name(), true);
				field.set(obj, val);
			} catch (IllegalAccessException e) {
				throw new IllegalArgumentException(e);
			}
		}

		/**
		 * @see #with(DeclaredField, Object)
		 */
		public <V> void but(final DeclaredField<? super T> declaredField, final V val) {
			with(declaredField, val);
		}

		/**
		 * @param method Method reference
		 * @param value The value to be set
		 * @param <V> The type of teh value
		 */
		public <V> void apply(final BiConsumer<T, V> method, final V value) {
			method.accept(obj, value);
		}

		/**
		 * @return The object itself
		 */
		public T apply() {
			return obj;

		}
	}
}
