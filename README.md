# Diff

Allows to diff two arbitrary Java objects and retrieve the diff result as a list of strings.

## Example code
```java
    SomeDomainObject base = getPersistedObject();
    SomeDomainObject working = getManipulatedObject();

    DiffConfig diffConfig = new DiffConfig()
        .excludeProperty("someUnwantedPropertyName")
        .addSerializer(new SomeSerializer())
        .addComparator(new SomeComparator())
        .setBaseObjectName("BaseObject")
        .setWorkingObjectName("WorkingObject");
		
    List<String> differences = new Differ(diffConfig).diff(base, working);

    for (String diff : differences) {
        System.out.println(diff);
    }
	...
```
For more details have a look at the [wiki](https://github.com/codereligion/diff/wiki).

## Example output
```
--- BaseObject
+++ WorkingObject
@@ -1,1 +1,1 @@
-SomeDomainObject.someProperty='21'
+SomeDomainObject.someProperty='42'
```

## Build

### Setup
* Java 1.6 or higher
* Maven 3

### Continuous integration and local testing
[![Build Status](https://api.travis-ci.org/codereligion/diff.png?branch=master)](https://travis-ci.org/codereligion/diff)

We use Travis CI to run our builds. The build compiles the code and runs the tests for OpenJDK 6, OpenJDK 7 and Oracle JDK 7.
It will fail on any compiler warnings. In order to debug compiler warnings and errors more efficiently we recommend to install
the above mentioned JDK versions. We also recommend to use [jEnv](http://www.jenv.be/) by Gildas Cuisinier to switch between them easily.

When you run local tests with: ```mvn clean test``` Maven will automatically try to compile the code and run the tests with 
the Java version which is currently set on your system.
