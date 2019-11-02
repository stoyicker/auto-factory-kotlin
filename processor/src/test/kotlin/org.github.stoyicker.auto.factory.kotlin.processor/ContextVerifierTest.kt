package org.github.stoyicker.auto.factory.kotlin.processor

import com.google.auto.factory.AutoFactory
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import javax.annotation.processing.Messager
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.tools.Diagnostic

internal class ContextVerifierTest {
  private val messager = mock<Messager>()
  private val subject = ContextVerifier(messager)

  @AfterEach
  fun afterEach() = verifyNoMoreInteractions(messager)

  @Test
  fun verifyOnClass() {
    val element = mock<Element> {
      whenever(mock.kind).thenReturn(ElementKind.CLASS)
    }

    val actual = subject.verify(element)

    verify(element).kind
    verifyNoMoreInteractions(element)
    assertTrue(actual)
  }

  @Test
  fun verifyOnConstructorNotEnclosedByClass() {
    val enclosingElement = mock<Element> {
      whenever(mock.kind).thenReturn(ElementKind.OTHER)
    }
    val element = mock<Element> {
      whenever(mock.kind).thenReturn(ElementKind.CONSTRUCTOR)
      whenever(mock.enclosingElement).thenReturn(enclosingElement)
    }

    val actual = subject.verify(element)

    verify(element).kind
    verify(element).enclosingElement
    verify(enclosingElement).kind
    verify(messager).printMessage(Diagnostic.Kind.ERROR, ERROR_CONSTRUCTOR_NOT_ENCLOSED_BY_CLASS, element)
    verifyNoMoreInteractions(element, enclosingElement)
    assertFalse(actual)
  }

  @Test
  fun verifyOnConstructorAndClass() {
    val annotation = mock<AutoFactory>()
    val enclosingElement = mock<Element> {
      whenever(mock.kind).thenReturn(ElementKind.CLASS)
      whenever(mock.getAnnotation(AutoFactory::class.java)).thenReturn(annotation)
    }
    val element = mock<Element> {
      whenever(mock.kind).thenReturn(ElementKind.CONSTRUCTOR)
      whenever(mock.enclosingElement).thenReturn(enclosingElement)
    }

    val actual = subject.verify(element)

    verify(element).kind
    verify(element).enclosingElement
    verify(enclosingElement).kind
    verify(enclosingElement).getAnnotation(AutoFactory::class.java)
    verify(messager).printMessage(Diagnostic.Kind.WARNING, ERROR_ANNOTATED_CONSTRUCTOR_IN_ANNOTATED_CLASS, element)
    verifyNoMoreInteractions(element, enclosingElement, annotation)
    assertFalse(actual)
  }

  @Test
  fun verifyOnConstructorAndNotClass() {
    val enclosingElement = mock<Element> {
      whenever(mock.kind).thenReturn(ElementKind.CLASS)
      whenever(mock.getAnnotation(AutoFactory::class.java)).thenReturn(null)
    }
    val element = mock<Element> {
      whenever(mock.kind).thenReturn(ElementKind.CONSTRUCTOR)
      whenever(mock.enclosingElement).thenReturn(enclosingElement)
    }

    val actual = subject.verify(element)

    verify(element).kind
    verify(element).enclosingElement
    verify(enclosingElement).kind
    verify(enclosingElement).getAnnotation(AutoFactory::class.java)
    verifyNoMoreInteractions(element, enclosingElement)
    assertTrue(actual)
  }

  @Test
  fun verifyOnOther() {
    val element = mock<Element> {
      whenever(mock.kind).thenReturn(ElementKind.OTHER)
    }

    val actual = subject.verify(element)

    verify(element).kind
    verify(messager).printMessage(Diagnostic.Kind.ERROR, ERROR_UNSUPPORTED_ANNOTATION_TARGET, element)
    verifyNoMoreInteractions(element)
    assertFalse(actual)
  }
}
