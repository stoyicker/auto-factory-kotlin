package javax.lang.model.element

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

internal class ElementKtTest {
  private val subject = mock<Element>()

  @AfterEach
  fun afterEach() = verifyNoMoreInteractions(subject)

  @Test
  fun kMetadataOnElementWithoutAnnotation() {
    whenever(subject.getAnnotation(Metadata::class.java)).thenReturn(null)

    val actual = subject.kMetadata()

    verify(subject).getAnnotation(Metadata::class.java)
    assertNull(actual)
  }
}
