package sample

import com.google.auto.factory.AutoFactory

@AutoFactory
class Public internal constructor() {
  constructor(ignored: Nothing): this()
}

@AutoFactory
internal class Internal public constructor() {
  internal constructor(ignored: Nothing): this()
}

@AutoFactory
typealias PublicAnyTypeAlias = Any

@AutoFactory
internal typealias InternalAnyTypeAlias = Any
