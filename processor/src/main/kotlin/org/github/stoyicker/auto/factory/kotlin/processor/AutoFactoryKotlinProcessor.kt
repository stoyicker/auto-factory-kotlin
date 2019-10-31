package org.github.stoyicker.auto.factory.kotlin.processor

import com.google.auto.factory.AutoFactory
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

internal class AutoFactoryKotlinProcessor(
    private val supportedAnnotationClasses: Set<Class<out Annotation>>,
    private val supportedOptions: Set<String>,
    private val supportedSourceVersion: SourceVersion,
    private val contextVerifierFactory: (Messager) -> ContextVerifier) : AbstractProcessor() {
  lateinit var contextVerifier: ContextVerifier

  @Suppress("unused") // Actual constructor used in production
  constructor() : this(
      setOf(AutoFactory::class.java),
      setOf(OPTION_KEY_KAPT_KOTLIN_GENERATED),
      SourceVersion.latestSupported(),
      ContextVerifier.Factory())

  override fun init(processingEnv: ProcessingEnvironment) {
    super.init(processingEnv)
    contextVerifier = contextVerifierFactory(processingEnv.messager)
  }

  override fun process(typeElements: MutableSet<out TypeElement>, roundEnvironment: RoundEnvironment): Boolean {
    supportedAnnotationClasses.forEach { annotationClass ->
      roundEnvironment.getElementsAnnotatedWith(annotationClass)
          .filter { contextVerifier.verify(it) }
    }
    return true
  }

  override fun getSupportedAnnotationTypes() = supportedAnnotationClasses.mapTo(hashSetOf()) { it.name }

  override fun getSupportedOptions() = supportedOptions

  override fun getSupportedSourceVersion() = supportedSourceVersion
}

private const val OPTION_KEY_KAPT_KOTLIN_GENERATED = "kapt.kotlin.generated"
