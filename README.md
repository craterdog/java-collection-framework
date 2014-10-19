# Java Collection Framework

## A Gist of It
The Java language development team that created the original `java.util` collection classes was fairly
new to object oriented design and collection frameworks and therefore made some common mistakes:
 * using *implementation inheritance* instead of *delegation* (a Stack is **not** a Vector!)
 * exposing underlying implementations (e.g. hashtable, linked list, and tree)
 * confusing *ordered* and *sortable* collections (e.g. SortedSet)

We forgive them, however, since they got a lot of things right :wink:.

Subsequent additions to the `java.util` package helped address some of these issues, but the designers were
somewhat constrained by the original mistakes to maintain backward compatibility.

## A Clean Slate
This project starts with a clean slate. It defines a set of Java collection classes that are independent
of the `java.util` collection classes but that still interoperate with them and also integrate with the built-in Java language features
that depend on the `java.lang.Iterable` interface. These classes provide the following nice features:
 * *simple*, easy to understand interfaces
 * *self-optimizing* implementations
 * a well designed *framework* in which to work

## Main Collection Classes
The following highlights the main types of collections that this project provides:

 * *Bag*
 * *Set*
 * *List*
 * *Map*
 * *Stack*
 * *Queue*

## Quick Links
For more detail on this project click on the following links:
 * [javadocs](http://craterdog.github.io/java-collection-framework/3.0/index.html)
 * [wiki](https://github.com/craterdog/java-collection-framework/wiki/Crater-Dog-Technologies%E2%84%A2-Java-Collection-Framework)
 * [website](http://craterdog.com)

## Getting Started
To get started using these classes, include the following dependency in your maven pom.xml file:

```xml
    <dependency>
        <groupId>com.craterdog</groupId>
        <artifactId>java-collection-framework</artifactId>
        <version>3.0</version>
    </dependency>
```

## Choosing a Collection
The following flow chart provides a way to easily choose which collection type is right for your
needs:

![Collection Flow Chart](https://github.com/craterdog/java-collection-framework/blob/master/docs/images/FlowChart.png)
