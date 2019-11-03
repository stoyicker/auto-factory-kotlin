# auto-factory-kotlin
### An annotation processor for Kotlin and Android that generates factories for your code. Compatible with auto-factory out of the box.
[![CircleCI](https://circleci.com/gh/stoyicker/auto-factory-kotlin.svg?style=svg)](https://circleci.com/gh/stoyicker/auto-factory-kotlin)
[![codecov](https://codecov.io/gh/stoyicker/auto-factory-kotlin/branch/master/graph/badge.svg)](https://codecov.io/gh/stoyicker/auto-factory-kotlin)
## Usage
[ ![Download](https://api.bintray.com/packages/stoyicker/auto-factory-kotlin/annotations/images/download.svg) ](https://search.maven.org/search?q=g:com.github.stoyicker.auto-factory-kotlin)

Add the dependencies to your project:
<details open>
<summary><b>Kotlin</b></summary>

```groovy
repositories {
  jcenter()
}
dependencies {
    // 1. Add the annotations
    compileOnly "com.github.stoyicker.auto-factory-kotlin:annotations:<version>"
    // 2. Add the processor (you must be using kotlin-kapt)
    kapt "com.github.stoyicker.auto-factory-kotlin:processor:<version>"
}
```

</details>
<details open>
<summary><b>Groovy</b></summary>

```groovy
repositories {
  jcenter()
}
dependencies {
    // 1. Add the annotations
    compileOnly("com.github.stoyicker.auto-factory-kotlin:annotations:<version>")
    // 2. Add the processor (you must be using kotlin-kapt)
    kapt("com.github.stoyicker.auto-factory-kotlin:processor:<version>")
}
```

</details>

Now annotate your class:
```kotlin
@AutoFactory
class SomeClass(@Provided @Named("AQualifier") private val providedDepA: String, private val providedDepB: String)
```
The following code will be generated:
```kotlin
@Generated(value = "org.github.stoyicker.auto.factory.kotlin.processor.AutoFactoryKotlinProcessor")
@Inject
class SomeClassFactory(@Named("AQualifier") private val providedDepAProvider: Provider<String>) {
  fun create(depB: String) = SomeClass(providedDepAProvider.get(), depB)
}
```
## Features
### Annotation targets
Both classes and typealias can be annotated. If you annotate a class, the generated factory contains methods for all of 
the public and internal constructors in the target class. If you annotate a typealias, only methods for public
constructors will be generated to ensure that there are no scenarios where compilation could be broken. 
```kotlin
class SomeClass(private val dep: String) {
  @AutoFactory
  constructor() : this("a value")
}
```
The following code will be generated:
```kotlin
@Generated(value = "org.github.stoyicker.auto.factory.kotlin.processor.AutoFactoryKotlinProcessor")
@Inject
class SomeClassFactory {
  fun create() = SomeClass()
}
```
### Generics
Generics are 100% respected. For example, with this code:
```kotlin
class SomeClass<A : Any> {
  @AutoFactory
  constructor(a: A, list: List<String>) : this()
}
```
The following code will be generated:
```kotlin
@Generated(value = "org.github.stoyicker.auto.factory.kotlin.processor.AutoFactoryKotlinProcessor")
@Inject
class SomeClassFactory<A : Any> {
  fun create(a: A, list: List<String>) = SomeClass(a, list)
}
```
### Factories for external classes
Factories for types whose source is out of your control can be requested via typealias. For example, you can request a 
factory for the public `Any` constructors like this:
```kotlin
@AutoFactory(name = "AnyFactory") // name is not mandatory, but it is a good idea to keep name consistent with the default rules
typealias AnyAlias = Any
```
The following code will be generated:
```kotlin
@Generated(value = "org.github.stoyicker.auto.factory.kotlin.processor.AutoFactoryKotlinProcessor")
@Inject
class AnyAliasFactory {
  fun create() = AnyAlias()
}
```
### Default arguments
Default arguments are respected. If a factory method is generated for a constructor which has at least one argument
with default values, such defaults will be honored in the generated code. For example, if you have:
```kotlin
@AutoFactory
class SomeClass(private val dep: String = "hello default arguments")
```
The following code will be generated:
```kotlin
@Generated(value = "org.github.stoyicker.auto.factory.kotlin.processor.AutoFactoryKotlinProcessor")
@Inject
class SomeClassFactory {
  fun create(dep: String = "hello default arguments") = SomeClass(dep)
}
```
## Annotations and options
[See the annotations for documentation.](annotations/src/main/kotlin/com/google/auto/factory)
### License
   Copyright 2019 Jorge Antonio Diaz-Benito Soriano

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

The annotations module is derived from the [annotations at google/auto](https://github.com/google/auto/tree/6c2c1a3ff8d2abc74755dfe18c07fa60ee1c7733/factory/src/main/java/com/google/auto/factory)
with minor modifications to their source documentation.
