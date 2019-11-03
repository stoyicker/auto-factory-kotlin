package org.github.stoyicker.auto.factory.kotlin.processor

import kotlinx.metadata.jvm.KotlinClassMetadata
import javax.annotation.processing.Messager
import javax.lang.model.element.Element
import javax.lang.model.element.kMetadata
import javax.tools.Diagnostic

internal class ContextVerifier(private val messager: Messager) {
  fun verify(element: Element) = when (element.kMetadata()) {
    is KotlinClassMetadata.Class -> true
    else -> {
      messager.printMessage(Diagnostic.Kind.ERROR, ERROR_UNSUPPORTED_ANNOTATION_TARGET, element)
      false
    }
  }

  class Factory : (Messager) -> ContextVerifier {
    override fun invoke(messager: Messager) = ContextVerifier(messager)
  }
}

internal const val ERROR_UNSUPPORTED_ANNOTATION_TARGET = "Unsupported annotation target"
