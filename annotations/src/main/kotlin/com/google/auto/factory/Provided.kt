package com.google.auto.factory

/**
 * An annotation to be applied to parameters that should be provided by an {@link javax.inject.Inject injected}
 * {@link javax.inject.Provider} in a generated factory.
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Provided
