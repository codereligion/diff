# Diff [![Build Status](https://api.travis-ci.org/codereligion/diff.png?branch=master)](https://travis-ci.org/codereligion/diff)

Allows to diff two arbitrary Java objects and retrieve the diff result as a list of strings.

## Requirements
* Java 1.5 or higher
* dependencies see [maven pom](pom.xml)

## Maven ##
```xml
<dependency>
	<groupId>com.codereligion</groupId>
	<artifactId>codereligion-diff</artifactId>
	<version>0.1.0-SNAPSHOT</version>
</dependency>
```

## Example code
```java
    SomeDomainObject base = getPersistedObject();
    SomeDomainObject working = getManipulatedObject();

    DiffConfig diffConfig = new DiffConfig()
        .excludeProperty("someUnwantedPropertyName")
        .useSerializer(new SomeSerializer())
        .useComparator(new SomeComparator())
        .useNaturalOrderingFor(SomeComparable.class)
        .useBaseObjectName("BaseObject")
        .useWorkingObjectName("WorkingObject");
		
    List<String> differences = new Differ(diffConfig).diff(base, working);

    for (String diff : differences) {
        System.out.println(diff);
    }
	...
```

## Example output
```diff
--- BaseObject
+++ WorkingObject
@@ -1,1 +1,1 @@
-SomeDomainObject.someIntegerProperty='21'
+SomeDomainObject.someIntegerProperty='42'
+SomeDomainObject.someIterableProperty[0]='foo'
+SomeDomainObject.someIterableProperty[1]='bar'
+SomeDomainObject.someMapProperty['someMapKey']='someMapValue'
...
```

For more details have a look at the [wiki](https://github.com/codereligion/diff/wiki).
