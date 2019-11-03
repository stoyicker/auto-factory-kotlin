package javax.lang.model.element

import kotlinx.metadata.jvm.KotlinClassHeader
import kotlinx.metadata.jvm.KotlinClassMetadata

internal fun Element.kMetadata() = getAnnotation(Metadata::class.java)?.run {
  KElementFactories.kotlinClassHeaderFactory(
      kind, metadataVersion, bytecodeVersion, data1, data2, extraString, packageName, extraInt)
}?.let { KElementFactories.kotlinClassMetadataFactory(it) }

internal object KElementFactories {
  var kotlinClassHeaderFactory = { kind: Int?,
                                   metadataVersion: IntArray?,
                                   bytecodeVersion: IntArray?,
                                   data1: Array<String>?,
                                   data2: Array<String>?,
                                   extraString: String?,
                                   packageName: String?,
                                   extraInt: Int? ->
    KotlinClassHeader(
        kind,
        metadataVersion,
        bytecodeVersion,
        data1,
        data2,
        extraString,
        packageName,
        extraInt)
  }
  var kotlinClassMetadataFactory = { header: KotlinClassHeader ->
    KotlinClassMetadata.read(header)
  }
}
