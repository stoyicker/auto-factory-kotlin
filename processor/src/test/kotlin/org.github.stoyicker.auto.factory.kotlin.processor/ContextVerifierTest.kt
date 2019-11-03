package org.github.stoyicker.auto.factory.kotlin.processor

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.metadata.jvm.KotlinClassHeader
import kotlinx.metadata.jvm.KotlinClassHeaderFactory
import kotlinx.metadata.jvm.KotlinClassHeaderFactoryHolder
import kotlinx.metadata.jvm.KotlinClassMetadata
import kotlinx.metadata.jvm.KotlinClassMetadataFactory
import kotlinx.metadata.jvm.KotlinClassMetadataFactoryHolder
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import javax.annotation.processing.Messager
import javax.lang.model.element.Element
import javax.tools.Diagnostic

internal class ContextVerifierTest {
  private val messager = mock<Messager>()
  private val subject = ContextVerifier(messager)

  @AfterEach
  fun afterEach() {
    KotlinClassHeaderFactoryHolder.value = KotlinClassHeaderFactory()
    KotlinClassMetadataFactoryHolder.value = KotlinClassMetadataFactory()
    verifyNoMoreInteractions(messager)
  }

  @Test
  fun verifyOnClass() {
    val kind = 8
    val metadataVersion = intArrayOf()
    val byteCodeVersion = intArrayOf()
    val data1 = emptyArray<String>()
    val data2 = emptyArray<String>()
    val extraString = "I am a string"
    val packageName = "I am a packageName"
    val extraInt = -321
    val metadata = mock<Metadata> {
      whenever(mock.kind).thenReturn(kind)
      whenever(mock.metadataVersion).thenReturn(metadataVersion)
      whenever(mock.bytecodeVersion).thenReturn(byteCodeVersion)
      whenever(mock.data1).thenReturn(data1)
      whenever(mock.data2).thenReturn(data2)
      whenever(mock.extraString).thenReturn(extraString)
      whenever(mock.packageName).thenReturn(packageName)
      whenever(mock.extraInt).thenReturn(extraInt)
    }
    val kotlinClassHeader = mock<KotlinClassHeader>()
    val kotlinClassHeaderFactory = mock<KotlinClassHeaderFactory> {
      whenever(
          mock(kind, metadataVersion, byteCodeVersion, data1, data2, extraString, packageName, extraInt))
          .thenReturn(kotlinClassHeader)
    }
    KotlinClassHeaderFactoryHolder.value = kotlinClassHeaderFactory
    val kotlinClassMetadata = mock<KotlinClassMetadata.Class>()
    val kotlinClassMetadataFactory = mock<KotlinClassMetadataFactory> {
      whenever(mock(kotlinClassHeader)).thenReturn(kotlinClassMetadata)
    }
    KotlinClassMetadataFactoryHolder.value = kotlinClassMetadataFactory
    val element = mock<Element> {
      whenever(mock.getAnnotation(Metadata::class.java)).thenReturn(metadata)
    }

    val actual = subject.verify(element)

    verify(element).getAnnotation(Metadata::class.java)
    verify(metadata).kind
    verify(metadata).metadataVersion
    verify(metadata).bytecodeVersion
    verify(metadata).data1
    verify(metadata).data2
    verify(metadata).extraString
    verify(metadata).packageName
    verify(metadata).extraInt
    verify(kotlinClassHeaderFactory)(
        kind, metadataVersion, byteCodeVersion, data1, data2, extraString, packageName, extraInt)
    verify(kotlinClassMetadataFactory)(kotlinClassHeader)
    verifyNoMoreInteractions(
        metadata,
        kotlinClassHeader,
        kotlinClassHeaderFactory,
        kotlinClassMetadata,
        kotlinClassMetadataFactory,
        element)
    assertTrue(actual)
  }

  @Test
  fun verifyOnUnsupported() {
    val element = mock<Element> {
      whenever(mock.getAnnotation(Metadata::class.java)).thenReturn(null)
    }

    val actual = subject.verify(element)

    verify(element).getAnnotation(Metadata::class.java)
    verify(messager).printMessage(Diagnostic.Kind.ERROR, ERROR_UNSUPPORTED_ANNOTATION_TARGET, element)
    verifyNoMoreInteractions(element)
    assertFalse(actual)
  }
}
