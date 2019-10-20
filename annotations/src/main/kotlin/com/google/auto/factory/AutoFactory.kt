package com.google.auto.factory

import kotlin.reflect.KClass

/**
 * An annotation to be applied to elements for which a factory should be automatically generated.
 *
 * <h2>Visibility</h2>
 * <p>The visibility of the generated factories will always be either {@code public} or {@code internal}. The
 * visibility of any given factory method is determined by the visibility of the type being created. The generated
 * factory is {@code public} if any of the factory methods are. Any method that implements an interface method is
 * necessarily public and any method that overrides an abstract method has the same visibility as that method.
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CONSTRUCTOR, AnnotationTarget.TYPE, AnnotationTarget.FUNCTION)
annotation class AutoFactory(
  /**
   * The <i>simple</i> name of the generated factory; the factory is always generated in the same
   * package as the annotated type.  The default value (the empty string) will result in a factory
   * with the name of the type being created with {@code Factory} appended to the end. For example,
   * the default name for a factory for {@code MyType} will be {@code MyTypeFactory}.
   *
   * <p>If the annotated type is nested, then the generated factory's name will start with the
   * enclosing type names, separated by underscores. For example, the default name for a factory for
   * {@code Outer.Inner.ReallyInner} is {@code Outer_Inner_ReallyInnerFactory}. If {@code className}
   * is {@code Foo}, then the factory name is {@code Outer_Inner_Foo}.
   */
  val name: String = "",
  /**
   * An array of interfaces that the generated factory is required to implement.
   */
  val implementing: Array<KClass<out Any>>,
  /**
   * The type that the generated factory is require to extend.
   */
  val extending: KClass<out Any> = Any::class,
  /**
   * Whether or not the generated factory should be final.
   * Defaults to disallowing subclasses (generating the factory as final).
   */
  val allowSubclasses: Boolean = false)
