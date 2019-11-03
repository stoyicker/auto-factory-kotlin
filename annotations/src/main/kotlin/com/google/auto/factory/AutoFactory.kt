package com.google.auto.factory

/**
 * An annotation to be applied to elements for which factories should be automatically generated.
 *
 * <h2>Visibility</h2>
 * <p>The visibility of the generated factories will always be either {@code public} or {@code internal}, according to
 * the visibility of the annotated element.
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.TYPEALIAS, AnnotationTarget.CLASS)
annotation class AutoFactory(
    /**
     * The <i>simple</i> name of the generated factory; the factory is always generated in the same
     * package as the annotated type. The default value (the empty string) will result in a factory
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
     * Whether or not the generated factory should be final.
     * Defaults to disallowing subclasses (generating the factory as final).
     */
    val allowSubclasses: Boolean = false)
