## Java Collection Framework
The Java language development team that created the original `java.util` collection classes was fairly new
to object oriented design and collection frameworks and therefore made some common mistakes:
 * using implementation inheritance instead of delegation (a `Stack` is **not** a `Vector`!)
 * exposing underlying implementations (e.g. hashtable, linked list, and tree)
 * confusing _ordered_ and _sortable_ collections (e.g. `SortedSet` which is really an _ordered_ set)

We forgive them, however, since they got a lot of things right :wink:.

Subsequent additions to the `java.util` package helped address some of these issues, but the designers were
somewhat constrained by the original mistakes to maintain backward compatibility.

## A Clean Slate
This project starts with a clean slate. It defines a set of Java collection classes that are independent
of the `java.util` collection classes but that still interoperate with them and also integrate with the
built-in Java language features that depend on the `java.lang.Iterable` interface. These classes provide
the following nice features:

 * *simple*, easy to understand interfaces
 * *self-optimizing* implementations
 * a well designed *framework* in which to work

## Highlighted Components
The following highlights the main types of collections that this project provides:

 * *Bag* - an ordered collection where duplicates are allowed
 * *Set* - an ordered collection where duplicates are *not* allowed
 * *List* - a sortable collection with implicit indexes
 * *Map* - a sortable collection with explicit indexes
 * *Stack* - a collection that supports last in first out (LIFO)
 * *Queue* - a collection that supports first in first out (FIFO)

## Quick Links
For more detail on this project click on the following links:

 * [javadocs](http://craterdog.github.io/java-collection-framework/3.3/index.html)
 * [wiki](https://github.com/craterdog/java-collection-framework/wiki)
 * [release notes](https://github.com/craterdog/java-collection-framework/wiki/Release-Notes)
 * [website](http://craterdog.com)

## Getting Started
To get started using these classes, include the following dependency in your maven pom.xml file:

```xml
    <dependency>
        <groupId>com.craterdog</groupId>
        <artifactId>java-collection-framework</artifactId>
        <version>3.3</version>
    </dependency>
```

The source code, javadocs and jar file artifacts for this project are available from the
*Maven Central Repository*. If your project doesn't currently use maven and you would like to,
click [here](https://github.com/craterdog/maven-parent-poms) to get started down that path quickly.

## Choosing a Collection
The following flow chart provides a way to easily choose which collection type is right for your
needs:

![Collection Flow Chart](https://github.com/craterdog/java-collection-framework/blob/master/docs/images/FlowChart.png)

No need to worry about the underlying implementation of each type of collection, they are all
self-optimizing and adjust automatically to varying read/update percentages.
