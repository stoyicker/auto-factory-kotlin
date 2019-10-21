# auto-factory-kotlin
### An annotation processor for Kotlin and Android that generates factories for your code. Compatible with auto-factory out of the box.
[![CircleCI](https://circleci.com/gh/stoyicker/auto-factory-kotlin.svg?style=svg)](https://circleci.com/gh/stoyicker/auto-factory-kotlin)
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
class SomeClassFactory(@Named("AQualifier") private val providedDepA: Provider<String>) {
  fun create(depB: String) = SomeClass(providedDepA, depB)
}
```
## Features
### Annotation targets
Both constructors and classes can be annotated. If you annotate one or more constructors, a factory class will be 
generated containing a method for each of them. If you annotate the class instead, a factory class with methods for 
all of the constructors in the annotated class will be generated.
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
### Factories for classes that cannot be annotated
Factories for types whose source is out of your control can be requested via extension functions. For example, you can request a factory
for the argument-less `Any` constructor like this:
```kotlin
@AutoFactory
fun Any.factory() = Unit
```
The following code will be generated:
```kotlin
@Generated(value = "org.github.stoyicker.auto.factory.kotlin.processor.AutoFactoryKotlinProcessor")
@Inject
class AnyFactory {
  fun create() = Any()
}
```
Factories generated this way will have methods for constructors matching the signatures of all of the annotated extension methods. Therefore, if you annotate an extension method for a type which does not have a matching constructor (or it is not accessible), although the code will be generated correctly, it will not compile as the targeted constructor does not exist (or is not accessible).
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
### Annotations and options
This project aims to remain compatible with https://github.com/google/auto/tree/master/factory. Please check that
repository for annotation options and documentation.
