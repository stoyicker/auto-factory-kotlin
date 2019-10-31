package org.github.stoyicker.auto.factory.kotlin.processor

import com.google.auto.factory.AutoFactory
import javax.annotation.processing.Messager
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.tools.Diagnostic

internal class ContextVerifier(private val messager: Messager) {
  fun verify(element: Element) = when (element.kind) {
    ElementKind.CLASS -> true
    ElementKind.CONSTRUCTOR -> {
      val enclosingElement = element.enclosingElement
      if (enclosingElement.kind != ElementKind.CLASS) {
        messager.printMessage(Diagnostic.Kind.ERROR, ERROR_CONSTRUCTOR_NOT_ENCLOSED_BY_CLASS, element)
        false
      } else {
        if (enclosingElement.getAnnotation(AutoFactory::class.java) != null) {
          messager.printMessage(Diagnostic.Kind.WARNING, ERROR_ANNOTATED_CONSTRUCTOR_IN_ANNOTATED_CLASS, element)
        }
        true
      }
    }
    else -> {
      messager.printMessage(Diagnostic.Kind.ERROR, ERROR_UNSUPPORTED_ANNOTATION_TARGET, element)
      false
    }
  }

  class Factory : (Messager) -> ContextVerifier {
    override fun invoke(messager: Messager) = ContextVerifier(messager)
  }
}

internal const val ERROR_CONSTRUCTOR_NOT_ENCLOSED_BY_CLASS = "Unexpected state: constructor not enclosed by class. Please report an issue at https://github.com/stoyicker/auto-factory-kotlin/issues"
internal const val ERROR_ANNOTATED_CONSTRUCTOR_IN_ANNOTATED_CLASS = "Constructor annotated for factory generation in class which is also annotated. Annotating a class is equivalent to annotating all of its constructors, so this is redundant"
internal const val ERROR_UNSUPPORTED_ANNOTATION_TARGET = "Unsupported annotation target"
