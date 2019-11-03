package kotlinx.metadata.jvm

internal object KotlinClassHeaderFactoryHolder {
  var value = KotlinClassHeaderFactory()
}

internal class KotlinClassHeaderFactory :
    (Int?, IntArray?, IntArray?, Array<String>?, Array<String>?, String?, String?, Int?) -> KotlinClassHeader {
  override fun invoke(kind: Int?,
                      metadataVersion: IntArray?,
                      bytecodeVersion: IntArray?,
                      data1: Array<String>?,
                      data2: Array<String>?,
                      extraString: String?,
                      packageName: String?,
                      extraInt: Int?) =
      KotlinClassHeader(kind, metadataVersion, bytecodeVersion, data1, data2, extraString, packageName, extraInt)
}
