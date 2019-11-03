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
    if (!roundEnvironment.errorRaised()) {
      supportedAnnotationClasses.forEach { annotationClass ->
        roundEnvironment.getElementsAnnotatedWith(annotationClass)
            .filter { contextVerifier.verify(it) }
            // TODO Change this to onEach and delegate it away to put it on a map of k(?)class to constructors
//            .flatMap {
//              when {
//                it.kMetadata() is KotlinClassMetadata.Class -> it.toKmClass().constructors.filter { constructor ->
//                  Flag.IS_PUBLIC(constructor.flags)
//                  // TODO Include internal constructors if it's not a typealias
//                }
//                else -> {
//                  processingEnv.messager.printMessage(
//                      Diagnostic.Kind.ERROR,
//                      "Unsupported annotation target kClass type ${it.kMetadata().let {
//                        metadata -> if (metadata == null) null else metadata::class.java.name
//                      }}, should have been checked by the verifier",
//                      it)
//                  emptyList()
//                }
//              }
//            }
      }
    }
    return true
  }

  override fun getSupportedAnnotationTypes() = supportedAnnotationClasses.mapTo(hashSetOf()) { it.name }

  override fun getSupportedOptions() = supportedOptions

  override fun getSupportedSourceVersion() = supportedSourceVersion
}

private const val OPTION_KEY_KAPT_KOTLIN_GENERATED = "kapt.kotlin.generated"
