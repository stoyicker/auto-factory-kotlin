package org.github.stoyicker.auto.factory.kotlin.processor

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test

internal class AutoFactoryKotlinProcessorTest {
  private val supportedAnnotationTypes = mock<Set<String>>()
  private val subject = AutoFactoryKotlinProcessor(supportedAnnotationTypes)

  @AfterEach
  fun afterEach() {
    verifyNoMoreInteractions(supportedAnnotationTypes)
  }

  @Test
  fun getSupportedAnnotationTypes() {
    assertSame(supportedAnnotationTypes, subject.supportedAnnotationTypes)
  }
}
