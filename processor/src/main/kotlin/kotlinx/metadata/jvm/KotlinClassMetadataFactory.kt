package kotlinx.metadata.jvm

internal object KotlinClassMetadataFactoryHolder {
  var value = KotlinClassMetadataFactory()
}

internal class KotlinClassMetadataFactory : (KotlinClassHeader) -> KotlinClassMetadata? {
  override fun invoke(header: KotlinClassHeader) = KotlinClassMetadata.read(header)
}
