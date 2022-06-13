package org.qiunet.utils.data;

/***
 * 不重要, 没必要封装对象. 用这个比较快.
 *
 * @author qiunet
 * 2022/6/13 15:00
 */
public interface ArgsData {

	static <A> ArgsData.One<A> of(A a) {
		return () -> a;
	}

	static <A, B> ArgsData.Two<A, B> of(A a, B b) {
		return new Two<A, B>() {
			@Override
			public A a() {
				return a;
			}

			@Override
			public B b() {
				return b;
			}
		};
	}

	static <A, B, C> ArgsData.Three<A, B, C> of(A a, B b, C c) {
		return new Three<A, B, C>() {
			@Override
			public A a() {
				return a;
			}

			@Override
			public B b() {
				return b;
			}

			@Override
			public C c() {
				return c;
			}
		};
	}

	static <A, B, C, D> ArgsData.Four<A, B, C, D> of(A a, B b, C c, D d) {
		return new Four<A, B, C, D>() {
			@Override
			public A a() {
				return a;
			}

			@Override
			public B b() {
				return b;
			}

			@Override
			public C c() {
				return c;
			}

			@Override
			public D d() {
				return d;
			}
		};
	}

	static <A, B, C, D, E> ArgsData.Five<A, B, C, D, E> of(A a, B b, C c, D d, E e) {
		return new Five<A, B, C, D, E>() {
			@Override
			public A a() {
				return a;
			}

			@Override
			public B b() {
				return b;
			}

			@Override
			public C c() {
				return c;
			}

			@Override
			public D d() {
				return d;
			}

			@Override
			public E e() {
				return e;
			}
		};
	}

	interface One<A> extends ArgsData {
		A a();
	}

	interface Two<A, B> extends ArgsData {
		A a();

		B b();
	}

	interface Three<A, B, C> extends ArgsData {
		A a();

		B b();

		C c();
	}

	interface Four<A, B, C, D> extends ArgsData {
		A a();

		B b();

		C c();

		D d();
	}

	interface Five<A, B, C, D, E> extends ArgsData {
		A a();

		B b();

		C c();

		D d();

		E e();
	}
}
