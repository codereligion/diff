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
        .setWorkingObjectname("WorkingObject");
		
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

## Build & deploy

### Setup
* Java 1.6 or higher
* Maven 3

### Run tests locally

```mvn clean test```

### Continuous integration
[![Build Status](https://api.travis-ci.org/codereligion/diff.png?branch=master)](https://api.travis-ci.org/codereligion/diff)

We use Travis CI to run our builds.