package org.github.stoyicker.auto.factory.kotlin.processor

import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedOptions
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions(OPTION_KEY_KAPT_KOTLIN_GENERATED)
class AutoFactoryKotlinProcessor : AbstractProcessor() {
  override fun process(p0: MutableSet<out TypeElement>?, p1: RoundEnvironment?): Boolean {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
}

private const val OPTION_KEY_KAPT_KOTLIN_GENERATED = "kapt.kotlin.generated"
