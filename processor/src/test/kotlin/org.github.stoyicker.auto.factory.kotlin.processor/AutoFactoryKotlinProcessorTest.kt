package org.github.stoyicker.auto.factory.kotlin.processor

import com.google.auto.factory.AutoFactory
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

internal class AutoFactoryKotlinProcessorTest {
  private val supportedAnnotationClasses = mock<Set<Class<out Annotation>>>()
  private val supportedOptions = mock<Set<String>>()
  private val supportedSourceVersion = SourceVersion.RELEASE_0
  private val contextVerifierFactory = mock<(Messager) -> ContextVerifier>()
  private val subject = AutoFactoryKotlinProcessor(
      supportedAnnotationClasses, supportedOptions, supportedSourceVersion, contextVerifierFactory)

  @AfterEach
  fun afterEach() = verifyNoMoreInteractions(supportedAnnotationClasses, supportedOptions, contextVerifierFactory)

  @Test
  fun init() {
    val messager = mock<Messager>()
    val processingEnvironment = mock<ProcessingEnvironment> {
      whenever(mock.messager).thenReturn(messager)
    }
    val contextVerifier = mock<ContextVerifier>()
    whenever(contextVerifierFactory(messager)).thenReturn(contextVerifier)

    subject.init(processingEnvironment)

    verify(processingEnvironment).messager
    verify(contextVerifierFactory)(messager)
    verifyNoMoreInteractions(processingEnvironment, messager)
    assertSame(contextVerifier, subject.contextVerifier)
  }

  @Test
  fun processErrorRaised() {
    val typeElements = mock<MutableSet<TypeElement>>()
    val roundEnvironment = mock<RoundEnvironment> {
      whenever(mock.errorRaised()).thenReturn(true)
    }

    val actual = subject.process(typeElements, roundEnvironment)

    verify(roundEnvironment).errorRaised()
    verifyNoMoreInteractions(typeElements, roundEnvironment)
    assertTrue(actual)
  }

  @Test
  fun processNoErrorRaised() {
    val annotationClass = AutoFactory::class.java
    val annotationClassesIterator = mock<Iterator<Class<out Annotation>>> {
      whenever(mock.hasNext()).thenReturn(true, false)
      whenever(mock.next()).thenReturn(annotationClass)
      whenever(supportedAnnotationClasses.iterator()).thenReturn(mock)
    }
    val typeElements = mock<MutableSet<TypeElement>>()
    val annotatedElement = mock<Element>()
    val annotatedElementsIterator = mock<Iterator<Element>> {
      whenever(mock.hasNext()).thenReturn(true, false)
      whenever(mock.next()).thenReturn(annotatedElement)
    }
    val annotatedElements = mock<Set<Element>> {
      whenever(mock.iterator()).thenReturn(annotatedElementsIterator)
    }
    val roundEnvironment = mock<RoundEnvironment> {
      whenever(mock.errorRaised()).thenReturn(false)
      whenever(mock.getElementsAnnotatedWith(annotationClass)).thenReturn(annotatedElements)
    }
    val contextVerifier = mock<ContextVerifier> {
      subject.contextVerifier = mock
    }

    val actual = subject.process(typeElements, roundEnvironment)

    verify(roundEnvironment).errorRaised()
    verify(supportedAnnotationClasses).iterator()
    verify(annotationClassesIterator, times(2)).hasNext()
    verify(annotationClassesIterator).next()
    verify(roundEnvironment).getElementsAnnotatedWith(annotationClass)
    verify(annotatedElements).iterator()
    verify(annotatedElementsIterator, times(2)).hasNext()
    verify(annotatedElementsIterator).next()
    verify(contextVerifier).verify(annotatedElement)
    verifyNoMoreInteractions(
        annotationClassesIterator,
        typeElements,
        annotatedElementsIterator,
        annotatedElements,
        roundEnvironment,
        contextVerifier)
    assertTrue(actual)
  }

  @Test
  fun getSupportedAnnotationTypes() {
    val iterator = mock<Iterator<Class<out Annotation>>> {
      whenever(mock.hasNext()).thenReturn(false)
    }
    whenever(supportedAnnotationClasses.iterator()).thenReturn(iterator)

    assertEquals(emptySet<String>(), subject.supportedAnnotationTypes)

    verify(supportedAnnotationClasses).iterator()
    verify(iterator).hasNext()
    verifyNoMoreInteractions(iterator)
  }

  @Test
  fun getSupportedOptions() = assertSame(supportedOptions, subject.supportedOptions)

  @Test
  fun getSupportedSourceVersion() = assertSame(supportedSourceVersion, subject.supportedSourceVersion)
}
