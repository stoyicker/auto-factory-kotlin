package org.github.stoyicker.auto.factory.kotlin.processor

import com.google.auto.factory.AutoFactory
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedOptions
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions(OPTION_KEY_KAPT_KOTLIN_GENERATED)
internal class AutoFactoryKotlinProcessor constructor(
    private val supportedAnnotationTypes: Set<String>) : AbstractProcessor() {
  @Suppress("unused") // Actual constructor used in production
  constructor() : this(setOf(AutoFactory::javaClass.name))

  override fun init(processingEnv: ProcessingEnvironment?) {
    super.init(processingEnv)
  }

  override fun process(p0: MutableSet<out TypeElement>?, p1: RoundEnvironment?): Boolean {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun getSupportedAnnotationTypes() = supportedAnnotationTypes
}

private const val OPTION_KEY_KAPT_KOTLIN_GENERATED = "kapt.kotlin.generated"
