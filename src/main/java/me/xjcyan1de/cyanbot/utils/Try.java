package me.xjcyan1de.cyanbot.utils;

import java.util.function.Predicate;

/**
 * Позволяет игрорировать некоторые фичи java<br>
 * Ну а хули нам, хохлам
 */
public class Try {

	/**
	 * Игрорировать проверяемое исключение (если проверяемое исключение возникнет, оно будет в обертке RuntimeThrowable)
	 * @param supplier code
	 * @param <T>      type result
	 * @return result
	 */
	public static <T> T unchecked(SupplierThrows<T> supplier) {
		try {
			return supplier.get();
		} catch(Throwable e) {
			doThrow0(e);
			throw new AssertionError(); // до сюда код не дойдет
		}
	}

	/**
	 * Игрорировать проверяемое исключение (если проверяемое исключение возникнет, оно будет в обертке RuntimeThrowable)
	 * @param runnable code
	 */
	public static void unchecked(RunnableThrows runnable) {
		try {
			runnable.run();
		} catch(Throwable e) {
			doThrow0(e);
			throw new AssertionError(); // до сюда код не дойдет
		}
	}

	/**
	 * Игрорировать проверяемое исключение (если проверяемое исключение возникнет, оно будет в обертке RuntimeThrowable)
	 * @param predicate code
	 */
	public static <T> Predicate<T> unchecked(PredicateThrows<T> predicate) {
		return t -> {
			try {
				return predicate.test(t);
			} catch(Throwable e) {
				doThrow0(e);
				throw new AssertionError(); // до сюда код не дойдет
			}
		};
	}

	/**
	 * Игнорировать исключения целиком и полностью (если исключение воникнет, тогда просто будет printStackTrace)
	 * @param supplier code
	 * @param <T>      type result
	 * @param def      дефотное значение, если возникнет исключение
	 * @return result
	 */
	public static <T> T ignore(SupplierThrows<T> supplier, T def) {
		try {
			return supplier.get();
		} catch(Throwable e) {
			e.printStackTrace();
			return def;
		}
	}

	/**
	 * Игнорировать исключения целиком и полностью (если исключение воникнет, тогда просто будет printStackTrace)
	 * @param runnable code
	 */
	public static void ignore(RunnableThrows runnable) {
		try {
			runnable.run();
		} catch(Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * Игрорировать проверяемое исключение (если исключение возникнет, оно будет в обертке RuntimeThrowable)
	 * @param predicate code
	 * @param <T>       type result
	 * @param def       дефотное значение, если возникнет исключение
	 */
	public static <T> Predicate<T> ignore(PredicateThrows<T> predicate, boolean def) {
		return t -> {
			try {
				return predicate.test(t);
			} catch(Throwable e) {
				e.printStackTrace();
				return def;
			}
		};
	}

	@SuppressWarnings("unchecked")
	private static <E extends Throwable> void doThrow0(Throwable e) throws E {
		throw (E) e;
	}

	public interface SupplierThrows<T> {

		T get() throws Throwable;
	}

	public interface RunnableThrows {

		void run() throws Throwable;
	}

	public interface PredicateThrows<T> {

		boolean test(T val) throws Throwable;
	}
}