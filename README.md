# Diff [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.codereligion/codereligion-diff/badge.svg?style=plastic)](https://maven-badges.herokuapp.com/maven-central/com.codereligion/codereligion-diff) [![Build Status](https://ssl.webpack.de/secure-jenkins.codereligion.com/buildStatus/icon?job=codereligion-diff-master-build-flow)](http://jenkins.codereligion.com/view/codereligion-diff/job/codereligion-diff-master-build-flow/) [![SonarQube Coverage](https://img.shields.io/sonar/http/sonar.codereligion.com/com.codereligion:codereligion-diff/coverage.svg?style=plastic)](http://sonar.codereligion.com/dashboard/index/44)

Allows to diff any two Java objects and retrieve the diff result as a list of strings.
The result is a [unified diff](http://en.wikipedia.org/wiki/Diff#Unified_format) without any contextual lines.

## Requirements
* Java 1.6 or higher
* dependencies see [maven pom](pom.xml)

## Example code
```java
    SomeDomainObject base = getPersistedObject();
    SomeDomainObject working = getManipulatedObject();

    Configuration configuration = new Configuration()
        .excludeProperty("someUnwantedPropertyName")
        .useSerializer(new SomeSerializer())
        .useComparator(new SomeComparator())
        .useNaturalOrderingFor(SomeComparable.class)
        .useBaseObjectName("BaseObject")
        .useWorkingObjectName("WorkingObject");
		
    List<String> differences = new Differ(configuration).diff(base, working);

    for (String diff : differences) {
        System.out.println(diff);
    }
	...
```

## Example output
```diff
--- BaseObject
+++ WorkingObject
@@ -1,1 +1,4 @@
-SomeDomainObject.someIntegerProperty='21'
+SomeDomainObject.someIntegerProperty='42'
+SomeDomainObject.someIterableProperty[0]='foo'
+SomeDomainObject.someIterableProperty[1]='bar'
+SomeDomainObject.someMapProperty['someMapKey']='someMapValue'
...
```

For more details have a look at the [wiki](https://github.com/codereligion/diff/wiki).
