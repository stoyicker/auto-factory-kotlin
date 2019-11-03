package javax.lang.model.element

import kotlinx.metadata.jvm.KotlinClassHeaderFactoryHolder
import kotlinx.metadata.jvm.KotlinClassMetadataFactoryHolder

internal fun Element.kMetadata() = getAnnotation(Metadata::class.java)?.run {
  KotlinClassHeaderFactoryHolder.value(
      kind, metadataVersion, bytecodeVersion, data1, data2, extraString, packageName, extraInt)
      .let { KotlinClassMetadataFactoryHolder.value(it) }
}
